package com.morefun.XSanGo.partner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.XSanGo.Protocol.HeroState;
import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.PartnerView;
import com.XSanGo.Protocol.PartnerViewInfo;
import com.XSanGo.Protocol.PropertyConfig;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RolePartner;
import com.morefun.XSanGo.equip.EquipItem;
import com.morefun.XSanGo.event.protocol.IAttendantChange;
import com.morefun.XSanGo.event.protocol.IHeroBreakUp;
import com.morefun.XSanGo.event.protocol.IHeroEquipChange;
import com.morefun.XSanGo.event.protocol.IHeroPractice;
import com.morefun.XSanGo.event.protocol.IHeroQualityUp;
import com.morefun.XSanGo.event.protocol.IHeroSkillUp;
import com.morefun.XSanGo.event.protocol.IHeroStarUp;
import com.morefun.XSanGo.event.protocol.IPartnerClear;
import com.morefun.XSanGo.event.protocol.IPartnerPosChange;
import com.morefun.XSanGo.event.protocol.IPartnerPosOpen;
import com.morefun.XSanGo.event.protocol.IPartnerPosReset;
import com.morefun.XSanGo.event.protocol.IPartnerStateChange;
import com.morefun.XSanGo.event.protocol.IResetPractice;
import com.morefun.XSanGo.formation.IFormation;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.hero.PositionData;
import com.morefun.XSanGo.hero.RelationT;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.CellWrap;
import com.morefun.XSanGo.util.IRandomHitable;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.RandomRange;
import com.morefun.XSanGo.util.TextUtil;

public class PartnerControler implements IPartnerControler, IHeroEquipChange, IHeroQualityUp, IHeroStarUp,
		IHeroSkillUp, IAttendantChange, IHeroPractice, IResetPractice, IHeroBreakUp {

	private IRole roleRt;
	private Role roleDb;
	private IPartnerClear partnerClearEvent;
	private IPartnerPosChange partnerPosChangeEvent;
	private IPartnerPosOpen partnerPosOpenEvent;
	private IPartnerPosReset partnerPosResetEvent;
	private IPartnerStateChange partnerStateChange;
	private XsgPartner xsgPartner;

	public PartnerControler(IRole roleRt, Role roleDb) {
		this.roleRt = roleRt;
		this.roleDb = roleDb;

		RolePartner rolePartner = roleDb.getRolePartner();
		if (null != rolePartner) {
			xsgPartner = new XsgPartner(roleRt, rolePartner);
		}

		partnerClearEvent = this.roleRt.getEventControler().registerEvent(IPartnerClear.class);
		partnerPosChangeEvent = this.roleRt.getEventControler().registerEvent(IPartnerPosChange.class);
		partnerPosOpenEvent = this.roleRt.getEventControler().registerEvent(IPartnerPosOpen.class);
		partnerPosResetEvent = this.roleRt.getEventControler().registerEvent(IPartnerPosReset.class);
		partnerStateChange = this.roleRt.getEventControler().registerEvent(IPartnerStateChange.class);

		roleRt.getEventControler().registerHandler(IHeroEquipChange.class, this);
		roleRt.getEventControler().registerHandler(IHeroQualityUp.class, this);
		roleRt.getEventControler().registerHandler(IHeroStarUp.class, this);
		roleRt.getEventControler().registerHandler(IHeroSkillUp.class, this);
		roleRt.getEventControler().registerHandler(IAttendantChange.class, this);
		roleRt.getEventControler().registerHandler(IHeroPractice.class, this);
		roleRt.getEventControler().registerHandler(IResetPractice.class, this);
		roleRt.getEventControler().registerHandler(IHeroBreakUp.class, this);
	}

	@Override
	public PartnerView getPartnerView() throws NoteException {

		PartnerView view = new PartnerView();
		List<PartnerViewInfo> list = new ArrayList<PartnerViewInfo>();
		// 获取数据库里的伙伴最大阵位
		PartnerConfig config = null;
		// 根据vip等级更新数据信息
		RolePartner rolePartner = roleDb.getRolePartner();
		if (rolePartner != null) {
			config = TextUtil.GSON.fromJson(rolePartner.getConfigs(), PartnerConfig.class);
		}

		IntString propResetCost = XsgGameParamManager.getInstance().getPartnerPropResetCost();
		for (int i = 1; i <= XsgPartnerManager.getInstance().getPosList().size(); i++) {// 将7个位置的信息全部返回
			PartnerPosT partnerT = XsgPartnerManager.getInstance().getPartnerTById(i);
			if (rolePartner == null) {// 所有位置均未开放
				PartnerViewInfo info = new PartnerViewInfo(null, null, i, 0, 0, new PropertyConfig(),
						partnerT.position, partnerT.level, partnerT.type, partnerT.amount, partnerT.vipLevel,
						partnerT.lockPay, partnerT.openCondition, propResetCost.strValue, propResetCost.intValue);
				list.add(info);
			} else {// 部分位置已经开放
				Map<Integer, String> heroPos = config.heroPosition;
				PartnerViewInfo info = new PartnerViewInfo(null, null, i, 0, 0, null, partnerT.position,
						partnerT.level, partnerT.type, partnerT.amount, partnerT.vipLevel, partnerT.lockPay,
						partnerT.openCondition, propResetCost.strValue, propResetCost.intValue);
				// 判断该位置是否有武将
				if (heroPos != null && !TextUtil.isBlank(heroPos.get(i))) {
					info.heroId = heroPos.get(i);
					info.heroFlag = 1;
				}
				// 获取位置属性加成
				PropertyConfig pc = null;
				// 获取数据库中的 位置-属性id 信息
				Map<Integer, Integer> positionProp = config.positionProp;
				if (positionProp != null && positionProp.get(i) != null) {
					int propId = positionProp.get(i);
					PartnerPropT propT = XsgPartnerManager.getInstance().findPropById(propId);
					pc = new PropertyConfig(propT.id, propT.propName, propT.color, propT.propPercent);
					info.proConfig = pc;
				} else {
					info.proConfig = new PropertyConfig();
				}
				// 获取专属武将模版ID
				if (config.specialHeroPosition != null && config.specialHeroPosition.get(i) != null) {
					info.specialHero = config.specialHeroPosition.get(i);
					info.openFlag = 1;
				}
				list.add(info);
			}
		}

		view.yfdNum = roleRt.getItemControler().getItemCountInPackage("ydan");
		view.infoSeq = list.toArray(new PartnerViewInfo[0]);

		return view;
	}

	/**
	 * 按照脚本权重，随机获取一个不在指定集合中的武将配置
	 * 
	 * @param heroIds
	 *            武将模版ID集合
	 * @return
	 */
	private PartnerHeroT getRandomHero(List<String> heroIds) {

		Map<Integer, PartnerHeroT> propMap = XsgPartnerManager.getInstance().getHeroMap();
		List<PartnerControler.RandomProp> randomProps = new ArrayList<PartnerControler.RandomProp>();
		for (PartnerHeroT p : propMap.values()) {// 排除已有记录
			if (!heroIds.contains(p.id + "")) {
				randomProps.add(new RandomProp(p.id, p.weight));
			}
		}
		if (randomProps.size() <= 0) {
			return null;
		}
		RandomRange<RandomProp> randomRewardGen = new RandomRange<RandomProp>(randomProps);
		RandomProp randomReward = randomRewardGen.random();
		return propMap.get(randomReward.id);
	}

	/**
	 * 按照脚本权重，随机获取一个不在指定集合中 的伙伴属性
	 * 
	 * @param propNames
	 *            伙伴属性名称集合
	 * @return
	 */
	private PartnerPropT getRandomProp(List<String> propNames) {

		Map<Integer, PartnerPropT> propMap = XsgPartnerManager.getInstance().getPropMap();

		List<PartnerControler.RandomProp> randomProps = new ArrayList<PartnerControler.RandomProp>();
		for (PartnerPropT p : propMap.values()) {
			if (!propNames.contains(p.propName)) {// 排除已有记录
				randomProps.add(new RandomProp(p.id, p.weight));
			}
		}

		if (randomProps.size() <= 0) {
			return null;
		}

		RandomRange<RandomProp> randomRewardGen = new RandomRange<RandomProp>(randomProps);
		RandomProp randomReward = randomRewardGen.random();
		return propMap.get(randomReward.id);

	}

	static class RandomProp implements IRandomHitable {
		public int id;
		public int rank;

		public RandomProp(int id, int rank) {
			this.id = id;
			this.rank = rank;
		}

		@Override
		public int getRank() {
			return rank;
		}
	}

	@Override
	public void setPartnerPosition(byte position, IHero hero, IHero oldHero, boolean b) throws NoteException {

		// 1、判断位置是否合法
		PartnerPosT partnerPosT = XsgPartnerManager.getInstance().getPartnerTById(position);

		if (partnerPosT == null) {
			LogManager.warn(TextUtil.format("role[" + roleRt.getRoleId() + "] partner position [" + position
					+ "] is invalid!"));
			throw new IllegalArgumentException();
		}
		// 2、判断等级是否合法
		PartnerLevelRequiredT reuiredLevel = XsgPartnerManager.getInstance().getReuiredLevel();
		if (reuiredLevel.level > roleRt.getLevel()) {
			throw new NoteException(TextUtil.format(Messages.getString("PartnerController.levelRequired"),
					reuiredLevel.level));
		}

		RolePartner rolePartner = roleDb.getRolePartner();
		// XsgPartner partner = new XsgPartner(roleRt, roleDb.getRolePartner());
		xsgPartner = new XsgPartner(roleRt, roleDb.getRolePartner());
		// 3、判断阵位是否开启
		// if (!partner.isOpened((int) position)) {
		if (!xsgPartner.isOpened((int) position)) {
			LogManager.warn(TextUtil.format("role[" + roleRt.getRoleId() + "] position: " + position
					+ ", is out of maxPos: " + rolePartner.getMaxPos()));
			throw new IllegalArgumentException();
		}

		if (hero != null && hero == oldHero) {
			throw new NoteException(Messages.getString("PartnerController.sameHero"));
		}

		// 阵容中两者互换hero.getHeroPositionData()中position从对应伙伴位置1-7
		if (hero != null && oldHero != null && HeroState.PartnerShip.equals(hero.getState())) {

			try {
				this.exchangePosition(xsgPartner, position, hero.getHeroPositionData().position);
			} catch (Exception e) {
				throw new NoteException(Messages.getString("PartnerController.setFailed"));
			}
		}

		if (hero != null) {
			PositionData pd = hero.getHeroPositionData();
			// 武将hero从伙伴阵位的一个位置移动到另一个位置
			if (pd != null && TextUtil.isBlank(pd.formationId) && !hero.isAttendant()) {
				// 移除旧的位置
				xsgPartner.setHeroPosition(pd.position, null);
			} else {// 移除两个武将 的所有关系

				this.roleRt.getHeroControler().removeReference(hero, true, true);

				if (oldHero != null) {
					this.roleRt.getHeroControler().removeReference(oldHero, true, true);
				}

				pd = new PositionData();
			}

			// 伙伴阵位中新加武将
			pd.partnerId = rolePartner.getId();
			pd.position = position;
			hero.setHeroPositionData(pd);
		}
		xsgPartner.setHeroPosition(position, hero);
		roleDb.setRolePartner(rolePartner);

		// 事件变更通知
		this.partnerChange(xsgPartner);
		partnerPosChangeEvent.onHeroPositionChange(xsgPartner, position, hero);

	}

	/**
	 * 伙伴阵容中，武将位置互换
	 * 
	 * @param partner
	 *            伙伴对象
	 * @param pos1
	 *            目标位置
	 * @param pos2
	 *            指定武将所在位置
	 */
	private void exchangePosition(XsgPartner partner, byte pos1, int pos2) throws NoteException {

		IHero hero1 = partner.getHeroByPos(pos1);
		IHero hero2 = partner.getHeroByPos(pos2);

		if (hero1 == null || hero2 == null) {
			throw new NoteException(Messages.getString("FormationControler.5")); //$NON-NLS-1$
		}
		// 直接交换两个武将在伙伴中的位置
		partner.setHeroPosition(pos1, hero2);
		partner.setHeroPosition(pos2, hero1);
		// 武将状态数据交换
		PositionData temp = hero1.getHeroPositionData();
		hero1.setHeroPositionData(hero2.getHeroPositionData());
		hero2.setHeroPositionData(temp);

		// 触发事件
		// 事件变更通知
		partnerPosChangeEvent.onHeroPositionChange(partner, pos1, hero2);
		partnerPosChangeEvent.onHeroPositionChange(partner, pos2, hero1);
		this.partnerChange(partner);
	}

	@Override
	public PartnerViewInfo openPartnerPosition(byte position) throws NoteException {
		// 开启伙伴阵位的时候，一定要设置该阵位的随机属性和特殊武将，且都不可和已有记录重复
		if (position <= 0 || position > XsgPartnerManager.getInstance().getPosList().size()) {
			throw new NoteException(Messages.getString("FormationI.0"));
		}

		PartnerLevelRequiredT reuiredLevel = XsgPartnerManager.getInstance().getReuiredLevel();
		if (reuiredLevel.level > roleRt.getLevel()) {
			throw new NoteException(TextUtil.format(Messages.getString("PartnerController.levelRequired"),
					reuiredLevel.level));
		}

		PartnerPosT partnerT = XsgPartnerManager.getInstance().getPartnerTById(position);

		if (partnerT == null) {
			throw new NoteException(Messages.getString("FormationI.0"));
		}

		// 检查是否满足开启条件
		boolean flag = this.checkOpenCondition(partnerT, partnerT.amount);

		if (!flag) {
			throw new NoteException(Messages.getString("PartnerController.openCondition"));
		}

		RolePartner rolePartner = roleDb.getRolePartner();
		if (rolePartner == null) {// 初次开启

			PartnerConfig pc = new PartnerConfig();
			// 随机获取指定位置的属性
			Map<Integer, Integer> positionProp = new HashMap<Integer, Integer>();
			PartnerPropT randomProp = this.getRandomProp(new ArrayList<String>());
			positionProp.put((int) position, randomProp.id);
			pc.positionProp = positionProp;
			// 随机获取指定位置的特殊武将
			Map<Integer, String> specialHeroPosition = new HashMap<Integer, String>();
			PartnerHeroT randomHero = this.getRandomHero(new ArrayList<String>());
			specialHeroPosition.put((int) position, randomHero.id + "");
			pc.specialHeroPosition = specialHeroPosition;

			String configs = TextUtil.GSON.toJson(pc);
			rolePartner = new RolePartner(roleRt.getRoleId(), roleDb, position, configs);
			roleDb.setRolePartner(rolePartner);
			partnerPosOpenEvent.onPartnerPositionChange(position, randomProp.id, randomHero.id + "");
		} else {
			this.startNewPosition(position, rolePartner);
		}

		// 扣除货币
		if (partnerT.type == 1) {// 1 金币，2 元宝
			this.roleRt.getRewardControler().acceptReward(Const.PropertyName.MONEY, -partnerT.amount);
		} else if (partnerT.type == 2) {
			this.roleRt.getRewardControler().acceptReward(Const.PropertyName.RMBY, -partnerT.amount);
		} else if (partnerT.type == 0) {// 0 表示无消耗开
		} else {
			throw new NoteException(Messages.getString("PartnerController.invalidCurrency"));
		}

		xsgPartner = new XsgPartner(roleRt, rolePartner);
		IntString propResetCost = XsgGameParamManager.getInstance().getPartnerPropResetCost();
		int pos = position;
		String heroId = xsgPartner.getHeroByPos(pos) == null ? null : xsgPartner.getHeroByPos(pos).getId();
		PartnerHeroT specialHeroT = xsgPartner.getSpecialHeroMap().get(pos);
		PartnerPropT prop = xsgPartner.getPropByPos(pos);
		PropertyConfig pConf = new PropertyConfig(prop.id, prop.propName, prop.color, prop.propPercent);

		return new PartnerViewInfo(heroId, specialHeroT == null ? null : specialHeroT.id + "", pos, heroId != null ? 1
				: 0, 1, pConf, partnerT.position, partnerT.level, partnerT.type, partnerT.amount, partnerT.vipLevel,
				partnerT.lockPay, partnerT.openCondition, propResetCost.strValue, propResetCost.intValue);
	}

	/**
	 * 在已有伙伴阵位的基础上开启一个新的指定伙伴阵位
	 * 
	 * @param position
	 *            阵位位置
	 * @param rolePartner
	 * @throws NoteException
	 */
	private void startNewPosition(byte position, RolePartner rolePartner) throws NoteException {
		// 已经开启部分阵位
		// XsgPartner partner = new XsgPartner(roleRt, rolePartner);
		xsgPartner = new XsgPartner(roleRt, rolePartner);
		// 判断开启位置是否合法
		if (xsgPartner.isOpened(position)) {
			throw new NoteException(Messages.getString("FormationI.0"));
		}

		PartnerConfig pc = TextUtil.GSON.fromJson(rolePartner.getConfigs(), PartnerConfig.class);
		// 生成不重复的随机属性和特殊武将
		List<String> props = new ArrayList<String>();
		List<String> specialHeroIds = new ArrayList<String>();

		if (pc.positionProp.size() > 0) {
			Map<Integer, Integer> propMap = pc.positionProp;
			for (Entry<Integer, Integer> en : propMap.entrySet()) {
				Integer id = en.getValue();
				PartnerPropT propT = XsgPartnerManager.getInstance().findPropById(id);
				props.add(propT.propName);
			}
		}

		if (pc.specialHeroPosition.size() > 0) {
			Map<Integer, String> speHeroMap = pc.specialHeroPosition;

			for (Entry<Integer, String> en : speHeroMap.entrySet()) {
				String value = en.getValue();
				specialHeroIds.add(value);
			}
			// specialHeroIds.addAll(pc.specialHeroPosition.values());
		}
		PartnerHeroT randomHero = this.getRandomHero(specialHeroIds);
		PartnerPropT randomProp = this.getRandomProp(props);

		if (randomHero == null || randomProp == null) {
			NoteException note = new NoteException(Messages.getString("PartnerController.scriptError"));
			throw note;
		}

		int propId = randomProp.id;
		String heroId = randomHero.id + "";

		pc.positionProp.put((int) position, propId); // 保存位置及属性模版id
		pc.specialHeroPosition.put((int) position, heroId); // 保存位置及特殊英雄模版id

		String configs = TextUtil.GSON.toJson(pc);
		rolePartner.setConfigs(configs);
		// 更新最大开启阵位
		rolePartner.setMaxPos(position);
		roleDb.setRolePartner(rolePartner);

		partnerPosOpenEvent.onPartnerPositionChange(position, propId, randomHero.id + "");
	}

	/**
	 * 检查当前roleRt是否满足开启指定伙伴阵位的条件
	 * 
	 * @param partnerT
	 * @param cost
	 * @return
	 */
	private boolean checkOpenCondition(PartnerPosT partnerT, int cost) {

		int level = partnerT.level;
		int type = partnerT.type;
		int amount = partnerT.amount;
		// 判断等级要求 partnerT.openCondition==1 vip等级和主公等级有一个满足，即可开放
		if (partnerT.openCondition == 1// 此时，主公等级和vip等级都不满足时返回false
				&& (roleRt.getLevel() < level && roleRt.getVipLevel() < partnerT.vipLevel)) {
			return false;
		}
		// partnerT.openCondition==2 是交集 vip等级和主公等级都满足的时候才开放
		if (partnerT.openCondition == 2// 此时，主公等级和vip等级只要有一个不满足时返回false
				&& (roleRt.getLevel() < level || roleRt.getVipLevel() < partnerT.vipLevel)) {
			return false;
		}

		// 购买伙伴位，判断货币是否充足
		switch (type) {// 1 金币，2 元宝
		case 1:
			if (amount <= roleRt.getJinbi()) {
				return true;
			}
			break;
		case 2:
			if (amount <= roleRt.getTotalYuanbao()) {
				return true;
			}
			break;
		case 0: // 免费开
			return true;
		}
		return false;
	}

	@Override
	public IPartner getPartner() {

		return roleDb.getRolePartner() == null ? null : new XsgPartner(roleRt, roleDb.getRolePartner());

	}

	@Override
	public PartnerViewInfo resetPartnerPosition(int postion, int cost, int isLock) throws NoteException {
		// 判断伙伴位置是否已经开启
		RolePartner rolePartner = roleDb.getRolePartner();
		// XsgPartner partner = new XsgPartner(roleRt, rolePartner);
		if (rolePartner == null || !xsgPartner.isOpened(postion)) {
			NoteException note = new NoteException(Messages.getString("PartnerController.notOpened"));
			LogManager.error(note);
			throw note;
		}

		PartnerLevelRequiredT reuiredLevel = XsgPartnerManager.getInstance().getReuiredLevel();
		if (reuiredLevel.level > roleRt.getLevel()) {
			throw new NoteException(TextUtil.format(Messages.getString("PartnerController.levelRequired"),
					reuiredLevel.level));
		}

		// 判断缘分丹是否足够
		int itemCountInPackage = roleRt.getItemControler().getItemCountInPackage("ydan");
		if (itemCountInPackage < cost && isLock != 2) {
			throw new NoteException(Messages.getString("PartnerController.yfdLack"));
		}
		
		// 锁定属性时判断缘分丹是否足够
		IntString costStr = XsgGameParamManager.getInstance().getPartnerPropResetCost();
		if(isLock == 2){//锁定属性时
			int costNum = roleRt.getItemControler().getItemCountInPackage(costStr.strValue);
			if (costNum < costStr.intValue) {
				throw new NoteException(Messages.getString("HeroControler.33"));
			}
		}

		PartnerConfig pc = TextUtil.GSON.fromJson(rolePartner.getConfigs(), PartnerConfig.class);
		String heroIdOnPos = pc.heroPosition != null ? pc.heroPosition.get(postion) : null;
		String heroCode = null;

		if (TextUtil.isNotBlank(heroIdOnPos)) {
			IHero hero = roleRt.getHeroControler().getHero(heroIdOnPos);
			if (null != hero) {
				heroCode = hero.getTemplateId() + "";
			}
		}

		// 生成不重复的随机属性和特殊武将
		List<String> props = new ArrayList<String>();
		List<String> specialHeroIds = new ArrayList<String>();

		if (pc.positionProp.size() > 0) {
			Map<Integer, Integer> propMap = pc.positionProp;
			for (Entry<Integer, Integer> en : propMap.entrySet()) {
				Integer id = en.getValue();
				PartnerPropT propT = XsgPartnerManager.getInstance().findPropById(id);
				props.add(propT.propName);
			}
		}

		if (pc.specialHeroPosition.size() > 0) {
			specialHeroIds.addAll(pc.specialHeroPosition.values());
		}

		Map<Integer, PartnerPropT> propMap = XsgPartnerManager.getInstance().getPropMap();
		if (isLock != 2) {// 2表示锁定属性 此时重置不更新属性
			PartnerPropT randomProp = this.getRandomProp(props);

			if (randomProp == null) {
				NoteException note = new NoteException(Messages.getString("PartnerController.scriptError"));
				throw note;
			}
			int propId = randomProp.id;
			pc.positionProp.put(postion, propId); // 保存位置及属性模版id
		}
		PartnerPropT partnerPropT = propMap.get(pc.positionProp.get(postion));

		String heroId = pc.specialHeroPosition.get(postion);
		// 重置前判断是否有激活
		byte activedBefore = 0;
		if (TextUtil.isNotBlank(heroId) && TextUtil.isNotBlank(heroCode) && heroId.equals(heroCode)) {
			activedBefore = 1;
		}

		if (isLock != 1) {// 1 表示锁定专属武将
			PartnerHeroT randomHero = this.getRandomHero(specialHeroIds);
			if (randomHero == null) {
				NoteException note = new NoteException(Messages.getString("PartnerController.scriptError"));
				throw note;
			}
			heroId = randomHero.id + "";
			pc.specialHeroPosition.put(postion, heroId); // 保存位置及特殊英雄模版id
		}

		String configs = TextUtil.GSON.toJson(pc);
		rolePartner.setConfigs(configs);

		roleDb.setRolePartner(rolePartner);

		// 扣除掉重置消耗
		if (isLock == 2) {// 锁定属性时
			int costNum = roleRt.getItemControler().getItemCountInPackage(costStr.strValue);
			if (costNum < costStr.intValue) {
				throw new NoteException(Messages.getString("HeroControler.33"));
			}

			this.roleRt.getItemControler().changeItemByTemplateCode(costStr.strValue, -costStr.intValue);
		} else {
			this.roleRt.getItemControler().changeItemByTemplateCode("ydan", -cost);
		}

		byte activedAfter = 0;
		if (TextUtil.isNotBlank(heroId) && TextUtil.isNotBlank(heroCode) && heroId.equals(heroCode)) {
			activedAfter = 1;
		}

		partnerPosResetEvent.onPartnerPositionReset(postion, partnerPropT.id, heroId, cost);
		// 判断重置前后是否有状态变化 影响战力变更
		if (isLock == 1) {// 锁定武将时，若该伙伴位置已激活，则重置属性必定会有战力变更
			if (activedBefore == 1) {
				partnerStateChange.onPartnerStateChange();
				this.partnerChange(xsgPartner);
			}
		} else if (activedAfter != activedBefore) {
			partnerStateChange.onPartnerStateChange();
			this.partnerChange(xsgPartner);
		}
		PartnerPosT partnerT = XsgPartnerManager.getInstance().getPartnerTById(postion);

		PropertyConfig conView = new PropertyConfig(partnerPropT.id, partnerPropT.propName, partnerPropT.color,
				partnerPropT.propPercent);

		return new PartnerViewInfo(heroIdOnPos, heroId, postion, pc.heroPosition.get(postion) != null ? 1 : 0, 1,
				conView, partnerT.position, partnerT.level, partnerT.type, partnerT.amount, partnerT.vipLevel,
				partnerT.lockPay, partnerT.openCondition, costStr.strValue, costStr.intValue);
	}

	@Override
	public void clearAll() throws NoteException {

		IPartner partner = this.getPartner();
		if (partner == null) {
			throw new NoteException(Messages.getString("PartnerController.firstPosition"));
		}
		// 清空伙伴部队配置
		try {
			this.roleRt.getNotifyControler().setAutoNotify(false);
			for (int i = 1; i <= XsgPartnerManager.getInstance().getPosList().size(); i++) {
				IHero hero = partner.getHeroByPos(i);
				if (hero != null) {
					this.roleRt.getHeroControler().removeReference(hero, false, true);
				}
			}

		} catch (NoteException e) {
			LogManager.error(e);
			throw new NoteException(Messages.getString("PartnerController.clearFailed"));
		} finally {
			this.roleRt.getNotifyControler().setAutoNotify(true);
		}
		this.partnerChange(partner);
		this.partnerClearEvent.onPartnerClear(partner);
	}

	@Override
	public void partnerChange(IPartner partner) {
		Map<String, IFormation> map = roleRt.getFormationControler().getFormation();

		for (Entry<String, IFormation> en : map.entrySet()) {

			IFormation value = en.getValue();
			roleRt.getFormationControler().formationChange(value);

		}
	}

	@Override
	public int getRequiredLevel() {

		return XsgPartnerManager.getInstance().getReuiredLevel().level;
	}

	@Override
	public int getOpenedPartner() {

		IPartner partner = this.getPartner();

		return partner == null ? 0 : partner.getPropMap().size();
	}

	@Override
	public int getActivedRelationPartner() {
		// 获取所有武将
		List<IHero> allHero = roleRt.getHeroControler().getAllHero();
		int result = 0;

		IPartner partner = this.getPartner();
		if (partner == null) {
			return result;
		}
		// 获取伙伴上阵武将
		Map<Integer, IHero> heroMap = partner.getHeroMap();
		if (heroMap == null || heroMap.size() <= 0) {
			return result;
		}
		// 获取专属伙伴配置
		Map<Integer, PartnerHeroT> specialHeroMap = partner.getSpecialHeroMap();
		for (int j = 0; j < specialHeroMap.size(); j++) {
			// 获取该位置的伙伴武将 伙伴位置从1开始
			IHero partnerHero = heroMap.get(j + 1);
			if (partnerHero == null) {
				continue;
			}
			// 伙伴武将是否激活特殊伙伴加成 伙伴位置从1开始
			PartnerHeroT partnerHeroT = specialHeroMap.get(j + 1);
			// 未激活则跳过本次循环
			if (partnerHero.getTemplateId() != partnerHeroT.id) {
				continue;
			}

			// 遍历过滤出上阵武将
			for (int i = 0; i < allHero.size(); i++) {
				IHero iHero = allHero.get(i);
				// 如果是上阵武将，则判断该武将缘分是否包含该伙伴武将
				if (iHero.getState() == HeroState.InTheFormation || iHero.getState() == HeroState.InTheSupport) {
					// 获取该上阵武将的所有缘分
					RelationT[] relations = iHero.getTemplate().getRelations();
					boolean heroFlag = false;
					for (RelationT relationT : relations) {
						boolean flag = false;
						for (CellWrap relationObjId : relationT.targetList) {
							// 如果该缘分配置的匹配模版id是该位置的伙伴武将模版id
							if (relationObjId.value.equals(partnerHero.getTemplateId() + "")) {
								result++;
								flag = true;
								break;
							}
							;
						}

						if (flag) {
							heroFlag = true;
							break;
						}
					}
					if (heroFlag) {
						break;
					}
				}

			}
		}
		return result;
	}

	@Override
	public void onHeroEquipChange(IHero hero, EquipItem equip) {

		if (hero != null
				&& (hero.getState() == HeroState.PartnerShip || hero.isAttendant()
						&& hero.getMaster().getState() == HeroState.PartnerShip)) {
			IPartner partner = getPartner();
			if (partner != null) {
				this.partnerChange(partner);
			}
		}

	}

	@Override
	public void onHeroBreakUp(IHero hero, int beforeBreakLevel) {
		if (hero != null
				&& (hero.getState() == HeroState.PartnerShip || hero.isAttendant()
						&& hero.getMaster().getState() == HeroState.PartnerShip)) {
			IPartner partner = getPartner();
			if (partner != null) {
				this.partnerChange(partner);
			}
		}
	}

	@Override
	public void onResetPractice(IHero hero, int index, String oldName, int oldColor, int oldLevel, int oldExp,
			String newName, int newColor) {
		if (hero != null
				&& (hero.getState() == HeroState.PartnerShip || hero.isAttendant()
						&& hero.getMaster().getState() == HeroState.PartnerShip)) {
			IPartner partner = getPartner();
			if (partner != null) {
				this.partnerChange(partner);
			}
		}
	}

	@Override
	public void onHeroPractice(IHero hero, int index, String name, int coloe, int oldLevel, int oldExp, int addExp,
			int newLevel, int newExp, int sumGx) {
		if (hero != null
				&& (hero.getState() == HeroState.PartnerShip || hero.isAttendant()
						&& hero.getMaster().getState() == HeroState.PartnerShip)) {
			IPartner partner = getPartner();
			if (partner != null) {
				this.partnerChange(partner);
			}
		}
	}

	@Override
	public void onAttendantChange(IHero hero, byte pos, IHero attendant) {
		if (hero != null && hero.getState() == HeroState.PartnerShip) {
			IPartner partner = getPartner();
			if (partner != null) {
				this.partnerChange(partner);
			}
		}
	}

	@Override
	public void onHeroSkillUp(IHero hero, String name, int oldLevel, int newLevel) {
		if (hero != null
				&& (hero.getState() == HeroState.PartnerShip || hero.isAttendant()
						&& hero.getMaster().getState() == HeroState.PartnerShip)) {
			IPartner partner = getPartner();
			if (partner != null) {
				this.partnerChange(partner);
			}
		}
	}

	@Override
	public void onHeroStarUp(IHero hero, int beforeStar) {
		if (hero != null
				&& (hero.getState() == HeroState.PartnerShip || hero.isAttendant()
						&& hero.getMaster().getState() == HeroState.PartnerShip)) {
			IPartner partner = getPartner();
			if (partner != null) {
				this.partnerChange(partner);
			}
		}
	}

	@Override
	public void onHeroQualityUp(IHero hero, int beforeQualityLeve) {
		if (hero != null
				&& (hero.getState() == HeroState.PartnerShip || hero.isAttendant()
						&& hero.getMaster().getState() == HeroState.PartnerShip)) {
			IPartner partner = getPartner();
			if (partner != null) {
				this.partnerChange(partner);
			}
		}
	}

}

class PartnerConfig {
	// 位置-属性id
	public Map<Integer, Integer> positionProp;
	// 位置-伙伴武将id
	public Map<Integer, String> heroPosition;
	// 位置-特殊武将模版id
	public Map<Integer, String> specialHeroPosition;
}

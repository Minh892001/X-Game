/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: HeroAwakenController
 * 功能描述：
 * 文件名：HeroAwakenController.java
 **************************************************
 */
package com.morefun.XSanGo.heroAwaken;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.XSanGo.Protocol.BaptizeProp;
import com.XSanGo.Protocol.HeroBaptizeData;
import com.XSanGo.Protocol.HeroBaptizeResetResult;
import com.XSanGo.Protocol.HeroBaptizeResult;
import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleHero;
import com.morefun.XSanGo.db.game.RoleHeroAwaken;
import com.morefun.XSanGo.event.protocol.IHeroAwaken;
import com.morefun.XSanGo.event.protocol.IHeroBaptize;
import com.morefun.XSanGo.event.protocol.IHeroBaptizeReset;
import com.morefun.XSanGo.event.protocol.IHeroBaptizeUpgrade;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 武将觉醒实现
 * 
 * @author weiyi.zhao
 * @since 2016-4-13
 * @version 1.0
 */
public class HeroAwakenController implements IHeroAwakenController {

	/** 角色接口 */
	private IRole roleRt;

	/** 角色数据对象 */
	private Role roleDB;

	/** 武将觉醒事件 */
	private IHeroAwaken heroAwaken;

	/** 武将洗炼事件 */
	private IHeroBaptize heroBaptize;

	/** 洗炼等级升级事件 */
	private IHeroBaptizeUpgrade heroBaptizeUpgrade;

	/** 洗炼重置事件 */
	private IHeroBaptizeReset heroBaptizeReset;

	/** 洗炼属性当前等级的上限 */
	private Map<String, Map<String, Integer>> baptizePropMaxValue;

	/**
	 * 构造函数
	 * 
	 * @param roleRt
	 * @param roleDB
	 */
	public HeroAwakenController(IRole roleRt, Role roleDB) {
		this.roleRt = roleRt;
		this.roleDB = roleDB;

		// 注册事件
		this.heroAwaken = this.roleRt.getEventControler().registerEvent(IHeroAwaken.class);
		this.heroBaptize = this.roleRt.getEventControler().registerEvent(IHeroBaptize.class);
		this.heroBaptizeUpgrade = this.roleRt.getEventControler().registerEvent(IHeroBaptizeUpgrade.class);
		this.heroBaptizeReset = this.roleRt.getEventControler().registerEvent(IHeroBaptizeReset.class);

		// 初始化数据 防止重复计算
		this.baptizePropMaxValue = new HashMap<String, Map<String, Integer>>();
		for (RoleHero hero : this.roleDB.getRoleHeros()) {
			initHeroBaptizePropLimit(hero.getRoleHeroAwaken());
		}
	}

	/**
	 * 初始化武将洗炼属性上限
	 * 
	 * @param rha 角色洗炼数据对象
	 */
	private void initHeroBaptizePropLimit(RoleHeroAwaken rha) {
		if (rha != null && rha.getLvl() > 0) {
			Map<String, Integer> maxValues = new HashMap<String, Integer>();
			for (HeroBaptizeT t : XsgHeroAwakenManager.getInstance().getHeroBaptizes().values()) {
				int maxValue = XsgHeroAwakenManager.getInstance().calculationPropMaxValue(rha.getLvl(), t.propType);
				maxValues.put(t.propType, maxValue);
			}
			this.baptizePropMaxValue.put(rha.getHero_id(), maxValues);
		}
	}

	/**
	 * 检测是否可以觉醒
	 * 
	 * @param heroId
	 * @throws NoteException
	 * @throws NotEnoughMoneyException
	 * @throws NotEnoughYuanBaoException
	 */
	private void checkHeroAwaken(String heroId) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException {
		IHero hero = roleRt.getHeroControler().getHero(heroId);
		if (hero == null) {
			throw new NoteException(Messages.getString("HeroControler.13"));
		}
		if (hero.getTemplate().isAwaken == 0) {// 不可觉醒
			throw new NoteException(Messages.getString("HeroAwakenController.noCanAwaken"));
		}
		if (hero.getTemplate().awakenHeroLevel > hero.getLevel()) {
			throw new NoteException("HeroAwakenController.levelNoEnough");
		}
		if (hero.isAwaken()) {// 已觉醒，不能进入觉醒界面
			throw new NoteException(Messages.getString("HeroAwakenController.Awaken"));
		}
		HeroAwakenT heroAwakenT = XsgHeroAwakenManager.getInstance().getHeroAwakens(hero.getTemplateId());
		if (heroAwakenT == null) {
			throw new NoteException(Messages.getString("HeroAwakenController.noCanAwaken"));
		}
		// 检测道具条件
		RoleHeroAwaken rha = hero.getRoleHeroAwaken();
		if (rha == null || (rha.getStar() != heroAwakenT.needSteps)) {// 修炼满级，进行觉醒无须道具
			roleRt.getItemControler().isItemNumEnough(heroAwakenT.needItemCode, heroAwakenT.needItemCount);
		}
	}

	@Override
	public int awakenHero(String heroId) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException {
		checkHeroAwaken(heroId);
		IHero hero = roleRt.getHeroControler().getHero(heroId);
		HeroAwakenT heroAwakenT = XsgHeroAwakenManager.getInstance().getHeroAwakens(hero.getTemplateId());
		RoleHeroAwaken rha = hero.getRoleHeroAwaken();
		if (rha == null || (rha.getStar() != heroAwakenT.needSteps)) {// 修炼满级，进行觉醒无须道具
			// 检测并扣除金币
			roleRt.winJinbi(-heroAwakenT.needGold);
			// 扣除道具
			roleRt.getRewardControler().acceptReward(heroAwakenT.needItemCode, -heroAwakenT.needItemCount);
		}
		// 觉醒
		if (rha == null) {
			rha = new RoleHeroAwaken();
			hero.setRoleHeroAwaken(rha);
		}
		if (rha.getStar() == heroAwakenT.needSteps) {// 修炼满级，进行觉醒
			rha.setIs_awaken((byte) 1);
			// 激活觉醒技能（最终觉醒触发）
			if (hero.getTemplate().awakenSkillId > 0) {
				hero.addSkill(hero.getTemplate().awakenSkillId, 1);
			}
		} else {// 修炼
			rha.setStar(rha.getStar() + 1);
		}
		this.roleRt.getNotifyControler().onHeroChanged(hero);
		// 事件触发
		heroAwaken.onHeroAwaken(hero.getTemplateId(), rha.getStar(), rha.getIs_awaken() == 1);
		return rha.getStar();
	}

	/**
	 * 角色武将洗炼属性
	 * 
	 * @param roleHeroAwaken
	 * @return
	 */
	private AwakenMap parseHeroBaptizeProp(RoleHeroAwaken roleHeroAwaken) {
		return XsgHeroAwakenManager.getInstance().parseAwaken(roleHeroAwaken.getBaptizeProps());
	}

	/**
	 * 检测是否可以洗炼
	 * 
	 * @param heroId
	 * @throws NoteException
	 * @throws NotEnoughMoneyException
	 * @throws NotEnoughYuanBaoException
	 */
	private void checkHeroBaptize(String heroId) throws NoteException {
		IHero hero = roleRt.getHeroControler().getHero(heroId);
		if (hero == null) {
			throw new NoteException(Messages.getString("HeroControler.13"));
		}
		if (hero.getTemplate().isAwaken == 0) {// 不可觉醒
			throw new NoteException(Messages.getString("HeroAwakenController.noCanAwaken"));
		}
		if (!hero.isAwaken()) {// 未觉醒，不能开启洗炼
			throw new NoteException(Messages.getString("HeroAwakenController.noAwaken"));
		}
	}

	@Override
	public HeroBaptizeData heroBaptizeShow(String heroId) throws NoteException {
		checkHeroBaptize(heroId);

		IHero hero = roleRt.getHeroControler().getHero(heroId);
		RoleHeroAwaken rha = hero.getRoleHeroAwaken();
		AwakenMap propMaps = parseHeroBaptizeProp(rha);
		return createHeroBaptizeData(rha, propMaps);
	}

	/**
	 * 指定武将洗炼数据对象
	 * 
	 * @param rha 觉醒对象
	 * @param propMaps 洗炼数据
	 * @return
	 */
	private HeroBaptizeData createHeroBaptizeData(RoleHeroAwaken rha, AwakenMap propMaps) {
		HeroBaptizeData hbd = new HeroBaptizeData();
		hbd.lvl = rha == null ? 0 : rha.getLvl();
		Map<String, HeroBaptizeT> heroBaptizes = XsgHeroAwakenManager.getInstance().getHeroBaptizes();
		hbd.props = new BaptizeProp[heroBaptizes.size()];
		int i = 0;
		for (HeroBaptizeT t : heroBaptizes.values()) {
			hbd.props[i] = new BaptizeProp();
			hbd.props[i].code = t.propType;
			hbd.props[i].maxValue = getPropMaxValue(rha, t.propType);
			hbd.props[i].value = propMaps == null ? 0 : propMaps.get(t.propType);
			i++;
		}
		return hbd;
	}

	/**
	 * 获得当前洗炼等级的属性上限值
	 * 
	 * @param rha
	 * @param propType
	 * @return
	 */
	private int getPropMaxValue(RoleHeroAwaken rha, String propType) {
		int baptizeLvl = rha == null ? 0 : rha.getLvl();
		if (rha == null || rha.getLvl() < 1) {
			return XsgHeroAwakenManager.getInstance().calculationPropMaxValue(baptizeLvl, propType);
		}
		Map<String, Integer> maxValues = this.baptizePropMaxValue.get(rha.getHero_id());
		if (maxValues == null) {
			return XsgHeroAwakenManager.getInstance().calculationPropMaxValue(baptizeLvl, propType);
		}
		return maxValues.get(propType);
	}

	/**
	 * 计算本次的洗炼数值
	 * 
	 * @param rha
	 * @param propType
	 * @return
	 */
	private int calculationPropValue(RoleHeroAwaken rha, String propType) {
		HeroBaptizeT hbt = XsgHeroAwakenManager.getInstance().getHeroBaptizeT(propType);
		if (hbt == null) {
			return 0;
		}
		return NumberUtil.randomContain(hbt.rangeOnceMin, hbt.rangeOnceMax);
	}

	@Override
	public HeroBaptizeResult heroBaptize(String heroId, boolean isTenTimes) throws NotEnoughMoneyException, NotEnoughYuanBaoException, NoteException {
		checkHeroBaptize(heroId);
		// 循环次数
		int times = isTenTimes ? 10 : 1;
		// 循环进行洗炼
		int resultTimes = calculationHeroBaptize(heroId, times);
		// 计算属性
		IHero hero = roleRt.getHeroControler().getHero(heroId);
		if (resultTimes > 0) {
			this.roleRt.getNotifyControler().onHeroChanged(hero);
		}

		RoleHeroAwaken rha = hero.getRoleHeroAwaken();
		AwakenMap propMaps = parseHeroBaptizeProp(rha);
		HeroBaptizeData baptizeData = createHeroBaptizeData(rha, propMaps);
		return new HeroBaptizeResult(resultTimes, baptizeData);
	}

	/**
	 * 计算洗炼
	 * 
	 * @param heroId
	 * @param times 洗炼次数
	 * @return
	 * @throws NotEnoughMoneyException
	 * @throws NotEnoughYuanBaoException
	 * @throws NoteException
	 */
	private int calculationHeroBaptize(String heroId, int times) throws NotEnoughMoneyException, NotEnoughYuanBaoException, NoteException {
		IHero hero = roleRt.getHeroControler().getHero(heroId);
		RoleHeroAwaken rha = hero.getRoleHeroAwaken();
		AwakenMap propMaps = parseHeroBaptizeProp(rha);
		if (propMaps == null) {
			propMaps = new AwakenMap();
		}
		HeroBaptizeConfigT config = XsgHeroAwakenManager.getInstance().getHeroBaptizeConfigs(rha.getLvl());
		Map<String, HeroBaptizeT> heroBaptizes = XsgHeroAwakenManager.getInstance().getHeroBaptizes();
		AwakenMap consumes = XsgHeroAwakenManager.getInstance().parseAwaken(rha.getBaptizeConsume());
		consumes = consumes == null ? new AwakenMap() : consumes;
		AwakenMap logProps = new AwakenMap();
		// 循环计算洗炼结果
		int resultTimes = 0;
		for (; resultTimes < times; resultTimes++) {
			// 洗炼材料是否足够
			boolean isSuccess = true;
			Map<String, Integer> costItems = new HashMap<String, Integer>();
			for (String items : StringUtils.split(config.expendOnce, ";")) {
				String[] item = StringUtils.split(items, ":");
				if (resultTimes == 0) {// 异常会直接抛出去处理
					roleRt.getItemControler().isItemNumEnough(item[0], Integer.parseInt(item[1]));
				} else {// 道具不足中断循环再返回
					try {
						roleRt.getItemControler().isItemNumEnough(item[0], Integer.parseInt(item[1]));
					} catch (Exception e) {
						isSuccess = false;
						break;
					}
				}
				costItems.put(item[0], Integer.parseInt(item[1]));
			}
			if (!isSuccess) {// 道具不足，跳出
				break;
			}
			// 验证属性是否全部已达上限
			boolean isLimit = true;
			for (String propType : heroBaptizes.keySet()) {
				int maxValue = getPropMaxValue(rha, propType);
				int curValue = propMaps.get(propType);
				if (maxValue > curValue) {
					isLimit = false;
					int value = calculationPropValue(rha, propType);
					curValue += value;
					curValue = curValue > maxValue ? maxValue : curValue;
					propMaps.put(propType, curValue);

					logProps.combine(propType, value);
				}
			}
			if (isLimit) {
				if (resultTimes == 0) {
					throw new NoteException(Messages.getString("HeroAwakenController.baptizeMaxValue"));
				}
				break;
			}
			// 扣除道具
			for (String itemCode : costItems.keySet()) {
				roleRt.getRewardControler().acceptReward(itemCode, -costItems.get(itemCode));
				if (itemCode.equals(XsgGameParamManager.getInstance().getHeroAwakenItemRebate())) {
					consumes.combine(itemCode, costItems.get(itemCode));
				}
			}
		}
		rha.setBaptizeProps(TextUtil.GSON.toJson(propMaps));
		rha.setBaptizeConsume(TextUtil.GSON.toJson(consumes));
		// 事件触发
		this.heroBaptize.onHeroBaptize(heroId, TextUtil.GSON.toJson(logProps), resultTimes);
		return resultTimes;
	}

	@Override
	public HeroBaptizeData baptizeUpgrade(String heroId) throws NotEnoughMoneyException, NotEnoughYuanBaoException, NoteException {
		checkHeroBaptize(heroId);

		IHero hero = roleRt.getHeroControler().getHero(heroId);
		RoleHeroAwaken rha = hero.getRoleHeroAwaken();
		AwakenMap propMaps = parseHeroBaptizeProp(rha);
		AwakenMap consumes = XsgHeroAwakenManager.getInstance().parseAwaken(rha.getBaptizeConsume());
		consumes = consumes == null ? new AwakenMap() : consumes;

		// 验证当前洗炼等级的属性是否已满
		if (propMaps == null) {
			throw new NoteException(Messages.getString("HeroAwakenController.noBaptizeUpgrade"));
		}
		for (String propType : propMaps.keySet()) {
			int maxValue = getPropMaxValue(rha, propType);
			if (maxValue > propMaps.get(propType)) {
				throw new NoteException(Messages.getString("HeroAwakenController.noBaptizeUpgrade"));
			}
		}
		if (rha.getLvl() >= XsgHeroAwakenManager.getInstance().getMaxBaptizeLvl()) {
			throw new NoteException(Messages.getString("HeroAwakenController.baptizeMaxLvl"));
		}
		// 是否洗炼上限
		HeroBaptizeConfigT config = XsgHeroAwakenManager.getInstance().getHeroBaptizeConfigs(rha.getLvl());
		if (config == null || TextUtil.isBlank(config.levelUpExpend)) {
			throw new NoteException(Messages.getString("HeroAwakenController.baptizeMaxLvl"));
		}
		// 材料是否充足
		Map<String, Integer> costItems = new HashMap<String, Integer>();
		for (String items : StringUtils.split(config.levelUpExpend, ";")) {
			String[] item = StringUtils.split(items, ":");
			roleRt.getItemControler().isItemNumEnough(item[0], Integer.parseInt(item[1]));
			costItems.put(item[0], Integer.parseInt(item[1]));
		}
		// 扣除道具
		for (String itemCode : costItems.keySet()) {
			roleRt.getRewardControler().acceptReward(itemCode, -costItems.get(itemCode));
			if (itemCode.equals(XsgGameParamManager.getInstance().getHeroAwakenItemRebate())) {
				consumes.combine(itemCode, costItems.get(itemCode));
			}
		}
		rha.setLvl(rha.getLvl() + 1);
		rha.setBaptizeConsume(TextUtil.GSON.toJson(consumes));
		// 缓存洗炼等级提升各属性上限变化
		initHeroBaptizePropLimit(rha);
		// 查看是否存在属性激活
		HeroBaptizeAddPropT addPropT = XsgHeroAwakenManager.getInstance().getHeroBaptizeAddProps(rha.getLvl());
		if (addPropT != null) {
			this.roleRt.getNotifyControler().onHeroChanged(hero);
		}
		// 事件处理
		this.heroBaptizeUpgrade.onBaptizeUpgrade(heroId, rha.getLvl());
		return createHeroBaptizeData(rha, propMaps);
	}

	@Override
	public HeroBaptizeResetResult baptizeReset(String heroId) throws NoteException {
		checkHeroBaptize(heroId);
		IHero hero = roleRt.getHeroControler().getHero(heroId);
		RoleHeroAwaken rha = hero.getRoleHeroAwaken();
		if (rha.getLvl() == 0 && TextUtil.isBlank(rha.getBaptizeProps())) {
			throw new NoteException(Messages.getString("HeroAwakenController.noCanReset"));
		}
		// 返还道具
		IntString[] items = null;
		AwakenMap consumes = XsgHeroAwakenManager.getInstance().parseAwaken(rha.getBaptizeConsume());
		if (consumes != null) {
			for (String itemCode : consumes.keySet()) {
				int itemNum = consumes.get(itemCode);
				itemNum = itemNum * XsgGameParamManager.getInstance().getHeroAwakenBaptizeRebate() / 10000;
				roleRt.getRewardControler().acceptReward(itemCode, itemNum);
				items = (IntString[]) ArrayUtils.add(items, new IntString(itemNum, itemCode));
			}
		}

		int baptizeLvl = rha.getLvl();
		AwakenMap baptizeProp = parseHeroBaptizeProp(rha);
		// 重置洗炼数据
		rha.setLvl(0);
		rha.setBaptizeConsume(null);
		rha.setBaptizeProps(null);

		// 刷新武将属性
		this.roleRt.getNotifyControler().onHeroChanged(hero);

		// 触发事件
		heroBaptizeReset.onReset(heroId, baptizeLvl, TextUtil.GSON.toJson(baptizeProp), TextUtil.GSON.toJson(items));
		baptizeProp = parseHeroBaptizeProp(rha);
		return new HeroBaptizeResetResult(createHeroBaptizeData(rha, baptizeProp), items);
	}
}

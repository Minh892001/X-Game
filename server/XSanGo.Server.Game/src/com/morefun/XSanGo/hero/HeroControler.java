/**
 * 
 */
package com.morefun.XSanGo.hero;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.XSanGo.Protocol.AttendantView;
import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.EquipPosition;
import com.XSanGo.Protocol.HeroEquipView;
import com.XSanGo.Protocol.HeroEquips;
import com.XSanGo.Protocol.HeroPracticeView;
import com.XSanGo.Protocol.HeroView;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.Money;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.OthersHeroView;
import com.XSanGo.Protocol.QualityColor;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.HeroSource;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.db.game.HeroPractice;
import com.morefun.XSanGo.db.game.HeroSkill;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleHero;
import com.morefun.XSanGo.equip.EquipItem;
import com.morefun.XSanGo.event.IEventControler;
import com.morefun.XSanGo.event.protocol.IAttendantChange;
import com.morefun.XSanGo.event.protocol.IAttendantReset;
import com.morefun.XSanGo.event.protocol.IEquipRebuild;
import com.morefun.XSanGo.event.protocol.IEquipStarUp;
import com.morefun.XSanGo.event.protocol.IEquipStrengthen;
import com.morefun.XSanGo.event.protocol.IHeroBreakUp;
import com.morefun.XSanGo.event.protocol.IHeroEquipChange;
import com.morefun.XSanGo.event.protocol.IHeroJoin;
import com.morefun.XSanGo.event.protocol.IHeroPractice;
import com.morefun.XSanGo.event.protocol.IHeroQualityUp;
import com.morefun.XSanGo.event.protocol.IHeroRelationChange;
import com.morefun.XSanGo.event.protocol.IHeroSkillUp;
import com.morefun.XSanGo.event.protocol.IHeroStarUp;
import com.morefun.XSanGo.event.protocol.IResetPractice;
import com.morefun.XSanGo.item.AbsItemT;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.reward.XsgRewardManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.CollectionUtil;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.IPredicate;
import com.morefun.XSanGo.util.IRandomHitable;
import com.morefun.XSanGo.util.IRecoverable;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.RandomRange;
import com.morefun.XSanGo.util.RecoveryUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 武将系统控制器
 * 
 * @author sulingyun
 * 
 */
public class HeroControler implements IHeroControler, IEquipStrengthen, IEquipStarUp, IEquipRebuild {
	/** 功勋编号 */
	public static final String MED3 = "med3";

	public static final String DrugForBreak = "tdan";

	/**
	 * 修炼丹编号
	 */
	public static final String XDAN = XsgGameParamManager.getInstance().getXiuLianCode();

	private IRole roleRt;

	private Role roleDb;

	private Map<String, IHero> heroMap;

	private IHeroStarUp heroStarUpEvent;

	private IHeroSkillUp heroSkillUpEvent;

	private IHeroQualityUp heroColorUpEvent;

	private IAttendantChange attendantChangeEvent;

	private IHeroEquipChange heroEquipChangeEvent;

	private IHeroJoin heroJoinEvent;

	// 修炼事件
	private IHeroPractice heroPracticeEvent;

	// 重置修炼事件
	private IResetPractice resetPracticeEvent;

	private IHeroBreakUp heroBreakUpEvent;

	private IHeroRelationChange heroRelationChangeEvent;

	private IAttendantReset resetAttendantEvent;

	public HeroControler(IRole rt, Role db) {
		this.roleRt = rt;
		this.roleDb = db;
		this.heroMap = new HashMap<String, IHero>();
		for (RoleHero hero : this.roleDb.getRoleHeros()) {
			this.heroMap.put(hero.getId(), new XsgHero(this.roleRt, hero));
		}

		IEventControler eventControler = this.roleRt.getEventControler();
		this.heroJoinEvent = eventControler.registerEvent(IHeroJoin.class);
		this.heroStarUpEvent = eventControler.registerEvent(IHeroStarUp.class);
		this.heroSkillUpEvent = eventControler.registerEvent(IHeroSkillUp.class);
		this.heroColorUpEvent = eventControler.registerEvent(IHeroQualityUp.class);
		this.attendantChangeEvent = eventControler.registerEvent(IAttendantChange.class);
		this.heroEquipChangeEvent = eventControler.registerEvent(IHeroEquipChange.class);
		this.heroBreakUpEvent = eventControler.registerEvent(IHeroBreakUp.class);
		this.heroRelationChangeEvent = eventControler.registerEvent(IHeroRelationChange.class);

		eventControler.registerHandler(IEquipStrengthen.class, this);
		eventControler.registerHandler(IEquipStarUp.class, this);
		eventControler.registerHandler(IEquipRebuild.class, this);
		this.heroPracticeEvent = eventControler.registerEvent(IHeroPractice.class);
		this.resetPracticeEvent = eventControler.registerEvent(IResetPractice.class);
		this.resetAttendantEvent = eventControler.registerEvent(IAttendantReset.class);
	}

	@Override
	public void initHeroAttendant() {
		for (IHero hero : this.heroMap.values()) {
			hero.initAttendant();
		}
	}

	@Override
	public void checkData() {
		EquipPosition[] positions = EquipPosition.values();
		for (IHero hero : this.heroMap.values()) {
			String heroId = hero.getId();
			// 检查是否同时被部队和主人引用
			PositionData pd = hero.getHeroPositionData();
			if (pd != null) {
				if (!TextUtil.isBlank(pd.formationId) && !TextUtil.isBlank(pd.heroId)) {
					LogManager.warn(TextUtil.format("[{0}]的[{1}]同时被部队和主人引用！！！", //$NON-NLS-1$
							this.roleRt.getName(), hero.getName()));
				}
				// 设置了主人, 但是主人不存在
				if (!TextUtil.isBlank(pd.heroId) && getHero(pd.heroId) == null) {
					LogManager.warn(TextUtil.format("[{0}]的[{1}]的主人没找到！！！", this.roleRt.getName(), hero.getName()));
					hero.setHeroPositionData(null);
				}
			}

			// 检查随从
			for (byte i = 0; i < Const.Role.Max_Attendant_Count; i++) {
				IHero attendant = hero.getAttendantByPos(i);
				if (attendant != null) {
					PositionData attPD = attendant.getHeroPositionData();
					if (attPD != null) {
						if (!heroId.equals(attPD.heroId)) {
							hero.setAttendant(i, null);
						}
					} else {
						hero.setAttendant(i, null);
					}
				}
			}

			// 检查装备
			for (EquipPosition pos : positions) {
				EquipItem equip = hero.getEquipByPos(pos);
				if (equip == null) {
					continue;
				}

				if (!heroId.equals(equip.getRefereneHero())) {
					LogManager.warn(TextUtil.format("[{0}]的[{1}]身上的装备[{2}]数据不匹配！！！", //$NON-NLS-1$
							this.roleRt.getName(), hero.getName(), equip.getId()));
				}
			}
		}
	}

	@Override
	public HeroView[] getHeroViewList() {
		List<HeroView> viewList = new ArrayList<HeroView>();
		for (IHero hero : this.heroMap.values()) {
			viewList.add(hero.getHeroView());
		}

		return viewList.toArray(new HeroView[0]);
	}

	@Override
	public List<HeroEquipView> getEquipDetails() {
		List<HeroEquipView> viewList = new ArrayList<HeroEquipView>();
		for (IHero hero : this.heroMap.values()) {
			viewList.addAll(hero.getEquipDetails());
		}
		return viewList;
	}

	@Override
	public IHero getHero(String heroId) {
		return this.heroMap.get(heroId);
	}

	@Override
	public List<IHero> getAllHero() {
		return new ArrayList<IHero>(heroMap.values());
	}

	@Override
	public void setHeroEquip(String heroId, EquipItem equip) throws NoteException {
		IHero hero = this.getHero(heroId);
		if (hero == null) {
			throw new NoteException(Messages.getString("HeroControler.3")); //$NON-NLS-1$
		}

		if (!hero.canEquip(equip)) {
			throw new NoteException(Messages.getString("HeroControler.4")); //$NON-NLS-1$
		}

		// 武将原来位置的装备
		EquipItem oldEquip = hero.getEquipByPos(equip.getEquipPos());
		if (oldEquip != null) {
			hero.setHeroEquip(oldEquip.getEquipPos(), null);
			oldEquip.setRefereneHero(null);
		}

		// 装备原先的使用者
		if (!TextUtil.isBlank(equip.getRefereneHero())) {
			this.getHero(equip.getRefereneHero()).setHeroEquip(equip.getEquipPos(), null);
			this.roleRt.getNotifyControler().onHeroChanged(this.getHero(equip.getRefereneHero()));
		}

		hero.setHeroEquip(equip.getEquipPos(), equip);
		equip.setRefereneHero(hero);
		this.roleRt.getNotifyControler().onHeroChanged(hero);
		this.heroEquipChangeEvent.onHeroEquipChange(hero, equip);
	}

	@Override
	public void removeHeroEquip(String heroId, EquipItem equip) throws NoteException {
		IHero hero = this.getHero(heroId);
		if (hero == null) {
			throw new NoteException(Messages.getString("HeroControler.5")); //$NON-NLS-1$
		}

		// 武将对应位置的装备
		EquipPosition position = equip.getEquipPos();
		EquipItem equipItem = hero.getEquipByPos(position);
		if (equipItem == null) {
			throw new NoteException(Messages.getString("HeroControler.6")); //$NON-NLS-1$
		}
		if (!equipItem.getId().equals(equip.getId())) {
			throw new NoteException(Messages.getString("HeroControler.7")); //$NON-NLS-1$
		}

		// 卸载装备
		hero.setHeroEquip(position, null);
		equipItem.setRefereneHero(null);

		this.roleRt.getNotifyControler().onHeroChanged(hero);
		this.heroEquipChangeEvent.onHeroEquipChange(hero, equipItem);
	}

	@Override
	public void removeHeroEquipNotNotify(String heroId, EquipItem equip) throws NoteException {
		IHero hero = this.getHero(heroId);
		if (hero == null) {
			throw new NoteException(Messages.getString("HeroControler.8")); //$NON-NLS-1$
		}

		// 武将对应位置的装备
		EquipPosition position = equip.getEquipPos();
		EquipItem equipItem = hero.getEquipByPos(position);
		if (equipItem == null) {
			throw new NoteException(Messages.getString("HeroControler.9")); //$NON-NLS-1$
		}
		if (!equipItem.getId().equals(equip.getId())) {
			throw new NoteException(Messages.getString("HeroControler.10")); //$NON-NLS-1$
		}

		// 卸载装备
		hero.setHeroEquip(position, null);
		equipItem.setRefereneHero(null);

		heroEquipChangeEvent.onHeroEquipChange(hero, equipItem);
	}

	@Override
	public void setHeroAttendant(String heroId, byte pos, IHero attendant) throws NoteException {
		IHero hero = this.getHero(heroId);
		if (hero == null) {
			throw new NoteException(Messages.getString("HeroControler.11")); //$NON-NLS-1$
		}

		// 绝不允许设置自己作为自己的随从
		if (hero.equals(attendant)) {
			throw new NoteException(Messages.getString("HeroControler.12")); //$NON-NLS-1$
		}

		// 自己是别人的随从就不能再带随从了
		if (hero.isAttendant()) {
			throw new NoteException(Messages.getString("HeroControler.13")); //$NON-NLS-1$
		}

		// 绝不允许带着随从的武将被设置为其他武将的随从，防止循环引用
		if (attendant != null && attendant.hasAttendant()) {
			throw new NoteException(Messages.getString("HeroControler.14")); //$NON-NLS-1$
		}

		// 无论是设置新的随从还是取消老的随从，都是先把该位置的武将的位置数据清空，然后再做操作
		IHero oldAttendant = hero.getAttendantByPos(pos);
		if (oldAttendant != null) {
			this.removeReference(oldAttendant, true, false);
		}

		if (attendant != null) {
			// 拉了个新武将上来且这个武将之前在阵型里或者是其他人的随从，则需要清理数据
			removeReference(attendant, true, true);

			PositionData pd = new PositionData();
			pd.heroId = heroId;
			pd.position = pos;
			attendant.setHeroPositionData(pd);
			this.roleRt.getNotifyControler().onHeroChanged(attendant);
		}

		hero.setAttendant(pos, attendant);
		this.roleRt.getNotifyControler().onHeroChanged(hero);

		this.attendantChangeEvent.onAttendantChange(hero, pos, attendant);
	}

	@Override
	public void removeReference(IHero hero, boolean checkLastHero, boolean removeFromPosition) throws NoteException {
		if (removeFromPosition && hero.getHeroPositionData() != null) {
			String formationId = hero.getHeroPositionData().formationId;
			String oldMasterId = hero.getHeroPositionData().heroId;
			int oldPos = hero.getHeroPositionData().position;
			String partnerId = hero.getHeroPositionData().partnerId;
			if (!TextUtil.isBlank(formationId)) {
				this.roleRt.getFormationControler().setFormationPosition(formationId, (byte) oldPos, null,
						checkLastHero);
			}

			if (!TextUtil.isBlank(oldMasterId)) {
				IHero oldMaster = this.getHero(oldMasterId);
				if (oldMaster != null) {
					oldMaster.setAttendant((byte) oldPos, null);
					this.roleRt.getNotifyControler().onHeroChanged(oldMaster);
				}
			}

			if (!TextUtil.isBlank(partnerId)) {
				this.roleRt.getPartnerControler().setPartnerPosition((byte) oldPos, null, null, checkLastHero);
			}

		}

		hero.setHeroPositionData(null);
		this.roleRt.getNotifyControler().onHeroChanged(hero);
	}

	private void onEquipPropertyChange(EquipItem equip) {
		// 如果变更装备穿在武将身上，则需要更新武将数据
		if (TextUtil.isBlank(equip.getRefereneHero())) {
			return;
		}

		this.roleRt.getNotifyControler().onHeroChanged(this.getHero(equip.getRefereneHero()));
	}

	@Override
	public void starUp(String heroId) throws NoteException, NotEnoughMoneyException {
		IHero hero = this.getHero(heroId);
		if (hero == null) {
			throw new NoteException(Messages.getString("HeroControler.15")); //$NON-NLS-1$
		}

		if (hero.getStar() >= Const.MaxStar) {
			throw new NoteException(Messages.getString("HeroControler.16")); //$NON-NLS-1$
		}

		HeroStarT starT = XsgHeroManager.getInstance().findHeroStarT(hero.getColor().ordinal(), hero.getStar() + 1);
		String soulId = XsgHeroManager.getInstance().getSoulTemplateId(hero.getTemplateId());
		if (this.roleRt.getItemControler().getItemCountInPackage(soulId) < starT.soulCount) {
			throw new NoteException(Messages.getString("HeroControler.17")); //$NON-NLS-1$
		}
		if (this.roleRt.getJinbi() < starT.jinbi) {
			throw new NotEnoughMoneyException();
		}

		int beforeStar = hero.getStar();
		this.roleRt.getItemControler().changeItemByTemplateCode(soulId, -starT.soulCount);
		this.roleRt.winJinbi(-starT.jinbi);
		hero.starUp();
		this.roleRt.getNotifyControler().onHeroChanged(hero);

		try {
			this.heroStarUpEvent.onHeroStarUp(hero, beforeStar);
		} catch (Exception e) {
			LogManager.error(e);
		}
	}

	@Override
	public ItemView[] colorUp(String heroId) throws NoteException, NotEnoughMoneyException {
		IHero hero = this.getHero(heroId);
		if (hero == null) {
			throw new NoteException(Messages.getString("HeroControler.18")); //$NON-NLS-1$
		}

		int beforeQualityLevel = hero.getQualityLevel();
		if (beforeQualityLevel == Const.MaxQualityLevel) {// 进阶达到上限后执行突破逻辑
			return this.breakHero(hero);
		}

		int nextLevel = beforeQualityLevel + 1;
		HeroQualityT nextColorT = XsgHeroManager.getInstance().findHeroColorT(hero.getTemplateId(), nextLevel);
		if (nextColorT == null) {
			throw new NoteException(Messages.getString("HeroControler.19")); //$NON-NLS-1$
		}

		HeroQualityProduceConsumeT pct = XsgHeroManager.getInstance().findQualityLevelUpT(nextLevel);
		if (hero.getLevel() < pct.level) {
			throw new NoteException(TextUtil.format(Messages.getString("HeroControler.20"), //$NON-NLS-1$
					pct.level));
		}

		// 检查功勋
		if (this.roleRt.getItemControler().getItemCountInPackage(MED3) < pct.med1Count) {
			throw new NoteException(Messages.getString("HeroControler.21")); //$NON-NLS-1$
		}
		if (this.roleRt.getJinbi() < pct.jinbi) {
			throw new NotEnoughMoneyException();
		}

		this.roleRt.getItemControler().changeItemByTemplateCode(MED3, -pct.med1Count);
		this.roleRt.winJinbi(-pct.jinbi);

		ItemView[] items = XsgRewardManager.getInstance().doTcToItem(this.roleRt, pct.tc);
		this.roleRt.getRewardControler().acceptReward(items);
		hero.colorUp();

		this.roleRt.getNotifyControler().onHeroChanged(hero);

		try {
			this.heroColorUpEvent.onHeroQualityUp(hero, beforeQualityLevel);
		} catch (Exception e) {
			LogManager.error(e);
		}

		return items;
	}

	/**
	 * 武将突破
	 * 
	 * @param hero
	 * @return
	 * @throws NoteException
	 * @throws NotEnoughMoneyException
	 */
	private ItemView[] breakHero(IHero hero) throws NoteException, NotEnoughMoneyException {
		int currentLvl = hero.getBreakLevel();
		if (currentLvl == Const.MaxBreakLevel) {
			throw new NoteException(Messages.getString("HeroControler.BreakLevelLimit")); //$NON-NLS-1$
		}

		int nextLevel = currentLvl + 1;
		HeroBreakConsumeT hbt = XsgHeroManager.getInstance().findBreakLevelUpT(nextLevel);
		if (hero.getLevel() < hbt.heroLevel) {
			throw new NoteException(TextUtil.format(Messages.getString("HeroControler.NotEnoughHeroLevelForBreak"), //$NON-NLS-1$
					hbt.heroLevel));
		}

		// 检查功勋，突破丹，金币
		if (this.roleRt.getItemControler().getItemCountInPackage(MED3) < hbt.med3Count) {
			throw new NoteException(Messages.getString("HeroControler.21")); //$NON-NLS-1$
		}
		if (this.roleRt.getItemControler().getItemCountInPackage(DrugForBreak) < hbt.drugCount) {
			throw new NoteException(Messages.getString("HeroControler.NotEnoughItemForBreak")); //$NON-NLS-1$
		}
		if (this.roleRt.getJinbi() < hbt.jinbi) {
			throw new NotEnoughMoneyException();
		}

		this.roleRt.getItemControler().changeItemByTemplateCode(MED3, -hbt.med3Count);
		this.roleRt.getItemControler().changeItemByTemplateCode(DrugForBreak, -hbt.drugCount);
		this.roleRt.winJinbi(-hbt.jinbi);

		hero.breakLevelUp();

		this.roleRt.getNotifyControler().onHeroChanged(hero);

		this.heroBreakUpEvent.onHeroBreakUp(hero, currentLvl);

		return new ItemView[0];
	}

	@Override
	public IHero getHero(final int templateId) {
		return CollectionUtil.first(this.heroMap.values(), new IPredicate<IHero>() {
			@Override
			public boolean check(IHero item) {
				return item.getTemplateId() == templateId;
			}
		});
	}

	@Override
	public IHero addHero(HeroT heroT, HeroSource source) {
		IHero hero = this.getHero(heroT.id);
		if (hero == null) {
			RoleHero rh = new RoleHero(GlobalDataManager.getInstance().generatePrimaryKey(), this.roleDb, heroT.id,
					(byte) QualityColor.Silver.ordinal(), heroT.star);

			this.roleDb.getRoleHeros().add(rh);

			hero = new XsgHero(this.roleRt, rh);
			hero.initSkill();
			hero.initAttendant();
			this.heroMap.put(hero.getId(), hero);
			this.roleRt.getNotifyControler().onHeroChanged(hero);
			this.heroJoinEvent.onHeroJoin(hero, source);
		}

		return hero;
	}

	private RoleHero getRoleHeroById(Set<RoleHero> set, String heroId) {
		if (set != null) {
			for (RoleHero hero : set) {
				if (hero.getId().equals(heroId)) {
					return hero;
				}
			}
		}
		return null;
	}

	@Override
	public void removeHero(String heroId) {
		IHero hero = getHero(heroId);
		if (hero != null) {
			// 移除内存对象
			heroMap.remove(hero.getId());
			// 移除数据库对象
			Set<RoleHero> heros = roleDb.getRoleHeros();
			roleDb.getRoleHeros().remove(getRoleHeroById(heros, heroId));
		}
	}

	@Override
	public void winHeroExp(Map<String, Integer> heroExpMap) {
		if (heroExpMap == null) {
			return;
		}
		// 结算武将经验
		for (String heroId : heroExpMap.keySet()) {
			this.getHero(heroId).winExp(heroExpMap.get(heroId));
		}
	}

	@Override
	public void skillLevelUp(String heroId, int skillId, int upLevel) throws NotEnoughMoneyException, NoteException {
		if (upLevel < 1) {
			throw new NoteException(Messages.getString("HeroControler.28")); //$NON-NLS-1$
		}
		IHero hero = this.getHero(heroId);
		if (hero == null) {
			throw new NoteException(Messages.getString("HeroControler.HeroNotExists")); //$NON-NLS-1$
		}

		// 针对武将的觉醒技能验证
		if (hero.getTemplate().awakenSkillId == skillId && !hero.isAwaken()) {// 当前升级技能为武将觉醒技能，但武将未觉醒
			throw new NoteException(Messages.getString("HeroControler.skill_noAwaken"));
		}

		HeroSkill hs = hero.getHeroSkill(skillId);
		if (hs == null) {
			throw new NoteException(skillId + Messages.getString("HeroControler.23")); //$NON-NLS-1$
		}
		int index = hero.getTemplate().getSkillIndex(skillId);
		if (index < 0) {
			throw new NoteException(skillId + Messages.getString("HeroControler.24")); //$NON-NLS-1$
		}

		this.refreshHeroSkillData();
		if (this.roleDb.getHeroSkillPoint() < upLevel) {
			throw new NoteException(Messages.getString("HeroControler.25")); //$NON-NLS-1$
		}

		HeroSkillT hst = XsgHeroManager.getInstance().findHeroSKillT(skillId);
		if (hst.studyLevel > hero.getLevel()) {
			throw new NoteException(Messages.getString("HeroControler.26")); //$NON-NLS-1$
		}

		// 等级，金币判断
		int newLevel = hs.getLevel() + upLevel;
		int totalMoney = 0;
		for (int i = hs.getLevel() + 1; i <= newLevel; i++) {
			HeroSkillLevelupT hslt = XsgHeroManager.getInstance().getHeroSkillLevelupT(i);
			if (hslt.conditions[index].needHeroLevel > hero.getLevel()) {
				throw new NoteException(Messages.getString("HeroControler.27")); //$NON-NLS-1$
			}
			totalMoney += hslt.conditions[index].jinbi;
		}

		try {
			this.roleRt.reduceCurrency(new Money(CurrencyType.Jinbi, totalMoney));
		} catch (NotEnoughYuanBaoException e) {
			LogManager.error(e);
		}
		this.roleDb.setHeroSkillPoint(this.roleDb.getHeroSkillPoint() - upLevel);
		if (this.roleDb.getNextHeroSkillTime() == null
				&& this.roleDb.getHeroSkillPoint() < this.roleRt.getVipController().getMaxSkillPoint()) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.SECOND, XsgGameParamManager.getInstance().getSkillPointInterval());
			this.roleDb.setNextHeroSkillTime(c.getTime());
		}

		hs.setLevel(newLevel);
		this.roleRt.getNotifyControler().onHeroChanged(hero);

		this.heroSkillUpEvent.onHeroSkillUp(hero, hst.name, newLevel - upLevel, newLevel);
	}

	@Override
	public void onEquipRebuild(EquipItem equip) {
		this.onEquipPropertyChange(equip);
	}

	@Override
	public void onEquipStarUp(EquipItem equip, int uplevel, List<EquipItem> deleteList, int money, int addExp,
			Map<String, Integer> consumeStars) {
		if (uplevel > 0) {
			this.onEquipPropertyChange(equip);
		}
	}

	@Override
	public void onEquipStrengthen(int auto, EquipItem equip, int beforeLevel, int afterLevel) {
		this.onEquipPropertyChange(equip);
	}

	@Override
	public int getHeroNum() {
		return this.heroMap.size();
	}

	@Override
	public int getHeroNumWhenStarGreaterThan(int star) {
		int count = 0;
		for (IHero hero : this.heroMap.values()) {
			if (hero.getStar() >= star) {
				count++;
			}
		}

		return count;
	}

	@Override
	public IHero anyDuelHero() {
		List<IHero> list = new ArrayList<IHero>();
		for (IHero hero : this.heroMap.values()) {
			if (hero.getTemplate().canDuel()) {
				list.add(hero);
			}
		}

		return list.get(NumberUtil.random(list.size()));
	}

	@Override
	public void refreshHeroSkillData() {
		IRecoverable recovery = new IRecoverable() {
			@Override
			public Date getNextRecTime() {
				return roleDb.getNextHeroSkillTime();
			}

			@Override
			public void setTime(Date time) {
				roleDb.setNextHeroSkillTime(time);
			}

			@Override
			public int getValue() {
				return roleDb.getHeroSkillPoint();
			}

			@Override
			public int getLimit() {
				return roleRt.getVipController().getMaxSkillPoint();
			}

			@Override
			public void changeValue(int change) {
				roleDb.setHeroSkillPoint(this.getValue() + change);
			}

			@Override
			public long getInterval() {
				return XsgGameParamManager.getInstance().getSkillPointInterval() * 1000;
			}
		};

		RecoveryUtil.recovery(recovery);

		// int maxPoint = this.roleRt.getVipController().getMaxSkillPoint();
		//
		// // 下次恢复时间为空表示原来是满的，由于配置或者VIP提升等原因上限提高了之后重新计算
		// if (this.roleDb.getNextHeroSkillTime() == null
		// && maxPoint > this.roleDb.getHeroSkillPoint()) {
		// long next = System.currentTimeMillis()
		// + XsgGameParamManager.getInstance().getSkillPointInterval()
		// * 1000;
		// this.roleDb.setNextHeroSkillTime(new Date(next));
		// }
		//
		// long now = System.currentTimeMillis();
		// while (this.roleDb.getNextHeroSkillTime() != null
		// && this.roleDb.getNextHeroSkillTime().getTime() < now
		// && this.roleDb.getHeroSkillPoint() < maxPoint) {
		// int value = this.roleDb.getHeroSkillPoint() + 1;
		// this.roleDb.setHeroSkillPoint(value);
		// if (value >= maxPoint) {
		// this.roleDb.setNextHeroSkillTime(null);
		// } else {
		// long next = this.roleDb.getNextHeroSkillTime().getTime()
		// + XsgGameParamManager.getInstance()
		// .getSkillPointInterval() * 1000;
		// this.roleDb.getNextHeroSkillTime().setTime(next);
		// }
		// }
		if (this.roleDb.getHeroSkillBuyTime() != null
				&& !DateUtil.isSameDay(Calendar.getInstance().getTime(), this.roleDb.getHeroSkillBuyTime())) {
			this.roleDb.setHeroSkillBuyCount(0);
		}
	}

	@Override
	public void addHeroSkillPoint(int num) {
		roleDb.setHeroSkillPoint(roleDb.getHeroSkillPoint() + num);
		// 通知
		roleRt.getNotifyControler().onPropertyChange(Const.PropertyName.SKILL, num);
	}

	@Override
	public HeroPracticeView[] getHeroPracticeList(String heroId) throws NoteException {
		IHero hero = this.getHero(heroId);
		if (hero == null) {
			throw new NoteException(Messages.getString("HeroControler.13"));
		}
		int openSize = 0;// 可开启属性个数
		for (OpenLevelT o : XsgHeroManager.getInstance().getOpenLevelList()) {
			if (hero.getLevel() >= o.level) {
				openSize++;
			}
		}
		int practiceSize = hero.getPracticeSize();
		boolean isAddPractice = false;// 是否增加了新属性
		while (practiceSize < openSize) {
			PracticePropT propT = getRandomProp();
			if (hero.hasProp(propT.propName)) {
				continue;
			}
			hero.addHeroPractice(new HeroPractice(GlobalDataManager.getInstance().generatePrimaryKey(), null, propT.id,
					propT.propName, 1, 0, propT.baseValue, propT.baseExp, practiceSize, propT.color));
			practiceSize++;
			isAddPractice = true;
		}

		if (isAddPractice) {
			this.roleRt.getNotifyControler().onHeroChanged(hero);
		}
		List<HeroPracticeView> practiceViews = hero.getHeroPracticeView();
		return practiceViews.toArray(new HeroPracticeView[0]);
	}

	@Override
	public HeroPracticeView[] resetPractice(String heroId, int id) throws NoteException {
		IHero hero = this.getHero(heroId);
		if (hero == null) {
			throw new NoteException(Messages.getString("HeroControler.13"));
		}
		int xdanNum = this.roleRt.getItemControler().getItemCountInPackage(XDAN);
		if (xdanNum <= 0) {
			throw new NoteException(Messages.getString("HeroControler.14"));
		}
		HeroPractice hp = hero.getHeroByScriptId(id);
		if (hp == null) {
			throw new NoteException(Messages.getString("HeroControler.12"));
		}

		// 计算返还修炼消耗
		XsgHeroManager heroManager = XsgHeroManager.getInstance();
		// 功勋
		int med = heroManager.caculateTotalPracticeGXConsume(hp.getLevel(), hp.getScriptId());
		int exp = heroManager.caculateTotalPracticeExpConsume(hp);

		HeroPractice old = new HeroPractice(null, null, hp.getScriptId(), hp.getPropName(), hp.getLevel(), hp.getExp(),
				0, 0, hp.getIndexof(), hp.getColor());

		this.roleRt.getItemControler().changeItemByTemplateCode(XDAN, -1);
		hero.removeHeroPractice(id);
		PracticePropT propT = getRandomProp();
		while (hero.hasProp(propT.propName)) {
			propT = getRandomProp();
		}
		hp.setAddValue(propT.baseValue);
		hp.setColor(propT.color);
		hp.setExp(0);
		hp.setLevel(1);
		hp.setNextUpExp(propT.baseExp);
		hp.setPropName(propT.propName);
		hp.setScriptId(propT.id);
		hero.addHeroPractice(hp);

		// 返还消耗
		// 折算黑龙王將魂
		int blackDragonNum = XsgHeroManager.getInstance().convertBlackDragonSoul(exp);
		if (med > 0) {
			roleRt.getRewardControler().acceptReward(HeroControler.MED3, med);
		}
		if (blackDragonNum != 0) {// 0判断
			roleRt.getRewardControler().acceptReward(Const.PropertyName.BlackDragonSoul, blackDragonNum);
		}

		this.roleRt.getNotifyControler().onHeroChanged(hero);
		this.resetPracticeEvent.onResetPractice(hero, hp.getIndexof() + 1, old.getPropName(), old.getColor(),
				old.getLevel(), old.getExp(), hp.getPropName(), hp.getColor());
		return getHeroPracticeList(heroId);
	}

	@Override
	public void practice(String heroId, int id, String itemIds) throws NoteException {
		IHero hero = this.getHero(heroId);
		if (hero == null) {
			throw new NoteException(Messages.getString("HeroControler.13"));
		}
		HeroPractice hp = hero.getHeroByScriptId(id);
		if (hp == null) {
			throw new NoteException(Messages.getString("HeroControler.12"));
		}
		PracticePropT propT = XsgHeroManager.getInstance().getPracticePropMap().get(id);
		if (hp.getLevel() >= propT.maxLevel) {
			throw new NoteException(Messages.getString("HeroControler.11"));
		}
		Map<String, Integer> heroNum = new HashMap<String, Integer>();
		for (String hid : itemIds.split(",")) {
			Integer num = heroNum.get(hid);
			if (num == null) {
				heroNum.put(hid, 1);
			} else {
				heroNum.put(hid, num + 1);
			}
		}
		// 检测物品是否足够
		int addExp = 0;
		for (Entry<String, Integer> entry : heroNum.entrySet()) {
			int hasCount = this.roleRt.getItemControler().getItemCountInPackage(entry.getKey());
			if (hasCount < entry.getValue()) {
				throw new NoteException(Messages.getString("HeroControler.10"));
			}
			AbsItemT itemT = XsgItemManager.getInstance().findAbsItemT(entry.getKey());
			int color = itemT.getColor().ordinal();
			addExp += XsgHeroManager.getInstance().getHeroExpT(color).exp * entry.getValue();
		}
		// 模拟升级检测功勋是否足够
		int nextUpExp = hp.getNextUpExp();
		int exp = hp.getExp();
		int level = hp.getLevel();
		int tempAddExp = addExp;
		// 总共需要的功勋
		int sumGx = 0;
		while (tempAddExp >= nextUpExp - exp) {
			level++;
			sumGx += propT.baseExploit * Math.pow(1 + (double) propT.exploitPercent / 100, level - 2);
			if (level >= propT.maxLevel) {
				break;
			}
			tempAddExp -= nextUpExp - exp;
			exp = 0;
			nextUpExp = nextUpExp + nextUpExp * propT.expPercent / 100;
		}
		// 重新赋值给后面事件使用
		exp = hp.getExp();
		level = hp.getLevel();
		tempAddExp = addExp;

		if (roleRt.getItemControler().getItemCountInPackage(MED3) < sumGx) {
			throw new NoteException(Messages.getString("HeroControler.21"));
		}
		roleRt.getNotifyControler().setAutoNotify(false);
		if (sumGx != 0) {
			// 扣除功勋
			this.roleRt.getItemControler().changeItemByTemplateCode(MED3, -sumGx);
		}
		// 扣除物品
		for (Entry<String, Integer> entry : heroNum.entrySet()) {
			this.roleRt.getItemControler().changeItemByTemplateCode(entry.getKey(), -entry.getValue());
		}
		// 循环升级
		boolean isUpdateLevel = false;
		while (addExp >= hp.getNextUpExp() - hp.getExp()) {
			isUpdateLevel = true;
			hp.setLevel(hp.getLevel() + 1);
			// 按百分比增长
			hp.setAddValue(hp.getAddValue() + hp.getAddValue() * propT.valuePercent / 100);
			if (hp.getLevel() >= propT.maxLevel) {
				break;
			}
			addExp -= hp.getNextUpExp() - hp.getExp();
			hp.setExp(0);
			hp.setNextUpExp(hp.getNextUpExp() + hp.getNextUpExp() * propT.expPercent / 100);
		}
		hp.setExp(hp.getExp() + addExp);
		if (isUpdateLevel) {
			this.roleRt.getNotifyControler().onHeroChanged(hero);
		}
		roleRt.getNotifyControler().setAutoNotify(true);
		this.heroPracticeEvent.onHeroPractice(hero, hp.getIndexof() + 1, propT.propName, hp.getColor(), level, exp,
				tempAddExp, hp.getLevel(), hp.getExp(), sumGx);
	}

	/**
	 * 随机一个修炼属性
	 * 
	 * @return
	 */
	private PracticePropT getRandomProp() {
		Map<Integer, PracticePropT> propMap = XsgHeroManager.getInstance().getPracticePropMap();
		List<RandomProp> randomProps = new ArrayList<HeroControler.RandomProp>();
		for (PracticePropT p : propMap.values()) {
			randomProps.add(new RandomProp(p.id, p.weight));
		}
		RandomRange<RandomProp> randomRewardGen = new RandomRange<RandomProp>(randomProps);
		RandomProp randomReward = randomRewardGen.random();
		return propMap.get(randomReward.id);
	}

	private static class RandomProp implements IRandomHitable {
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
	public void setRelationLevel(String heroId, int orignalRelationId, int level) throws NoteException,
			NotEnoughMoneyException {
		IHero hero = this.getHero(heroId);
		if (hero == null) {
			throw new NoteException(Messages.getString("HeroControler.HeroNotExists"));//$NON-NLS-1$
		}
		RelationT orignalRelation = null;
		for (RelationT rt : hero.getTemplate().getRelations()) {
			if (rt.id == orignalRelationId) {
				orignalRelation = rt;
				break;
			}
		}

		if (orignalRelation == null) {
			throw new NoteException(Messages.getString("HeroControler.28")); //$NON-NLS-1$
		}

		RelationT targetRelation = orignalRelation;
		int oldLevel = 0;
		for (; oldLevel < level; oldLevel++) {// 获取需要激活的缘分
			targetRelation = XsgHeroManager.getInstance().findRelationT(targetRelation.nextId);
		}

		if (targetRelation == null) {
			throw new NoteException(Messages.getString("HeroControler.28")); //$NON-NLS-1$
		}

		try {
			this.roleRt.reduceCurrency(new Money(CurrencyType.Jinbi, targetRelation.jinbi));
		} catch (NotEnoughYuanBaoException e) {
			LogManager.error(e);
		}
		hero.setRelationLevel(orignalRelationId, level);
		this.heroRelationChangeEvent.onRelationChange(hero, orignalRelationId, oldLevel, level);
		this.roleRt.getNotifyControler().onHeroChanged(hero);

	}

	@Override
	public AttendantView resetSpecialAttendant(String heroId, byte pos) throws NoteException {
		// 获取当前武将
		IHero hero = this.getHero(heroId);

		if (hero == null) {
			throw new NoteException(Messages.getString("HeroControler.30") + heroId);
		}

		int requiredLevel = XsgGameParamManager.getInstance().resetAttendantLevelLimit();
		if (hero.getLevel() < requiredLevel) {
			throw new NoteException(TextUtil.format(Messages.getString("ItemControler.heroLevelError"), requiredLevel));
		}
		// 排除当前武将的所有特殊随从配置
		List<Integer> templateIds = new ArrayList<Integer>();
		// 排除当前武将模版id
		templateIds.add(hero.getTemplateId());
		XsgAttendant[] attendants = hero.getAttentantsConfig();

		// 添加当前武将所有随从模版id
		AttendantView[] views = hero.getHeroView().attendants;
		for (AttendantView attendantView : views) {
			templateIds.add(attendantView.specialAttendantId);
		}
		/*
		 * if(TextUtil.isBlank(hero.getResetSpecAttendants())){ for
		 * (XsgAttendant xsgAtt : attendants) { if (xsgAtt != null &&
		 * xsgAtt.getTemplate() != null) {
		 * templateIds.add(xsgAtt.getTemplate().specialHeroId); } } }else{ int[]
		 * attendantArr = TextUtil.GSON.fromJson(hero.getResetSpecAttendants(),
		 * new int[Const.Role.Max_Attendant_Count].getClass()); // for (int id :
		 * attendantArr) { for(int i=0; i<attendantArr.length; i++){ int id =
		 * attendantArr[i]; if (id != 0) { templateIds.add(id); }else{
		 * templateIds.add(0); } } }
		 */

		// 生成随机随从
		ResetAttendantT attendantT = this.getRandomAttendants(templateIds);
		if (attendantT == null) {
			throw new NoteException(Messages.getString("HeroControler.32"));
		}

		// 检查消耗物品是否充足
		List<AttendantCostT> attendantCostTs = XsgHeroManager.getInstance().getAttendantCostTs();
		if (attendantCostTs == null || attendantCostTs.size() <= 0) {
			throw new NoteException(Messages.getString("HeroControler.31"));
		}

		// 背包中消耗物品数量必须充足
		for (AttendantCostT attendantCostT : attendantCostTs) {
			int itemNum = this.roleRt.getItemControler().getItemCountInPackage(attendantCostT.itemId);
			if (itemNum < attendantCostT.num) {
				throw new NoteException(Messages.getString("HeroControler.33"));
			}
		}

		// 扣除消耗物品
		for (AttendantCostT attendantCostT : attendantCostTs) {
			this.roleRt.getItemControler().changeItemByTemplateCode(attendantCostT.itemId, -attendantCostT.num);
		}

		// 设置特殊随从配置
		attendants[pos].getTemplate().specialHeroId = attendantT.itemId;
		hero.setSpecialAttendantId(attendantT.itemId, pos);

		// 是否匹配特殊随从
		IHero attendantByPos = hero.getAttendantByPos(pos);
		if (attendantByPos == null || attendantByPos.getTemplateId() != attendants[pos].getTemplate().specialHeroId) {
			attendants[pos].setSpecial(false);
		} else {
			attendants[pos].setSpecial(true);
		}

		AttendantView generateView = attendants[pos].generateView();
		// System.out.println("generateView.value:"+generateView.value+";
		// isSpecial:"+generateView.special);

		resetAttendantEvent.onAttendantReset(hero, pos, attendantT.itemId);
		this.roleRt.getNotifyControler().onHeroChanged(hero);
		return generateView;
	}

	/**
	 * 随机一个随从武将
	 * 
	 * @param templateIds
	 * @return
	 */
	private ResetAttendantT getRandomAttendants(List<Integer> templateIds) {

		Map<Integer, ResetAttendantT> propMap = XsgHeroManager.getInstance().getAttendantsTs();
		List<RandomProp> randomProps = new ArrayList<HeroControler.RandomProp>();
		for (ResetAttendantT p : propMap.values()) {
			if (!templateIds.contains(p.itemId)) {
				randomProps.add(new RandomProp(p.itemId, p.weight));
			}
		}
		RandomRange<RandomProp> randomRewardGen = new RandomRange<RandomProp>(randomProps);
		RandomProp randomReward = randomRewardGen.random();
		return propMap.get(randomReward.id);
	}

	/**
	 * 查看其他玩家，获取武将身上的装备View
	 * 
	 * @return
	 */
	@Override
	public OthersHeroView getHerosEquips() {
		// 武将+援军列表
		List<HeroView> list_heros = new ArrayList<HeroView>();
		// 装备列表
		List<HeroEquips> list_hero_equips = new ArrayList<HeroEquips>();
		Iterable<IHero> heros = this.roleRt.getFormationControler().getDefaultFormation().getHeros();
		for (Iterator<IHero> iter = heros.iterator(); iter.hasNext();) {
			IHero hero = (IHero) iter.next();
			list_heros.add(hero.getHeroView());

			list_hero_equips.add(new HeroEquips(hero.getTemplateId(), hero.getHeroEquipViews()));
		}
		return new OthersHeroView(list_heros.toArray(new HeroView[list_heros.size()]),
				list_hero_equips.toArray(new HeroEquips[list_hero_equips.size()]));
	}
}
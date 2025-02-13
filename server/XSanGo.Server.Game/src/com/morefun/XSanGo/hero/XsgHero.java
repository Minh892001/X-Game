/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.hero;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.XSanGo.Protocol.AttendantView;
import com.XSanGo.Protocol.DuelTemplateType;
import com.XSanGo.Protocol.EquipPosition;
import com.XSanGo.Protocol.EquipType;
import com.XSanGo.Protocol.FormationSummaryView;
import com.XSanGo.Protocol.GrowableProperty;
import com.XSanGo.Protocol.HeroConsumeView;
import com.XSanGo.Protocol.HeroEquipView;
import com.XSanGo.Protocol.HeroPracticeView;
import com.XSanGo.Protocol.HeroState;
import com.XSanGo.Protocol.HeroView;
import com.XSanGo.Protocol.IntIntPair;
import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.QualityColor;
import com.google.gson.reflect.TypeToken;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.battle.DuelUnit;
import com.morefun.XSanGo.common.BattlePropertyMap;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.FormulaUtil;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.db.game.FactionBattle;
import com.morefun.XSanGo.db.game.FactionBattleMember;
import com.morefun.XSanGo.db.game.HeroPractice;
import com.morefun.XSanGo.db.game.HeroSkill;
import com.morefun.XSanGo.db.game.RoleHero;
import com.morefun.XSanGo.db.game.RoleHeroAwaken;
import com.morefun.XSanGo.db.game.RoleHeroRelation;
import com.morefun.XSanGo.equip.ArtifactLevelT;
import com.morefun.XSanGo.equip.ArtifactPropertyT;
import com.morefun.XSanGo.equip.EquipItem;
import com.morefun.XSanGo.equip.SuitT;
import com.morefun.XSanGo.equip.XsgEquipManager;
import com.morefun.XSanGo.event.protocol.IHeroLevelUp;
import com.morefun.XSanGo.faction.IFaction;
import com.morefun.XSanGo.faction.TechnologyT;
import com.morefun.XSanGo.faction.XsgFactionManager;
import com.morefun.XSanGo.faction.XsgFactionManager.TechnologyType;
import com.morefun.XSanGo.faction.factionBattle.FactionBattleSceneT;
import com.morefun.XSanGo.faction.factionBattle.XsgFactionBattleManager;
import com.morefun.XSanGo.formation.IFormation;
import com.morefun.XSanGo.heroAwaken.AwakenMap;
import com.morefun.XSanGo.heroAwaken.AwakenPropT;
import com.morefun.XSanGo.heroAwaken.HeroAwakenT;
import com.morefun.XSanGo.heroAwaken.HeroBaptizeAddPropT;
import com.morefun.XSanGo.heroAwaken.XsgHeroAwakenManager;
import com.morefun.XSanGo.item.EquipItemT;
import com.morefun.XSanGo.item.FormationBuffItem;
import com.morefun.XSanGo.item.GemT;
import com.morefun.XSanGo.item.NormalItemT;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.partner.IPartner;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * 武将运行时对象类
 * 
 * @author Su LingYun
 * 
 */
public class XsgHero implements IHero, IHeroRelationProxy {
	private static final Log log = LogFactory.getLog(XsgHero.class);

	// /** 百分比属性的上限 */
	// private static final int Max_Rate_Value = 6600;

	private IRole roleRt;

	private RoleHero heroDb;

	private HeroT heroT;

	private Map<EquipPosition, EquipItem> equipMap;

	private XsgAttendant[] attendants;

	private HeroAttachData attachData;

	// 用于保存随从配置信息 专属随从模版id
	private int[] attendantArr;

	private IHeroLevelUp heroLevelUp;

	private AttendantT[] defaultAttendants;

	public XsgHero(IRole role, RoleHero heroDb) {
		this.roleRt = role;
		this.heroDb = heroDb;
		this.heroT = XsgHeroManager.getInstance().getHeroT(this.heroDb.getTemplateId());
		if (this.heroT == null) {
			throw new IllegalStateException(String.valueOf(this.heroDb.getTemplateId()));
		}

		this.defaultAttendants = XsgHeroManager.getInstance().getDefaultAttendants(this.heroDb.getTemplateId());

		this.attachData = TextUtil.GSON.fromJson(heroDb.getAttachData(), HeroAttachData.class);
		// 初始化装备
		initEquip();

		if (this.heroDb.getHeroSkills().size() == 0) {
			this.initSkill();
		}

		// 初始化专属随从模版id
		if (TextUtil.isBlank(this.heroDb.getSpecialAttendant())) {
			// 与随从长度一致
			attendantArr = new int[Const.Role.Max_Attendant_Count];
		} else {
			attendantArr = TextUtil.GSON.fromJson(this.heroDb.getSpecialAttendant(),
					new int[Const.Role.Max_Attendant_Count].getClass());
		}
		// 事件
		heroLevelUp = roleRt.getEventControler().registerEvent(IHeroLevelUp.class);
	}

	@Override
	public final void initSkill() {
		for (int[] skill : this.heroT.getSkillArray()) {
			HeroSkill hs = new HeroSkill(GlobalDataManager.getInstance().generatePrimaryKey(), this.heroDb, skill[0],
					skill[1]);
			this.heroDb.getHeroSkills().put(hs.getTemplateId(), hs);
		}
	}

	/**
	 * 初始化装备
	 */
	private void initEquip() {
		this.equipMap = new HashMap<EquipPosition, EquipItem>();
		if (!TextUtil.isBlank(this.heroDb.getEquipConfig())) {
			String[] equipIdArray = this.heroDb.getEquipConfig().split(",");
			for (String itemId : equipIdArray) {
				EquipItem equip = this.roleRt.getItemControler().getItem(itemId, EquipItem.class);
				if (equip == null) {
					LogManager.warn(TextUtil.format("Equip [{0}] is not found.", itemId));
				} else {
					this.equipMap.put(equip.getEquipPos(), equip);
				}
			}
		}
	}

	/**
	 * 初始化随从
	 */
	@Override
	public void initAttendant() {
		this.attendants = new XsgAttendant[Const.Role.Max_Attendant_Count];
		// 用于保存随从配置信息 专属随从模版id
		if (this.heroT.getAttendants() == null) {
			LogManager.warn(TextUtil.format("[{0}]缺少随从数据。", this.heroT.name));
			return;
		}
		// 设置重置随从所需物品及数量
		List<AttendantCostT> attendantCostTs = XsgHeroManager.getInstance().getAttendantCostTs();
		List<IntString> costItems = new ArrayList<IntString>();
		for (AttendantCostT attendantCostT : attendantCostTs) {
			IntString is = new IntString(attendantCostT.num, attendantCostT.itemId);
			costItems.add(is);
		}

		// 从模版中首次初始化随从配置，并保存到数据库记录中
		for (int i = 0; i < this.attendants.length; i++) {
			this.attendants[i] = new XsgAttendant(this.heroT.getAttendants()[i]);
			IntString[] costItemArr = costItems.toArray(new IntString[0]);
			this.attendants[i].setCostItems(costItemArr);
		}

		if (this.attachData.attendants != null) {
			for (AttendantData data : this.attachData.attendants) {
				this.attendants[data.pos].setHero(this.roleRt.getHeroControler().getHero(data.heroId));
			}

		}
	}

	@Override
	public HeroView getHeroView() {
		return getHeroView(false);
	}

	@Override
	public HeroView getHeroView(boolean isCalculationTempProp) {
		byte qualityAddPercents = 0;
		byte starLevel = this.heroDb.getStar();
		float hpGrow = this.heroT.getHpGrow(starLevel);
		float powerGrow = this.heroT.getPowerGrow(starLevel);
		float intelGrow = this.heroT.getIntelGrow(starLevel);

		// 先计算战力，再处理暴击率和抗爆率，如果先处理则暴击值在战力中重复体现且可能出现等级升高战力降低的情况
		Map<String, GrowableProperty> propertyMap = this.caculateProperties(isCalculationTempProp);
		GrowableProperty[] arrayForCaculateBattlePower = propertyMap.values().toArray(new GrowableProperty[0]);
		int battlePower = XsgHeroManager.getInstance().caculateBattlePower(arrayForCaculateBattlePower,
				BattlePropertyHolder.Hero);

		GrowableProperty[] lastProperties = propertyMap.values().toArray(new GrowableProperty[0]);
		// Property[] lastProperties = this.caculateProperties();
		byte awakenState = (byte) (this.getTemplate().isAwaken == 0 ? 0 : (isAwaken() ? 2 : 1));
		int awakenStar = getRoleHeroAwaken() == null ? 0 : getRoleHeroAwaken().getStar();
		HeroView view = new HeroView(this.getId(), this.getTemplateId(), this.getName(), battlePower,
				(short) this.getLevel(), starLevel, this.getQualityLevel(), getShowColor().ordinal(),
				(byte) this.getBreakLevel(), this.heroDb.getExp(), XsgHeroManager.getInstance().getLevelupExp(
						this.getLevel()), hpGrow, powerGrow, intelGrow, qualityAddPercents, lastProperties,
				this.getEquipViews(), this.getLeveuUpRelations(), this.getActivatedRelations(), this.getAttendants(),
				this.getState(), this.getSkills(), roleRt.getTreasureControler().isInTreasureGroup(this.getId()),
				awakenState, awakenStar);

		return view;
	}

	/**
	 * 获取已升级过缘分数据
	 * 
	 * @return
	 */
	private IntIntPair[] getLeveuUpRelations() {
		List<IntIntPair> list = new ArrayList<IntIntPair>();
		for (RoleHeroRelation rhr : this.heroDb.getRoleHeroRelations().values()) {
			list.add(new IntIntPair(rhr.getTemplateId(), rhr.getLevel()));
		}

		return list.toArray(new IntIntPair[0]);
	}

	@Override
	public IntIntPair[] getSkills() {
		List<IntIntPair> list = new ArrayList<IntIntPair>();
		for (HeroSkill hs : this.heroDb.getHeroSkills().values()) {
			list.add(new IntIntPair(hs.getTemplateId(), hs.getLevel()));
		}
		return list.toArray(new IntIntPair[0]);
	}

	/**
	 * 
	 * 
	 * @return
	 */
	private Map<String, GrowableProperty> caculateProperties() {
		return caculateProperties(false);
	}

	/**
	 * 计算武将属性
	 * 
	 * @param isCalculationTempProp
	 *            是否计算临时属性
	 * @return
	 */
	private Map<String, GrowableProperty> caculateProperties(boolean isCalculationTempProp) {
		// 基础+成长
		BattlePropertyMap baseMap = this.generateBattlePropertyFromBase();
		// 装备加成
		BattlePropertyMap extMap = this.generateBattlePropertyMapFromEquip();
		// 技能加成
		extMap.combine(this.generateBattlePropertyMapFromSkill());
		// 品质加成
		extMap.combine(this.generateBattlePropertyMapFromColor());
		// 突破加成
		extMap.combine(this.generateBattlePropertyMapFromBreak());
		// 套装加成
		extMap.combine(this.generateBattlePropertyMapFromSuit());
		// 缘分加成
		extMap.combine(this.generateBattlePropertyMapFromRelation());
		// 随从加成
		extMap.combine(this.generateBattlePropertyMapFromAttendant());
		// 阵法加成
		extMap.combine(this.generateBattlePropertyMapFromFormationBuff());
		// 伙伴加成
		extMap.combine(this.generateBattlePropertyMapFromPartner());
		// 神器加长
		extMap.combine(this.generateBattlePropertyMapFromArtifact());
		
		// 基础值增加武将修炼属性
		for (HeroPractice hp : this.heroDb.getHeroPractice().values()) {
			baseMap.combine(hp.getPropName(), hp.getAddValue());
		}
		// 基础值增加武将觉醒属性
		extMap.combine(generateBattlePropertyMapFromHeroAwaken());

		// 公会战BUFF临时属性
		if (isCalculationTempProp) {
			extMap.combine(generateBattlePropertyMapFromFactionBattle());
		}

		// 不加任何公式的处理，汇总属性
		Map<String, GrowableProperty> map = new HashMap<String, GrowableProperty>();
		for (Entry<String, GrowableProperty> entry : baseMap) {
			map.put(entry.getKey(), new GrowableProperty(entry.getKey(), entry.getValue().value, 0));
		}
		for (Entry<String, GrowableProperty> entry : extMap) {
			GrowableProperty gp = new GrowableProperty(entry.getKey(), 0, entry.getValue().value);
			if (map.containsKey(entry.getKey())) {
				gp.value = map.get(entry.getKey()).value;
			}

			map.put(entry.getKey(), gp);
		}

		// 下面是一些属性的特殊处理
		// HP
		GrowableProperty p = map.get(Const.PropertyName.Hero.HP);
		p.grow += (p.value + p.grow) * extMap.getValue(Const.PropertyName.Hero.Hp_Percent) / Const.Ten_Thousand;
		p.grow += extMap.getValue(Const.PropertyName.Hero.Final_Hp_Gem);
		p.grow += extMap.getValue(Const.PropertyName.Hero.Final_Hp);

		// POWER
		p = map.get(Const.PropertyName.Hero.POWER);
		p.grow += (p.value + p.grow) * extMap.getValue(Const.PropertyName.Hero.Power_Percent) / Const.Ten_Thousand;
		p.grow += extMap.getValue(Const.PropertyName.Hero.Final_Power_Gem);
		p.grow += extMap.getValue(Const.PropertyName.Hero.Final_Power);

		// 智力
		p = map.get(Const.PropertyName.Hero.Magic);
		p.grow += (p.value + p.grow) * extMap.getValue(Const.PropertyName.Hero.Magic_Percent) / Const.Ten_Thousand;
		p.grow += extMap.getValue(Const.PropertyName.Hero.Final_Magic_Gem);
		p.grow += extMap.getValue(Const.PropertyName.Hero.Final_Magic);

		// PHYDEF
		p = map.get(Const.PropertyName.Hero.PHYDEF);
		p.grow += (p.value + p.grow) * extMap.getValue(Const.PropertyName.Hero.PHYDEF_Percent) / Const.Ten_Thousand;
		p.grow += extMap.getValue(Const.PropertyName.Hero.Final_PHYDEF_Gem);
		p.grow += extMap.getValue(Const.PropertyName.Hero.Final_PHYDEF);

		// MAGICDEF
		p = map.get(Const.PropertyName.Hero.MAGICDEF);
		p.grow += (p.value + p.grow) * extMap.getValue(Const.PropertyName.Hero.MAGICDEF_Percent) / Const.Ten_Thousand;
		p.grow += extMap.getValue(Const.PropertyName.Hero.Final_MAGICDEF_Gem);
		p.grow += extMap.getValue(Const.PropertyName.Hero.Final_MAGICDEF);

		// 反控
		if (map.containsKey(Const.PropertyName.Hero.CHANGE_BUFFLIFETIME)) {
			p = map.get(Const.PropertyName.Hero.CHANGE_BUFFLIFETIME);
			// 计算被控减少值
			int value = (int) ((p.value + p.grow) * (roleRt.getLevel() / (double) 100 + 1));
			if (map.containsKey(Const.PropertyName.Hero.CHANGE_BUFFLIFETIME_DEF)) {
				map.get(Const.PropertyName.Hero.CHANGE_BUFFLIFETIME_DEF).value += value;
			} else {
				GrowableProperty gp = new GrowableProperty(Const.PropertyName.Hero.CHANGE_BUFFLIFETIME_DEF, value, 0);
				map.put(gp.code, gp);
			}
		}
		// 攻击速度
		if (map.containsKey(Const.PropertyName.Hero.Attatch_Speed_Percent)) {
			p = map.get(Const.PropertyName.Hero.Attatch_Speed_Percent);
			p.grow /= 1;// 数值溢出了，擦屁股
		}

		// 处理公会科技加成
		if (roleRt.getFactionControler().isInFaction() && isInFormation()) {
			List<TechnologyT> list = XsgFactionManager.getInstance().getTechnologyListByType(TechnologyType.ALL);
			for (TechnologyT t : list) {
				GrowableProperty temp = map.get(t.code);
				if (temp != null) {
					temp.value += roleRt.getFactionControler().getTechnologyValue(t.id);
				} else {
					temp = new GrowableProperty(t.code, roleRt.getFactionControler().getTechnologyValue(t.id), 0);
					map.put(t.code, temp);
				}
			}
		}

		// 暴击率的处理
		double rateFromValue = FormulaUtil.calculateCritRate(this.getValue(map, Const.PropertyName.Hero.CRIT),
				this.getLevel());
		int baseRate = (int) rateFromValue;
		String key = Const.PropertyName.Hero.CRIT_RATE;
		if (map.containsKey(key)) {
			map.get(key).value += baseRate;
		} else {
			map.put(key, new GrowableProperty(key, baseRate, 0));
		}
		// this.handleOverflow(map.get(key), Max_Rate_Value); //
		// 调整为服务器不做限制，客户端做限制

		// 抗爆率
		rateFromValue = FormulaUtil.calculateDecritRate(this.getValue(map, Const.PropertyName.Hero.Decrit),
				this.getLevel());
		baseRate = (int) rateFromValue;
		key = Const.PropertyName.Hero.Decrit_Rate;
		if (map.containsKey(key)) {
			map.get(key).value += baseRate;
		} else {
			map.put(key, new GrowableProperty(key, baseRate, 0));
		}
		// this.handleOverflow(map.get(key), Max_Rate_Value); //
		// 调整为服务器不做限制，客户端做限制

		// 物伤减少
		baseRate = (int) FormulaUtil.calculateDefPoro(this.getValue(map, Const.PropertyName.Hero.PHYDEF),
				this.getLevel());
		key = Const.PropertyName.Hero.Def_Poro;
		if (map.containsKey(key)) {
			map.get(key).value += baseRate;
		} else {
			map.put(key, new GrowableProperty(key, baseRate, 0));
		}
		// this.handleOverflow(map.get(key), Max_Rate_Value); //
		// 调整为服务器不做限制，客户端做限制

		// 魔伤减少
		baseRate = (int) FormulaUtil.calculateMagicDefPoro(this.getValue(map, Const.PropertyName.Hero.MAGICDEF),
				this.getLevel());
		key = Const.PropertyName.Hero.Magic_Def_Poro;
		if (map.containsKey(key)) {
			map.get(key).value += baseRate;
		} else {
			map.put(key, new GrowableProperty(key, baseRate, 0));
		}
		// this.handleOverflow(map.get(key), Max_Rate_Value); //
		// 调整为服务器不做限制，客户端做限制

		// 怒气消耗减少上限为39%
		this.handleOverflow(map.get(Const.PropertyName.Hero.Anger_Save), 3900);

		int skills = 0;// 技能等级
		for (HeroSkill hs : this.heroDb.getHeroSkills().values()) {
			skills += hs.getLevel();
		}
		GrowableProperty gp = new GrowableProperty(Const.PropertyName.Hero.Total_Skill_level, skills, 0);
		map.put(gp.code, gp);

		return map;
		// return map.values().toArray(new GrowableProperty[0]);
	}

	/**
	 * 伙伴属性加成效果
	 * 
	 * @return
	 */
	private BattlePropertyMap generateBattlePropertyMapFromPartner() {
		// 获取布阵中的武将
		// 获取当前角色的伙伴属性数据
		// 将伙伴属性加成到布阵中的武将中
		BattlePropertyMap map = new BattlePropertyMap();
		IFormation formation = this.getReferenceFormation();

		IPartner partner = this.getPartner();
		if (formation != null && partner != null) {

			BattlePropertyMap battlePropertyMap = partner.getBattlePropertyMap();
			map.combine(battlePropertyMap);
		}

		return XsgHeroManager.getInstance().translatePropertyMap(map);
	}

	@Override
	public IPartner getPartner() {
		return roleRt.getPartnerControler() == null ? null : roleRt.getPartnerControler().getPartner();
	}

	/**
	 * 获取突破加成效果
	 * 
	 * @return
	 */
	private BattlePropertyMap generateBattlePropertyMapFromBreak() {
		BattlePropertyMap map = new BattlePropertyMap();
		int breakLvl = this.getBreakLevel();
		if (breakLvl > 0) {
			for (int i = 1; i <= breakLvl; i++) {
				HeroBreakT hbt = XsgHeroManager.getInstance().findHeroBreakT(this.getTemplateId(), i);
				if (hbt != null) {
					map.combine(hbt.getBattlePropertyMap());
				}
			}
		}

		return XsgHeroManager.getInstance().translatePropertyMap(map);
	}

	/**
	 * 处理溢出值
	 * 
	 * @param gp
	 * @param limit
	 */
	private void handleOverflow(GrowableProperty gp, int limit) {
		if (gp == null) {
			return;
		}

		int overflow = (int) (gp.value + gp.grow - limit);
		if (overflow <= 0) {
			return;
		}
		int part1 = gp.value;
		if (overflow > part1) {
			gp.value = 0;
			gp.grow -= (overflow - part1);
		} else {
			gp.value -= overflow;
		}
	}

	private BattlePropertyMap generateBattlePropertyMapFromSkill() {
		BattlePropertyMap map = new BattlePropertyMap();
		for (HeroSkill skill : this.heroDb.getHeroSkills().values()) {
			HeroSkillT template = XsgHeroManager.getInstance().findHeroSKillT(skill.getTemplateId());
			if (template == null) {
				continue;
			}
			if (template.type == 0 && skill.getLevel() > 0) {// 只计算被动技能
				for (SkillEffectT effect : template.effectArray) {
					if (effect.isEffective()) {
						map.combine(effect.code, (int) (effect.initValue + (skill.getLevel() - 1) * effect.perLevel));
					}
				}
			}
		}

		return XsgHeroManager.getInstance().translatePropertyMap(map);
	}

	/**
	 * 获取品质加成效果
	 * 
	 * @return
	 */
	private BattlePropertyMap generateBattlePropertyMapFromColor() {
		BattlePropertyMap map = new BattlePropertyMap();
		for (int i = 1; i <= this.getQualityLevel(); i++) {
			HeroQualityT hct = XsgHeroManager.getInstance().findHeroColorT(this.getTemplateId(), i);
			map.combine(hct.getBattlePropertyMap());
		}

		return XsgHeroManager.getInstance().translatePropertyMap(map);
	}

	@Override
	public HeroState getState() {
		PositionData pd = this.getHeroPositionData();
		if (pd != null) {
			if (!TextUtil.isBlank(pd.formationId)) {
				if (pd.position <= 11 && pd.position > 8) {
					return HeroState.InTheSupport;
				} else if (pd.position <= 8) {
					return HeroState.InTheFormation;
				}
			}

			if (!TextUtil.isBlank(pd.heroId)) {
				return HeroState.Attendant;
			}

			if (!TextUtil.isBlank(pd.partnerId)) {
				if (pd.position <= 7 && pd.position >= 1) {
					return HeroState.PartnerShip;
				}
			}

		}

		return HeroState.Default;
	}

	@Override
	public String getName() {
		return XsgHeroManager.getInstance().orgnizeHeroName(this.getQualityLevel(), this.getBreakLevel(),
				this.heroT.name);
	}

	/**
	 * 获取阵法加成
	 * 
	 * @return
	 */
	private BattlePropertyMap generateBattlePropertyMapFromFormationBuff() {
		BattlePropertyMap map = new BattlePropertyMap();
		IFormation formation = this.getReferenceFormation();
		if (formation != null && formation.getBuff() != null) {
			Property p = formation.getBuff().getPropertyByPos(this.getHeroPositionData().position);
			if (!TextUtil.isBlank(p.code)) {
				map.combine(new GrowableProperty(p.code, p.value, 0));
			}
		}

		return XsgHeroManager.getInstance().translatePropertyMap(map);
	}

	private BattlePropertyMap generateBattlePropertyFromBase() {

		byte starLevel = this.heroDb.getStar();
		float hpGrow = this.heroT.getHpGrow(starLevel);
		float powerGrow = this.heroT.getPowerGrow(starLevel);
		float intelGrow = this.heroT.getIntelGrow(starLevel);

		BattlePropertyMap propertyMap = new BattlePropertyMap();

		propertyMap.combine(Const.PropertyName.Hero.Brave, this.heroT.brave);
		propertyMap.combine(Const.PropertyName.Hero.Calm, this.heroT.calm);
		// 初始+成长
		// 生命
		propertyMap.combine(Const.PropertyName.Hero.HP, (int) (this.heroT.hp + (this.getLevel() - 1) * hpGrow));
		// 武力
		propertyMap
				.combine(Const.PropertyName.Hero.POWER, (int) (this.heroT.power + (this.getLevel() - 1) * powerGrow));
		// 智力
		propertyMap
				.combine(Const.PropertyName.Hero.Magic, (int) (this.heroT.intel + (this.getLevel() - 1) * intelGrow));
		// 护甲
		propertyMap.combine(Const.PropertyName.Hero.PHYDEF, (int) (this.heroT.phyDef + (this.getLevel() - 1)
				* this.heroT.defGrow));
		// 魔抗
		propertyMap.combine(Const.PropertyName.Hero.MAGICDEF, (int) (this.heroT.magDef + (this.getLevel() - 1)
				* this.heroT.magDefGrow));

		propertyMap.combine(Const.PropertyName.Hero.Hp_Rec, this.heroT.hp_rec);
		propertyMap.combine(Const.PropertyName.Hero.Anger_Rec, this.heroT.anger_rec);
		// 暴击
		propertyMap.combine(Const.PropertyName.Hero.CRIT, (int) (this.heroT.critRate + (this.getLevel() - 1)
				* this.heroT.critGrow));
		// 抗暴
		propertyMap.combine(Const.PropertyName.Hero.Decrit, (int) (this.heroT.decritRate + (this.getLevel() - 1)
				* this.heroT.decritGrow));

		propertyMap.combine(Const.PropertyName.Hero.HITODDS, this.heroT.hitRate);
		propertyMap.combine(Const.PropertyName.Hero.Dodge_Percent, this.heroT.dodgeRate);
		propertyMap.combine(Const.PropertyName.Hero.Attack_Interval, this.heroT.attackInterval);

		return propertyMap;

	}

	/**
	 * 获取随从提供属性
	 * 
	 * @return
	 */
	private BattlePropertyMap generateBattlePropertyMapFromAttendant() {
		BattlePropertyMap map = new BattlePropertyMap();
		for (XsgAttendant attendant : this.attendants) {
			if (attendant != null) {
				String code = attendant.getProperty().code;
				if (!TextUtil.isBlank(code)) {// 初始设计3个随从，目前只开放了两个
					map.combine(new GrowableProperty(code, attendant.getProperty().value, 0));
				}
			}
		}

		return XsgHeroManager.getInstance().translatePropertyMap(map);
	}

	/**
	 * 获取缘分提供属性
	 * 
	 * @return
	 */
	private BattlePropertyMap generateBattlePropertyMapFromRelation() {
		BattlePropertyMap map = new BattlePropertyMap();
		IntIntPair[] actives = this.getActivatedRelations();
		for (IntIntPair pair : actives) {
			RelationT relation = XsgHeroManager.getInstance().findRelationT(pair.first);
			RelationMatchT match = relation.getMatchByObjCount(pair.second);
			map.combine(match.propertyName, match.value);
		}

		return XsgHeroManager.getInstance().translatePropertyMap(map);
	}

	/**
	 * 获取套装提供属性
	 * 
	 * @return
	 */
	private BattlePropertyMap generateBattlePropertyMapFromSuit() {
		BattlePropertyMap map = new BattlePropertyMap();
		// KEY为套装编号，value为套件数量
		Map<Integer, Integer> suitMap = new HashMap<Integer, Integer>();
		for (EquipItem equip : this.equipMap.values()) {
			EquipItemT template = equip.getTemplate(EquipItemT.class);
			if (template.suitId <= 0) {
				continue;
			}

			int totalCount = suitMap.containsKey(template.suitId) ? suitMap.get(template.suitId) : 0;
			totalCount++;
			suitMap.put(template.suitId, totalCount);
		}
		for (int suitId : suitMap.keySet()) {
			SuitT template = XsgEquipManager.getInstance().findSuitT(suitId);
			map.combine(template.getBattlePropertyMap(suitMap.get(suitId)));
		}

		return XsgHeroManager.getInstance().translatePropertyMap(map);
	}

	/**
	 * 获取装备提供的属性
	 * 
	 * @return
	 */
	private BattlePropertyMap generateBattlePropertyMapFromEquip() {
		BattlePropertyMap map = new BattlePropertyMap();
		for (EquipItem equip : this.equipMap.values()) {
			map.combine(equip.getBattlePropertyMap());
		}

		return XsgHeroManager.getInstance().translatePropertyMap(map);
	}

	/**
	 * 获取神器提供的属性
	 * 
	 * @return
	 */
	private BattlePropertyMap generateBattlePropertyMapFromArtifact() {
		BattlePropertyMap map = new BattlePropertyMap();
		ArtifactLevelT levelT = roleRt.getArtifactControler().getHeroArtifactId(getId());
		if (levelT != null) {
			for (ArtifactPropertyT p : levelT.propertys) {
				map.combine(p.code, p.vaule);
			}
		}
		return XsgHeroManager.getInstance().translatePropertyMap(map);
	}

	/**
	 * 获取武将觉醒属性
	 * 
	 * @return
	 */
	private BattlePropertyMap generateBattlePropertyMapFromHeroAwaken() {
		BattlePropertyMap map = new BattlePropertyMap();
		// 觉醒星级属性
		RoleHeroAwaken heroAwaken = getRoleHeroAwaken();
		if (heroAwaken != null) {
			HeroAwakenT heroAwakenT = XsgHeroAwakenManager.getInstance().getHeroAwakens(getTemplateId());
			for (String props : StringUtils.split(heroAwakenT.prop, ";")) {
				String[] prop = StringUtils.split(props, ":");
				map.combine(prop[0], Integer.parseInt(prop[1]) * heroAwaken.getStar());
			}
		}
		// 觉醒洗练属性
		if (isAwaken()) {
			// 洗炼属性加成
			AwakenMap props = TextUtil.GSON.fromJson(heroAwaken.getBaptizeProps(), new TypeToken<AwakenMap>() {
			}.getType());
			if (props != null) {
				for (String propKey : props.keySet()) {
					map.combine(propKey, props.get(propKey));
				}
			}
			// 洗炼等级加成属性
			Map<Integer, HeroBaptizeAddPropT> addProps = XsgHeroAwakenManager.getInstance().getHeroBaptizeAddProps();
			for (HeroBaptizeAddPropT prop : addProps.values()) {
				if (prop.baptizeLvl > heroAwaken.getLvl()) {
					break;
				}
				if (prop.props != null) {
					for (AwakenPropT propT : prop.props) {
						if (TextUtil.isNotBlank(propT.propType)) {
							map.combine(propT.propType, propT.propValue);
						}
					}
				}
			}
			// 洗炼满级属性加成
			if (XsgHeroAwakenManager.getInstance().isBaptizePropFull(this)) {
				HeroBaptizeAddPropT addPropT = addProps.get(heroAwaken.getLvl() + 1);
				if (addPropT != null && addPropT.props != null) {
					for (AwakenPropT propT : addPropT.props) {
						if (TextUtil.isNotBlank(propT.propType)) {
							map.combine(propT.propType, propT.propValue);
						}
					}
				}
			}
		}
		return XsgHeroManager.getInstance().translatePropertyMap(map);
	}

	/**
	 * 获取随从数据
	 * 
	 * @return
	 */
	private BattlePropertyMap generateBattlePropertyMapFromFactionBattle() {
		IFaction faction = roleRt.getFactionControler().getMyFaction();
		FactionBattle fb = faction.getFactionBattle();
		FactionBattleMember fbm = faction.getFactionBattleMember(roleRt.getRoleId());
		BattlePropertyMap map = new BattlePropertyMap();

		if (fb != null && fbm != null) {
			FactionBattleSceneT sceneT = XsgFactionBattleManager.getInstance()
					.getBattleSceneT(fb.getCampStrongholdId());
			List<String> strongholdList = TextUtil.stringToList(sceneT.homeCourt);
			if (strongholdList.contains(String.valueOf(fbm.getStrongholdId()))) {// 符合要求
				sceneT = XsgFactionBattleManager.getInstance().getBattleSceneT(fbm.getStrongholdId());
				if (TextUtil.isNotBlank(sceneT.buffEffect)) {
					String[] buffs = StringUtils.split(sceneT.buffEffect, ";");
					for (String buff : buffs) {
						String[] values = StringUtils.split(buff, ":");
						map.combine(values[0], Integer.parseInt(values[1]));
					}
				}
			}
		}
		return map;
	}

	/**
	 * 获取随从数据
	 * 
	 * @return
	 */
	private AttendantView[] getAttendants() {
		this.initAttendant();
		List<AttendantView> list = new ArrayList<AttendantView>();
		AttendantView tempView;
		// 通过DB记录判断玩家是否有重置
		boolean resetFlag = TextUtil.isBlank(this.heroDb.getSpecialAttendant());
		int[] attDbArr = null;
		String temp = this.heroDb.getSpecialAttendant();
		if (!resetFlag) {// 若有重置记录，则获取重置后的随从数据
			attDbArr = TextUtil.GSON.fromJson(temp, new int[Const.Role.Max_Attendant_Count].getClass());
		}
		for (int i = 0; i < this.attendants.length; i++) {
			XsgAttendant attendant = this.attendants[i];
			// System.out.println(this.heroT.name+"default: "+XsgHeroManager.getInstance().getDefaultAttendants(heroDb.getTemplateId())[i].specialHeroId);
			if (resetFlag || attDbArr[i] == 0) {// 若玩家未重置，则使用默认脚本配置随从
				attendant.getTemplate().specialHeroId = this.defaultAttendants[i].specialHeroId;
			} else {
				attendant.getTemplate().specialHeroId = attDbArr[i];
			}
			tempView = attendant == null ? null : attendant.generateView();
			if (tempView != null) {
				list.add(tempView);
			} else {
				// 随从为空的时候，创建一个默认构造的对象给客户端
				list.add(new AttendantView());
			}
		}

		return list.toArray(new AttendantView[0]);
	}

	private String[] getEquipViews() {
		List<String> list = new ArrayList<String>();
		for (EquipItem equip : this.equipMap.values()) {
			list.add(equip.getId());
		}

		return list.toArray(new String[0]);
	}

	/**
	 * 查看其他玩家，获取武将身上的装备View
	 * 
	 * @return
	 */
	public ItemView[] getHeroEquipViews() {
		List<ItemView> list = new ArrayList<ItemView>();
		for (EquipItem equip : this.equipMap.values()) {
			list.add(equip.getView());
		}

		return list.toArray(new ItemView[list.size()]);
	}

	public List<HeroEquipView> getEquipDetails() {
		List<HeroEquipView> list = new ArrayList<HeroEquipView>();
		StringBuffer sb = null;
		for (EquipItem equip : this.equipMap.values()) {
			sb = new StringBuffer();
			boolean first = true;
			for (IntString is : equip.getGemPairs()) {
				if (!TextUtil.isBlank(is.strValue)) {
					GemT gemT = (GemT) XsgItemManager.getInstance().findAbsItemT(is.strValue);
					if (!first) {
						sb.append(",");
						first = false;
					}
					sb.append(gemT.getName());
				}
			}
			list.add(new HeroEquipView(equip.getRefereneHero(), equip.getId(), equip.getLevel(), equip.getStar(), equip
					.getStarExp(), equip.getGrow(), equip.getGrow2(), sb.toString()));
		}
		return list;
	}

	/**
	 * 获取已激活缘分
	 * 
	 * @return
	 */
	private IntIntPair[] getActivatedRelations() {
		List<IntIntPair> list = new ArrayList<IntIntPair>();
		for (RelationT rt : this.heroT.getRelations()) {
			if (rt == null) {
				continue;
			}
			int needBreakLevel = this.getBreakRelationNeedLevel(rt);
			if (this.getBreakLevel() < needBreakLevel) {
				continue;
			}

			// 如果缘分升过级，则用最新的缘分来替代
			RoleHeroRelation relationChange = this.heroDb.getRoleHeroRelations().get(rt.id);
			if (relationChange != null) {
				for (int i = 0; i < relationChange.getLevel(); i++) {
					RelationT temp = XsgHeroManager.getInstance().findRelationT(rt.nextId);
					if (temp == null) {
						break;
					}

					rt = temp;
				}
			}
			List<RelationMatchT> matchList = rt.match(this);
			for (RelationMatchT match : matchList) {
				list.add(new IntIntPair(rt.id, match.needObjCount));
			}
		}

		return list.toArray(new IntIntPair[0]);
	}

	/**
	 * 获取缘分开启需要的突破等级
	 * 
	 * @param rt
	 * @return
	 */
	private int getBreakRelationNeedLevel(RelationT rt) {
		int[] breakRelations = TextUtil.GSON.fromJson("[" + this.getTemplate().relationFromBreak + "]", int[].class);
		int index = -1;
		for (int i = 0; i < breakRelations.length; i++) {
			if (rt.id == breakRelations[i]) {
				index = i;
				break;
			}
		}
		if (index == -1) {// 不属于突破扩展的缘分
			return 0;
		}

		for (int i = 1; i < Const.MaxBreakLevel; i++) {
			HeroBreakConsumeT hbct = XsgHeroManager.getInstance().findBreakLevelUpT(i);
			if (hbct != null && hbct.unlockRelationIndex == index) {
				return i;
			}
		}

		return 0;
	}

	@Override
	public int howManyHeroInTheSameGroup(Integer[] heroIdArray) {
		if (!this.isInFormation()) {
			return 0;
		}

		IFormation formation = this.roleRt.getFormationControler().getFormation(this.attachData.posData.formationId);

		IPartner partner = this.getPartner();

		int result = 0;
		for (int i : heroIdArray) {// 布阵或者伙伴中的武将包含包含缘分列表中的武将
			if (formation.containsHero(i) || (partner != null && partner.containsHero(i))) {
				result++;
			}
		}

		return result;
	}

	private boolean isInFormation() {
		return this.attachData.posData != null && !TextUtil.isBlank(this.attachData.posData.formationId);
	}

	@Override
	public boolean hasEquip(String equipTemplate) {
		for (EquipItem equip : this.equipMap.values()) {
			if (equip.getTemplate().getId().equals(equipTemplate)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String getId() {
		return this.heroDb.getId();
	}

	@Override
	public void setHeroPositionData(PositionData pd) {
		this.attachData.posData = pd;
		this.flushData();
	}

	@Override
	public PositionData getHeroPositionData() {
		return this.attachData.posData;
	}

	@Override
	public int getTemplateId() {
		return this.heroDb.getTemplateId();
	}

	@Override
	public void setHeroEquip(EquipPosition ep, EquipItem equip) {
		if (equip == null) {
			this.equipMap.remove(ep);
		} else {
			this.equipMap.put(equip.getEquipPos(), equip);
		}

		this.flushData();
	}

	private void flushData() {
		// 随从数据
		List<AttendantData> attendantList = new ArrayList<AttendantData>();
		for (int i = 0; i < this.attendants.length; i++) {
			if (this.attendants[i] != null && this.attendants[i].getHero() != null) {
				AttendantData ad = new AttendantData();
				ad.pos = i;
				ad.heroId = this.attendants[i].getHero().getId();
				attendantList.add(ad);
			}
		}
		this.attachData.attendants = attendantList.toArray(new AttendantData[0]);
		this.heroDb.setAttachData(TextUtil.GSON.toJson(this.attachData));

		// 装备数据
		List<String> equipIdList = new ArrayList<String>();
		for (EquipItem equip : this.equipMap.values()) {
			equipIdList.add(equip.getId());
		}
		this.heroDb.setEquipConfig(TextUtil.join(equipIdList, ","));
	}

	@Override
	public EquipItem getEquipByPos(EquipPosition equipPos) {
		return this.equipMap.get(equipPos);
	}

	@Override
	public QualityColor getColor() {
		return QualityColor.valueOf(this.heroT.color);
	}

	@Override
	public QualityColor getShowColor() {
		int color = this.heroT.color;
		// 绿将变蓝色的进阶等级
		final int g2bLevel = XsgGameParamManager.getInstance().getHeroG2BLevel();
		// 蓝将变紫色的进阶等级
		final int b2pLevel = XsgGameParamManager.getInstance().getHeroB2PLevel();
		// 绿色武将+5变成蓝色,+8变成紫色
		if (color == QualityColor.Green.ordinal()) {
			if (getQualityLevel() >= g2bLevel) {
				color = QualityColor.Blue.ordinal();
			}
			if (getQualityLevel() >= b2pLevel) {
				color = QualityColor.Violet.ordinal();
			}
		} else if (color == QualityColor.Blue.ordinal()) {
			// 蓝色武将+5变成紫色
			if (getQualityLevel() >= g2bLevel) {
				color = QualityColor.Violet.ordinal();
			}
		}
		return QualityColor.valueOf(color);
	}

	@Override
	public boolean hasAttendant() {
		for (XsgAttendant att : this.attendants) {
			if (att.getHero() != null) {
				return true;
			}
		}

		return false;
	}

	@Override
	public IHero getAttendantByPos(byte pos) {
		return this.attendants[pos] == null ? null : this.attendants[pos].getHero();
	}

	@Override
	public void setAttendant(byte pos, IHero attendant) {
		this.attendants[pos].setHero(attendant);
		this.flushData();
	}

	@Override
	public int getBattlePower() {
		return XsgHeroManager.getInstance().caculateBattlePower(
				this.caculateProperties().values().toArray(new GrowableProperty[0]), BattlePropertyHolder.Hero);
	}

	@Override
	public byte getStar() {
		return this.heroDb.getStar();
	}

	@Override
	public void starUp() {
		this.heroDb.setStar((byte) (getStar() + 1));
	}

	@Override
	public void colorUp() {
		this.heroDb.setColor((byte) (this.heroDb.getColor() + 1));
	}

	@Override
	public boolean canEquip(EquipItem equip) {
		if (equip.getEquipPos() == EquipPosition.EquipPositionTreasureOrHorse) {
			if (this.heroT.canDuel()) {
				if (equip.getTemplate(EquipItemT.class).getType() == EquipType.EquipTypeTreasure) {
					return false;
				}
			}
			// switch (this.heroT.oneVsOne) {
			// case 0:
			// if (equip.getTemplate(EquipItemT.class).getType() !=
			// EquipType.EquipTypeHorse) {
			// return false;
			// }
			// break;
			// case 1:
			// if (equip.getTemplate(EquipItemT.class).getType() !=
			// EquipType.EquipTypeTreasure) {
			// return false;
			// }
			// break;
			// default:
			// break;
			// }
		}

		return true;
	}

	@Override
	public int getLevel() {
		return this.heroDb.getLevel();
	}

	@Override
	public void winExp(int exp) {
		exp += this.heroDb.getExp();
		// 升级逻辑
		int maxLevel = this.getLevelLimit();

		int levelUpExp = XsgHeroManager.getInstance().getLevelupExp(this.getLevel());

		boolean isLevelUp = false;
		while (this.getLevel() < maxLevel) {
			if (exp < levelUpExp) {
				break;
			}
			exp -= levelUpExp;
			this.heroDb.setLevel(this.getLevel() + 1);
			isLevelUp = true;
			levelUpExp = XsgHeroManager.getInstance().getLevelupExp(this.getLevel());

			// 技能处理逻辑
			this.tryOpenNewSkill();
		}
		this.heroDb.setExp(Math.min(exp, levelUpExp));
		this.roleRt.getNotifyControler().onHeroChanged(this);

		if (isLevelUp) {
			heroLevelUp.onHeroLevelUp(this.heroDb.getTemplateId(), this.heroDb.getLevel());
		}
	}

	@Override
	public int getExp() {
		return heroDb.getExp();
	}

	@Override
	public void setExp(int exp) {
		heroDb.setExp(exp);
	}

	/**
	 * 根据各技能的需要等级和初始等级，检查是否有新技能开启
	 */
	private void tryOpenNewSkill() {
		for (HeroSkill hs : this.heroDb.getHeroSkills().values()) {
			if (hs.getLevel() > 0) {
				continue;
			}

			HeroSkillT template = XsgHeroManager.getInstance().findHeroSKillT(hs.getTemplateId());
			if (template.isAwakenSkill == 1) {// 觉醒技能需要觉醒后才开放
				continue;
			}
			if (this.getLevel() >= template.studyLevel) {
				hs.setLevel(template.orignalLevel);
			}
		}
	}

	private int getLevelLimit() {
		int maxLevel = this.roleRt.getLevel() * 2;
		if (this.roleRt.getLevel() < 10) {
			maxLevel += 2;
		}
		return maxLevel;
	}

	@Override
	public HeroT getTemplate() {
		return this.heroT;
	}

	@Override
	public boolean isFormationBuffEquals(String buffId) {
		PositionData pd = this.getHeroPositionData();
		if (pd != null) {
			if (!TextUtil.isBlank(pd.formationId)) {
				FormationBuffItem buff = this.roleRt.getFormationControler().getFormation(pd.formationId).getBuff();
				if (buff != null) {
					return buff.getTemplate().getId().equals(buffId);
				}
			}
		}

		return false;
	}

	@Override
	public boolean isAttendant() {
		return this.getState() == HeroState.Attendant;
	}

	@Override
	public HeroSkill getHeroSkill(int skillId) {
		return this.heroDb.getHeroSkills().get(skillId);
	}

	@Override
	public boolean setHeroSkill(int skillId, int level) {
		HeroSkill skill = getHeroSkill(skillId);
		if (skill != null) {
			skill.setLevel(level);
			return true;
		}
		return false;
	}

	@Override
	public DuelUnit createDuelUnit() {
		Map<String, GrowableProperty> map = this.caculateProperties();

		int maxHp = this.getValue(map, Const.PropertyName.Hero.HP);
		int brave = this.getValue(map, Const.PropertyName.Hero.Brave);
		int calm = this.getValue(map, Const.PropertyName.Hero.Calm);
		int power = this.getValue(map, Const.PropertyName.Hero.POWER);
		int intel = this.getValue(map, Const.PropertyName.Hero.Magic);
		int dodge = this.getValue(map, Const.PropertyName.Hero.Dodge_Percent);
		int critRate = this.getValue(map, Const.PropertyName.Hero.CRIT_RATE);
		int critResRate = this.getValue(map, Const.PropertyName.Hero.Decrit_Rate);
		int damageRes = this.getValue(map, Const.PropertyName.Hero.Def_Poro);
		return new DuelUnit(DuelTemplateType.DuelTemplateTypeHero, this.getTemplateId(), this.getShowColor().ordinal(),
				this.getStar(), this.getQualityLevel(), (byte) this.getBreakLevel(), this.getName(), getLevel(), maxHp,
				brave, calm, power, intel, dodge, critRate, critResRate, damageRes, XsgHeroManager.getInstance()
						.findDuelSkillT(this.getTemplate().duelSkill));
	}

	private int getValue(Map<String, GrowableProperty> map, String code) {
		return (int) (map.containsKey(code) ? (map.get(code).value + map.get(code).grow) : 0);
	}

	@Override
	public boolean isExpFull() {
		return this.getLevel() >= this.getLevelLimit()
				&& this.heroDb.getExp() >= XsgHeroManager.getInstance().getLevelupExp(this.getLevel());
	}

	@Override
	public String toString() {
		return TextUtil.format("[name={0},star={1},color={2},lv={3},exp={4},break={5}]", this.getName(),
				this.heroDb.getStar(), this.heroDb.getColor(), this.heroDb.getLevel(), this.heroDb.getExp(),
				this.getBreakLevel());
	}

	@Override
	public void setLevel(int level) {
		this.heroDb.setLevel(level);
	}

	@Override
	public void setStar(int star) {
		this.heroDb.setStar((byte) star);
	}

	@Override
	public void setColor(int color) {
		this.heroDb.setColor((byte) color);
	}

	@Override
	public IFormation getReferenceFormation() {
		IFormation result = null;
		PositionData pd = this.getHeroPositionData();
		if (pd != null) {
			if (!TextUtil.isBlank(pd.formationId)) {
				result = this.roleRt.getFormationControler().getFormation(pd.formationId);
			}
		}

		return result;
	}

	@Override
	public IHero getMaster() {
		IHero result = null;
		PositionData pd = this.getHeroPositionData();
		if (pd != null) {
			if (!TextUtil.isBlank(pd.heroId)) {
				result = this.roleRt.getHeroControler().getHero(pd.heroId);
			}
		}

		return result;
	}

	@Override
	public int getQualityLevel() {
		return this.heroDb.getColor();
	}

	@Override
	public int getRelationLevel(int orignalRelationId) {
		RoleHeroRelation rhr = this.heroDb.getRoleHeroRelations().get(orignalRelationId);
		return rhr == null ? 0 : rhr.getLevel();
	}

	@Override
	public void setRelationLevel(int orignalRelationId, int level) {
		RoleHeroRelation rhr = this.heroDb.getRoleHeroRelations().get(orignalRelationId);
		if (rhr == null) {// 没有则新建立一条记录
			rhr = new RoleHeroRelation(GlobalDataManager.getInstance().generatePrimaryKey(), heroDb, orignalRelationId,
					0);
			this.heroDb.getRoleHeroRelations().put(orignalRelationId, rhr);
		}

		rhr.setLevel(level);
	}

	@Override
	public HeroConsumeView getHeroConsumeView() {
		HeroConsumeView view = new HeroConsumeView();
		List<Property> propertyList = new ArrayList<Property>();

		XsgHeroManager heroManager = XsgHeroManager.getInstance();
		// 经验丹模版ID
		String expTID = XsgGameParamManager.getInstance().getHeroResetReturnExpTID();
		// 经验丹配置类
		NormalItemT expItemT = (NormalItemT) XsgItemManager.getInstance().findAbsItemT(expTID);
		// 总经验值
		long totalExp = heroManager.caculateTotalExp(getLevel()) + getExp();
		// 经验丹数量
		int expValue = Integer.parseInt(expItemT.useValue);
		int consumeExpCount = (int) ((totalExp + expValue - 1) / expValue);

		// 消耗的金币
		int consumeJinbi = 0;
		// 进阶
		int quality = getQualityLevel();
		// 进阶消耗金币数量
		int colorConsumeJinbi = heroManager.caculateTotalQualityUpJinbiConsume(quality);
		// 进阶消耗功勋数量
		int colorConsumeMed = heroManager.caculateTotalQualityUpMedConsume(quality);
		// 金币累计
		consumeJinbi += colorConsumeJinbi;

		// 技能点
		int skillJinbiConsume = 0;
		int skillConsume = 0;
		// 武将所有技能点
		IntIntPair skillPair[] = getSkills();
		if (skillPair != null && skillPair.length > 0) {
			for (IntIntPair pair : skillPair) {
				// 技能模版
				HeroSkillT template = heroManager.findHeroSKillT(pair.first);
				if (template != null) {
					// 技能初始等级
					int startLvl = template.orignalLevel;
					// 玩家升级了技能
					if (pair.second > startLvl) {
						skillJinbiConsume += heroManager.caculateTotalSkillJinbiConsume(
								getTemplate().getSkillIndex(pair.first), startLvl, pair.second);
						skillConsume += pair.second - startLvl;
					}
				} else {
					log.warn(TextUtil.format("missing skill template with id {0}:{1}", getName(), pair.first));
				}
			}
		}
		// 消耗金币累计
		consumeJinbi += skillJinbiConsume;

		// 武将修炼
		Map<Integer, HeroPractice> practiceMap = heroDb.getHeroPractice();
		int practiceMed = 0;
		int practiceExp = 0;
		if (practiceMap != null) {
			for (HeroPractice hp : practiceMap.values()) {
				practiceExp += heroManager.caculateTotalPracticeExpConsume(hp);
				practiceMed += heroManager.caculateTotalPracticeGXConsume(hp.getLevel(), hp.getScriptId());
			}
		}

		// 升星消耗的金币不返还

		propertyList.add(new Property(expTID, consumeExpCount)); // 经验丹
		propertyList.add(new Property(HeroControler.MED3, colorConsumeMed + practiceMed)); // 功勋
		propertyList.add(new Property(Const.PropertyName.SKILL, skillConsume)); // 技能点
		propertyList.add(new Property(Const.PropertyName.MONEY, consumeJinbi)); // 金币
		propertyList.add(new Property(Const.PropertyName.EXP, practiceExp)); // 修炼经验

		view.consumes = propertyList.toArray(new Property[0]);
		return view;
	}

	@Override
	public void resetHero(int level, int star, int color, boolean resetSkill, int breakLevel, boolean resetPractice,
			boolean practiceToInit) {
		// 重置等级
		if (level >= 0) {
			setLevel(level);
			heroDb.setExp(0);
		}
		// 重置星级
		if (star >= 0) {
			setStar(star);
		}
		// 重置进阶
		if (color >= 0) {
			setColor(color);
		}
		// 重置技能
		if (resetSkill) {
			heroDb.getHeroSkills().clear();
			initSkill();
		}
		// 重置突破等级
		if (breakLevel >= 0) {
			setBreakLevel(breakLevel);
		}
		// 重置修炼属性
		if (resetPractice) {
			removeAllHeroPractice();
		}
		// 修炼属性归零到初始状态, 不删除属性
		if (practiceToInit) {
			resetAllHeroPractice();
		}

		roleRt.getNotifyControler().onHeroChanged(this);
	}

	// @Override
	// public Map<Integer, HeroPractice> getHeroPractice() {
	// return this.heroDb.getHeroPractice();
	// }

	@Override
	public void addHeroPractice(HeroPractice heroPractice) {
		heroPractice.setHero(this.heroDb);
		this.heroDb.getHeroPractice().put(heroPractice.getScriptId(), heroPractice);
	}

	@Override
	public List<HeroPracticeView> getHeroPracticeView() {
		List<HeroPractice> ps = new ArrayList<HeroPractice>();
		for (HeroPractice p : this.heroDb.getHeroPractice().values()) {
			ps.add(p);
		}
		Collections.sort(ps, new Comparator<HeroPractice>() {

			@Override
			public int compare(HeroPractice o1, HeroPractice o2) {
				return o1.getIndexof() - o2.getIndexof();
			}
		});
		List<HeroPracticeView> views = new ArrayList<HeroPracticeView>();
		for (int i = 0; i < ps.size(); i++) {
			HeroPractice hp = ps.get(i);
			HeroPracticeView view = new HeroPracticeView(hp.getScriptId(), hp.getPropName(), hp.getColor(),
					hp.getLevel(), hp.getAddValue(), hp.getExp(), hp.getNextUpExp());
			views.add(view);
		}
		return views;
	}

	@Override
	public HeroPractice getHeroByScriptId(int scriptId) {
		return this.heroDb.getHeroPractice().get(scriptId);
	}

	@Override
	public void removeHeroPractice(int scriptId) {
		this.heroDb.getHeroPractice().remove(scriptId);
	}

	@Override
	public void removeAllHeroPractice() {
		this.heroDb.getHeroPractice().clear();
	}

	@Override
	public void resetAllHeroPractice() {
		Map<Integer, HeroPractice> map = heroDb.getHeroPractice();
		if (map != null) {
			Map<Integer, PracticePropT> propTMap = XsgHeroManager.getInstance().getPracticePropMap();
			for (HeroPractice hp : map.values()) {
				hp.setAddValue(0);
				hp.setExp(0);
				hp.setLevel(1);
				PracticePropT propT = propTMap.get(hp.getScriptId());
				if (propT != null) {
					hp.setNextUpExp(propT.baseExp);
					hp.setAddValue(propT.baseValue);
					hp.setColor(propT.color);
				}
			}
		}
	};

	@Override
	public boolean hasProp(String propName) {
		for (HeroPractice p : this.heroDb.getHeroPractice().values()) {
			if (p.getPropName().equals(propName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getBreakLevel() {
		return this.heroDb.getBreakLevel();
	}

	@Override
	public void breakLevelUp() {
		this.heroDb.setBreakLevel(this.heroDb.getBreakLevel() + 1);
	}

	@Override
	public void setBreakLevel(int lvl) {
		this.heroDb.setBreakLevel(lvl);
	}

	@Override
	public int getPracticeSize() {
		return this.heroDb.getHeroPractice().size();
	}

	@Override
	public XsgAttendant[] getAttentantsConfig() {
		return this.attendants;
	}

	@Override
	public void setSpecialAttendantId(int itemId, byte pos) {
		this.attendants[pos].getTemplate().specialHeroId = itemId;
		if (TextUtil.isBlank(this.heroDb.getSpecialAttendant())) {
			this.attendantArr = new int[Const.Role.Max_Attendant_Count];
		}
		this.attendantArr[pos] = itemId;
		// 随从配置
		this.heroDb.setSpecialAttendant(TextUtil.GSON.toJson(this.attendantArr));
	}

	@Override
	public String getResetSpecAttendants() {
		return this.heroDb.getSpecialAttendant();
	}

	@Override
	public boolean isAwaken() {
		RoleHeroAwaken rha = getRoleHeroAwaken();
		if (rha == null) {// 未觉醒或者未开始觉醒
			return false;
		}
		HeroAwakenT t = XsgHeroAwakenManager.getInstance().getHeroAwakens(heroDb.getTemplateId());
		if (t == null) {
			return false;
		}
		return rha.getIs_awaken() == 1;
	}

	@Override
	public void addSkill(int skillId, int skillLvl) {
		if (!this.heroDb.getHeroSkills().containsKey(skillId)) {// 未拥有的技能才进行处理
			HeroSkill hs = new HeroSkill(GlobalDataManager.getInstance().generatePrimaryKey(), this.heroDb, skillId,
					skillLvl);
			this.heroDb.getHeroSkills().put(hs.getTemplateId(), hs);
		} else {
			this.heroDb.getHeroSkills().get(skillId).setLevel(skillLvl);
		}
	}

	@Override
	public RoleHeroAwaken getRoleHeroAwaken() {
		return this.heroDb.getRoleHeroAwaken();
	}

	@Override
	public void setRoleHeroAwaken(RoleHeroAwaken roleHeroAwaken) {
		roleHeroAwaken.setHero(heroDb);
		roleHeroAwaken.setHero_id(heroDb.getId());
		this.heroDb.setRoleHeroAwaken(roleHeroAwaken);
	}

	@Override
	public FormationSummaryView getSummaryView(int index) {
		return new FormationSummaryView((byte) index, getTemplateId(), getQualityLevel(), getStar(), getLevel(),
				getShowColor().ordinal(), (byte) getBreakLevel(), isAwaken());
	}
}

class HeroAttachData {
	public PositionData posData;

	public AttendantData[] attendants;
}

class AttendantData {
	public int pos;

	public String heroId;
}

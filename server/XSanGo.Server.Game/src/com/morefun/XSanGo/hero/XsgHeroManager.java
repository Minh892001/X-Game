/**
 * 
 */
package com.morefun.XSanGo.hero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.XSanGo.Protocol.DuelSkillTemplateView;
import com.XSanGo.Protocol.GrowableProperty;
import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.QualityColor;
import com.morefun.XSanGo.battle.DuelSkillT;
import com.morefun.XSanGo.battle.DuelUtil;
import com.morefun.XSanGo.common.BattlePropertyMap;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.db.game.HeroPractice;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.item.AbsItemT;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.CollectionUtil;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 武将全局管理类
 * 
 * @author sulingyun
 * 
 */
public class XsgHeroManager {
	private static XsgHeroManager instance = new XsgHeroManager();

	public static XsgHeroManager getInstance() {
		return instance;
	}

	private HashMap<Integer, HeroT> heroTemplateMap;
	/** 主公升级经验表 */
	private Map<Integer, HeroLevelupExpT> levelupExpMap;
	/** 缘分信息 */
	private HashMap<Integer, RelationT> relationMap;
	/** 属性名称字典 */
	private Map<String, String> propertyMapTMap;
	/** 武将升星配置，外层KEY为颜色等级，内层KEY为星级 */
	private Map<Integer, Map<Integer, HeroStarT>> starMap;
	/** 武将进阶配置，外层KEY为武将ID，内层KEY为品阶等级 */
	private Map<Integer, Map<Integer, HeroQualityT>> heroQualityTemplateMap;
	private Map<String, Float> battlePowerPropertyMap;
	private Map<Integer, HeroSkillLevelupT> heroSkillLevelupMap;
	private Map<Integer, BuyHeroSkillT> buyHeroSkillTMap;
	private Map<Integer, HeroDialogT> dialogTMap;
	private Map<Integer, HeroSkillT> heroSkillTMap;
	private Map<Integer, DuelSkillT> duelSkillTMap;
	private Map<Integer, MonsterModeT> monsterModeTMap;
	private Map<Integer, HeroQualityProduceConsumeT> heroQualityLevelUpMap;
	/**
	 * 修炼属性
	 */
	private Map<Integer, PracticePropT> practicePropMap;
	/**
	 * 修炼开放等级
	 */
	private List<OpenLevelT> openLevelTs;
	/**
	 * 颜色经验
	 */
	private Map<Integer, HeroExpT> heroExpMap;
	/** 突破消耗 */
	private Map<Integer, HeroBreakConsumeT> heroBreakUpConsumeTMap;
	/** 突破属性 */
	private Map<Integer, Map<Integer, HeroBreakT>> heroBreakTMap;
	/** 重置随从消耗 */
	private List<AttendantCostT> attendantCostTs;
	/** 重置随从对象 */
	private Map<Integer, ResetAttendantT> attendantsTMap;
	/** 武将默认的随从配置 */
	private Map<Integer, AttendantT[]> defaultAttendantsTMap;

	private XsgHeroManager() {
		this.heroSkillTMap = CollectionUtil.toMap(ExcelParser.parse(HeroSkillT.class), "id");
		this.duelSkillTMap = CollectionUtil.toMap(ExcelParser.parse(DuelSkillT.class), "id");
		this.monsterModeTMap = CollectionUtil.toMap(ExcelParser.parse(MonsterModeT.class), "id");

		// 品阶配置
		this.heroQualityTemplateMap = new HashMap<Integer, Map<Integer, HeroQualityT>>();
		for (HeroQualityT hqt : ExcelParser.parse(HeroQualityT.class)) {
			if (!this.heroQualityTemplateMap.containsKey(hqt.heroId)) {
				this.heroQualityTemplateMap.put(hqt.heroId, new HashMap<Integer, HeroQualityT>());
			}

			this.heroQualityTemplateMap.get(hqt.heroId).put(hqt.quality, hqt);
		}
		// 进阶消耗及产出
		this.heroQualityLevelUpMap = CollectionUtil.toMap(ExcelParser.parse(HeroQualityProduceConsumeT.class),
				"quality");

		// 星级配置
		this.starMap = new HashMap<Integer, Map<Integer, HeroStarT>>();
		List<HeroStarT> starList = ExcelParser.parse(HeroStarT.class);
		for (HeroStarT starT : starList) {
			if (starT.open != 1) {
				continue;
			}
			int key = starT.color;
			if (!this.starMap.containsKey(key)) {
				this.starMap.put(key, new HashMap<Integer, HeroStarT>());
			}

			this.starMap.get(key).put(starT.star, starT);
		}

		// 缘分数据
		List<RelationT> relationList = ExcelParser.parse(RelationT.class);
		this.relationMap = new HashMap<Integer, RelationT>();
		for (RelationT relationT : relationList) {
			relationMap.put(relationT.id, relationT);
		}

		this.heroTemplateMap = new HashMap<Integer, HeroT>();
		List<HeroT> list = ExcelParser.parse(HeroT.class);
		for (HeroT heroT : list) {
			// 读取并设置缘分配置信息
			List<RelationT> heroRelationList = new ArrayList<RelationT>();
			String[] idArray = heroT.referenceRelationIds.split(",");
			for (String id : idArray) {
				heroRelationList.add(relationMap.get(NumberUtil.parseInt(id)));
			}

			idArray = heroT.relationFromBreak.split(",");
			for (String id : idArray) {
				heroRelationList.add(relationMap.get(NumberUtil.parseInt(id)));
			}

			heroT.setRelations(heroRelationList.toArray(new RelationT[0]));
			this.heroTemplateMap.put(heroT.id, heroT);
		}

		defaultAttendantsTMap = new HashMap<Integer, AttendantT[]>();
		for (AttendantContainerT container : ExcelParser.parse(AttendantContainerT.class)) {
			defaultAttendantsTMap.put(container.heroId, container.attendants);
		}
		List<AttendantContainerT> containerList = ExcelParser.parse(AttendantContainerT.class);
		defaultAttendantsTMap = new HashMap<Integer, AttendantT[]>();
		for (AttendantContainerT container : ExcelParser.parse(AttendantContainerT.class)) {
			defaultAttendantsTMap.put(container.heroId, container.attendants);
		}
		for (AttendantContainerT container : containerList) {
			this.heroTemplateMap.get(container.heroId).setAttendants(container.attendants);
		}

		// 武将升级经验表
		this.levelupExpMap = new HashMap<Integer, HeroLevelupExpT>();
		List<HeroLevelupExpT> expTable = ExcelParser.parse("武将升级经验表", HeroLevelupExpT.class);
		for (HeroLevelupExpT expT : expTable) {
			this.levelupExpMap.put(expT.level, expT);
		}

		// 战斗力公式
		this.battlePowerPropertyMap = new HashMap<String, Float>();
		this.propertyMapTMap = new HashMap<String, String>();
		for (BattlePowerT bpt : ExcelParser.parse(BattlePowerT.class)) {
			this.battlePowerPropertyMap.put(bpt.code, bpt.rank);
			this.propertyMapTMap.put(bpt.name, bpt.code);
		}

		// 技能升级花费
		this.heroSkillLevelupMap = new HashMap<Integer, HeroSkillLevelupT>();
		for (HeroSkillLevelupT hslt : ExcelParser.parse(HeroSkillLevelupT.class)) {
			this.heroSkillLevelupMap.put(hslt.level, hslt);
		}

		// 武将技能购买配置
		this.buyHeroSkillTMap = new HashMap<Integer, BuyHeroSkillT>();
		for (BuyHeroSkillT bhst : ExcelParser.parse(BuyHeroSkillT.class)) {
			this.buyHeroSkillTMap.put(bhst.count, bhst);
		}

		// 对白
		this.dialogTMap = new HashMap<Integer, HeroDialogT>();
		for (HeroDialogT hdt : ExcelParser.parse(HeroDialogT.class)) {
			this.dialogTMap.put(hdt.id, hdt);
		}

		// 修炼属性
		this.practicePropMap = new HashMap<Integer, PracticePropT>();
		for (PracticePropT p : ExcelParser.parse(PracticePropT.class)) {
			p.propName = this.translatePropertyCode(p.propName);
			if (p.propName != null) {
				this.practicePropMap.put(p.id, p);
			}
		}
		openLevelTs = ExcelParser.parse(OpenLevelT.class);
		// 颜色经验
		this.heroExpMap = new HashMap<Integer, HeroExpT>();
		for (HeroExpT h : ExcelParser.parse(HeroExpT.class)) {
			this.heroExpMap.put(h.color, h);
		}

		// 突破
		this.heroBreakUpConsumeTMap = CollectionUtil.toMap(ExcelParser.parse(HeroBreakConsumeT.class), "breakLevel");
		this.heroBreakTMap = new HashMap<Integer, Map<Integer, HeroBreakT>>();
		for (HeroBreakT hbt : ExcelParser.parse(HeroBreakT.class)) {
			if (!this.heroBreakTMap.containsKey(hbt.heroId)) {
				this.heroBreakTMap.put(hbt.heroId, new HashMap<Integer, HeroBreakT>());
			}

			this.heroBreakTMap.get(hbt.heroId).put(hbt.breakLevel, hbt);
		}

		// 读取随从重置消耗配置
		attendantCostTs = ExcelParser.parse(AttendantCostT.class);
		// 读取随机重置随从配置
		this.attendantsTMap = new HashMap<Integer, ResetAttendantT>();
		for (ResetAttendantT att : ExcelParser.parse(ResetAttendantT.class)) {
			this.attendantsTMap.put(att.itemId, att);
		}
	}

	public IHeroControler createHeroControler(IRole rt, Role db) {
		return new HeroControler(rt, db);
	}

	public HeroT getHeroT(int i) {
		HeroT result = this.heroTemplateMap.get(i);
		if (result == null || result.open == 0) {
			return null;
		}

		return result;
	}

	/**
	 * 获取武将升级所需经验，满级或无法匹配脚本则返回0
	 * 
	 * @param currentLevel
	 *            武将的当前等级
	 * @return
	 */
	public int getLevelupExp(int currentLevel) {
		int nextLevel = currentLevel + 1;
		return this.levelupExpMap.containsKey(nextLevel) ? this.levelupExpMap.get(nextLevel).exp : 0;
	}

	/**
	 * 查找缘分模板数据
	 * 
	 * @param templateId
	 * @return
	 */
	public RelationT findRelationT(int templateId) {
		return this.relationMap.get(templateId);
	}

	/**
	 * 翻译属性表，把脚本中的中文属性用程序属性表示
	 * 
	 * @param input
	 * @return
	 */
	public BattlePropertyMap translatePropertyMap(BattlePropertyMap input) {
		BattlePropertyMap out = new BattlePropertyMap();
		for (Entry<String, GrowableProperty> entry : input) {
			String code = this.translatePropertyCode(entry.getKey());
			if (TextUtil.isBlank(code)) {
				continue;
			}

			out.combine(code, entry.getValue().value);
		}

		return out;
	}

	/**
	 * 翻译属性代码，如果为中文属性则翻译为对应英文代码，如果为已配置的英文代码，则原样返回
	 * 
	 * @param code
	 * @return
	 */
	public String translatePropertyCode(String code) {
		if (this.propertyMapTMap.containsKey(code)) {
			return this.propertyMapTMap.get(code);
		}
		if (this.propertyMapTMap.containsValue(code)) {
			return code;
		}

		LogManager.error(new IllegalStateException(TextUtil.format("[{0}]找不到对应的属性！", code)));
		return null;
	}

	/**
	 * 计算召唤一个指定星级武将需要的魂魄数量
	 * 
	 * @param color
	 * @param star
	 * @return
	 */
	public int caculateSoulCountForSummon(int color, byte star) {
		int count = 0;
		Map<Integer, HeroStarT> map = this.starMap.get(color);
		if (map != null) {
			for (int i = 1; i <= star; i++) {
				if (map.containsKey(i)) {
					count += map.get(i).soulCount;
				}
			}
		}

		return count;
	}

	/**
	 * 计算召唤一个指定星级武将需要的金币数量
	 * 
	 * @param color
	 * @param star
	 * @return
	 */
	public int caculateJinbiForSummon(int color, byte star) {
		int jinbi = 0;
		Map<Integer, HeroStarT> map = this.starMap.get(color);
		if (map != null) {
			for (int i = 1; i <= star; i++) {
				if (map.containsKey(i)) {
					jinbi += map.get(i).jinbi;
				}
			}
		}

		return jinbi;
	}

	public HeroQualityT findHeroColorT(int heroId, int quality) {
		Map<Integer, HeroQualityT> subMap = this.heroQualityTemplateMap.get(heroId);
		HeroQualityT result = subMap == null ? null : subMap.get(quality);
		if (result == null) {
			LogManager.warn(TextUtil.format("无法匹配武将{0}的{1}级进阶数据", heroId, quality));
		}
		return result;
	}

	public HeroStarT findHeroStarT(int color, int star) {
		if (this.starMap.containsKey(color)) {
			if (this.starMap.get(color).containsKey(star)) {
				return this.starMap.get(color).get(star);
			}
		}

		return null;
	}

	/**
	 * 获取对应武将的将魂模板编号
	 * 
	 * @param heroTemplateId
	 * @return
	 */
	public String getSoulTemplateId(int heroTemplateId) {
		return "s" + heroTemplateId;
	}

	/**
	 * 获取对应将魂的武将编号
	 * 
	 * @param heroTemplateId
	 * @return
	 */
	private int getSoulTemplateId(String soulTemplateId) {
		return NumberUtil.parseInt(soulTemplateId.substring(1));
	}

	public HeroLevelupExpT findHeroLevelupExpT(int level) {
		return this.levelupExpMap.get(level);
	}

	/**
	 * 计算战力
	 * 
	 * @param propertyMap
	 *            战斗属性集合
	 * @return
	 */
	public int caculateBattlePower(BattlePropertyMap propertyMap, BattlePropertyHolder holder) {
		return this.caculateBattlePower(propertyMap.getProperties(), holder);
	}

	public int caculateBattlePower(GrowableProperty[] properties, BattlePropertyHolder holder) {
		List<String> growList = new ArrayList<String>();
		growList.add(Const.PropertyName.Hero.CRIT_RATE);
		growList.add(Const.PropertyName.Hero.Decrit_Rate);
		growList.add(Const.PropertyName.Hero.Def_Poro);
		growList.add(Const.PropertyName.Hero.Magic_Def_Poro);

		float power = 0;
		for (GrowableProperty p : properties) {
			String code = this.translatePropertyCode(p.code);
			if (TextUtil.isBlank(code) || !this.battlePowerPropertyMap.containsKey(code)) {
				continue;
			}

			float rate = this.battlePowerPropertyMap.get(code);
			// 特例,二级/百分比属性只计算扩展值，该规则只对武将有效
			if (holder == BattlePropertyHolder.Hero && growList.contains(code) && (p instanceof GrowableProperty)) {
				power += ((GrowableProperty) p).grow * rate;
				continue;
			}

			if (p instanceof GrowableProperty) {
				power += (p.value + ((GrowableProperty) p).grow) * rate;
			} else {
				power += p.value * rate;
			}
		}

		return (int) power;
	}

	public HeroSkillLevelupT getHeroSkillLevelupT(int level) {
		return this.heroSkillLevelupMap.get(level);
	}

	public BuyHeroSkillT findBuyHeroSkillT(int count) {
		return this.buyHeroSkillTMap.get(count);
	}

	/**
	 * 根据魂魄模板计算召唤需要数量
	 * 
	 * @param soulTemplateId
	 * @return
	 */
	public int caculateSoulCountForSummonFromSoulTemplate(String soulTemplateId) {
		HeroT heroT = this.getHeroT(this.getSoulTemplateId(soulTemplateId));
		return this.caculateSoulCountForSummon(heroT.color, heroT.star);
	}

	public HeroDialogT getHeroDialogT(int heroId) {
		return this.dialogTMap.get(heroId);
	}

	/**
	 * 获得已有卡牌时候转化的魂魄数量
	 * 
	 * @param star
	 * @return
	 */
	public int caculateSoulCountForCardTransform(int color) {
		return this.caculateSoulCountForSummon(color, (byte) 1) / 3;
	}

	public String[] getPropertyConfig() {
		List<String> list = new ArrayList<String>();
		for (String key : this.propertyMapTMap.keySet()) {
			list.add(this.propertyMapTMap.get(key));
			list.add(key);
		}
		return list.toArray(new String[0]);
	}

	public IntString[] getRelationConfig() {
		List<IntString> list = new ArrayList<IntString>();
		for (int key : this.relationMap.keySet()) {
			list.add(new IntString(key, this.relationMap.get(key).name));
		}
		return list.toArray(new IntString[0]);
	}

	public IntString[] getHeroSkillConfig() {
		List<IntString> list = new ArrayList<IntString>();
		for (HeroSkillT hst : this.heroSkillTMap.values()) {
			list.add(new IntString(hst.id, hst.name));
		}

		return list.toArray(new IntString[0]);
	}

	public DuelSkillT findDuelSkillT(int duelSkill) {
		return this.duelSkillTMap.get(duelSkill);
	}

	public MonsterModeT findMonsterTemplate(int templateId) {
		return this.monsterModeTMap.get(templateId);
	}

	public HeroSkillT findHeroSKillT(int templateId) {
		return this.heroSkillTMap.get(templateId);
	}

	/**
	 * 组织带前缀的武将名
	 * 
	 * @param colorLevel
	 *            品质等级
	 * @param breakLevel
	 * @param name
	 *            模板名字
	 * @return
	 */
	public String orgnizeHeroName(int colorLevel, int breakLevel, String name) {
		String result = name;
		if (breakLevel > 0) {
			result = this.findBreakLevelUpT(breakLevel).prefix + result;
		} else if (colorLevel > 0) {
			result += ("+" + colorLevel);
		}

		return result;
	}

	public DuelSkillTemplateView[] getDuelStrategyConfig() {
		List<DuelSkillTemplateView> list = new ArrayList<DuelSkillTemplateView>();
		for (DuelSkillT template : this.duelSkillTMap.values()) {
			list.add(DuelUtil.createDuelSkillTemplateView(template));
		}

		return list.toArray(new DuelSkillTemplateView[0]);
	}

	/**
	 * 获取进阶消耗和奖励配置
	 * 
	 * @param level
	 * @return
	 */
	public HeroQualityProduceConsumeT findQualityLevelUpT(int level) {
		return this.heroQualityLevelUpMap.get(level);
	}

	/**
	 * 获取武将升到某一级需要的总经验值
	 */
	public long caculateTotalExp(int level) {
		long sum = 0L;
		for (int i = 0; i < level; i++) {
			sum += getLevelupExp(i);
		}
		return sum;
	}

	/**
	 * 计算武将进阶消耗的功勋
	 * 
	 * @param quality
	 *            进阶等级
	 */
	public int caculateTotalQualityUpMedConsume(int quality) {
		int sum = 0;
		for (int i = 1; i <= quality; i++) {
			HeroQualityProduceConsumeT pct = XsgHeroManager.getInstance().findQualityLevelUpT(i);
			sum += pct.med1Count;
		}
		return sum;
	}

	/**
	 * 计算武将进阶消耗的金币
	 * 
	 * @param quality
	 *            进阶等级
	 */
	public int caculateTotalQualityUpJinbiConsume(int quality) {
		int sum = 0;
		for (int i = 1; i <= quality; i++) {
			HeroQualityProduceConsumeT pct = XsgHeroManager.getInstance().findQualityLevelUpT(i);
			sum += pct.jinbi;
		}
		return sum;
	}

	/**
	 * 计算升星消耗的金币
	 * 
	 * @param color
	 *            武将颜色
	 * @param star
	 *            星级
	 */
	public int caculateTotalStarUpJinbiConsume(int color, int star) {
		int sum = 0;
		for (int i = 1; i <= star; i++) {
			HeroStarT starT = findHeroStarT(color, i);
			sum += starT.jinbi;
		}
		return sum;
	}

	/**
	 * 计算升星消耗的将魂
	 * 
	 * @param color
	 *            武将颜色
	 * @param star
	 *            星级
	 */
	public int caculateTotalStarUpSoulConsume(int color, int star) {
		int sum = 0;
		for (int i = 2; i <= star; i++) {
			HeroStarT starT = findHeroStarT(color, i);
			sum += starT.soulCount;
		}
		return sum;
	}

	/**
	 * 计算技能升级消耗的金币
	 * 
	 * @param pos
	 *            技能位置
	 * @param startLvl
	 *            开始等级
	 * @param endLvl
	 *            结束等级
	 */
	public int caculateTotalSkillJinbiConsume(int pos, int startLvl, int endLvl) {
		int sum = 0;
		for (int i = startLvl + 1; i <= endLvl; i++) {
			HeroSkillLevelupT t = getHeroSkillLevelupT(i);
			sum += t.conditions[pos].jinbi;
		}
		return sum;
	}

	/**
	 * 计算修炼所消耗的功勋
	 * 
	 * @param level
	 *            修炼等级
	 * @param id
	 *            修炼属性ID
	 * */
	public int caculateTotalPracticeGXConsume(int level, int id) {
		PracticePropT propT = XsgHeroManager.getInstance().getPracticePropMap().get(id);
		int sum = 0;
		// 初始等级为1级,第一次修炼就从2级开始，所以这里从2级开始累加
		for (int i = 2; i <= level; i++) {
			sum += propT.baseExploit * Math.pow(1 + (double) propT.exploitPercent / 100, i - 2);
		}
		return sum;
	}

	/**
	 * 计算修炼所需要的经验
	 * */
	public int caculateTotalPracticeExpConsume(HeroPractice hp) {
		PracticePropT propT = XsgHeroManager.getInstance().getPracticePropMap().get(hp.getScriptId());
		int sum = (hp.getLevel() >= propT.maxLevel) ? 0 : hp.getExp();
		// 初始等级为1级,第一次修炼就从2级开始，所以这里从2级开始累加
		int nextExp = propT.baseExp;
		for (int i = 2; i <= hp.getLevel(); i++) {
			sum += nextExp;
			nextExp = nextExp + nextExp * propT.expPercent / 100;
		}
		return sum;
	}

	/**
	 * 经验转换为黑龙王將魂
	 * */
	public int convertBlackDragonSoul(int practiceExp) {
		// 折算黑龙王將魂
		AbsItemT itemT = XsgItemManager.getInstance().findAbsItemT(Const.PropertyName.BlackDragonSoul);
		if (itemT != null && practiceExp > 0) {
			HeroExpT expT = XsgHeroManager.getInstance().getHeroExpT(itemT.getColor().value());
			if (expT != null && expT.exp > 0) {
				int num = (practiceExp + expT.exp - 1) / expT.exp;
				return num;
			}
		}
		return 0;
	}

	public List<OpenLevelT> getOpenLevelList() {
		return this.openLevelTs;
	}

	public Map<Integer, PracticePropT> getPracticePropMap() {
		return this.practicePropMap;
	}

	public HeroExpT getHeroExpT(int color) {
		return this.heroExpMap.get(color);
	}

	/**
	 * 查找修炼消耗配置
	 * 
	 * @param breakLevel
	 * @return
	 */
	public HeroBreakConsumeT findBreakLevelUpT(int breakLevel) {
		return this.heroBreakUpConsumeTMap.get(breakLevel);
	}

	/**
	 * 查找武将突破
	 * 
	 * @param templateId
	 * @param breakLevel
	 * @return
	 */
	public HeroBreakT findHeroBreakT(int templateId, int breakLevel) {
		Map<Integer, HeroBreakT> subMap = this.heroBreakTMap.get(templateId);
		HeroBreakT result = subMap == null ? null : subMap.get(breakLevel);

		return result;
	}

	/**
	 * 获取武将显示颜色
	 * 
	 * @param templateId
	 * @param advance
	 *            进阶
	 * @return
	 */
	public QualityColor getShowColor(int templateId, int advance) {
		HeroT heroT = XsgHeroManager.getInstance().getHeroT(templateId);
		int color = heroT.color;
		// 绿将变蓝色的进阶等级
		final int g2bLevel = XsgGameParamManager.getInstance().getHeroG2BLevel();
		// 蓝将变紫色的进阶等级
		final int b2pLevel = XsgGameParamManager.getInstance().getHeroB2PLevel();
		// 绿色武将+5变成蓝色,+8变成紫色
		if (color == QualityColor.Green.ordinal()) {
			if (advance >= g2bLevel) {
				color = QualityColor.Blue.ordinal();
			}
			if (advance >= b2pLevel) {
				color = QualityColor.Violet.ordinal();
			}
		} else if (color == QualityColor.Blue.ordinal()) {
			// 蓝色武将+5变成紫色
			if (advance >= g2bLevel) {
				color = QualityColor.Violet.ordinal();
			}
		}
		return QualityColor.valueOf(color);
	}

	public List<AttendantCostT> getAttendantCostTs() {
		return attendantCostTs;
	}

	public Map<Integer, ResetAttendantT> getAttendantsTs() {
		return attendantsTMap;
	}

	public AttendantT[] getDefaultAttendants(int heroCode) {
		return defaultAttendantsTMap.get(heroCode);
	}

	/**
	 * 获取所有武将模板
	 * 
	 * @return
	 */
	public Map<Integer, HeroT> getAllHeroT() {
		return this.heroTemplateMap;
	}
}

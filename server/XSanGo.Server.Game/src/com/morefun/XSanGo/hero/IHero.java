/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.hero;

import java.util.List;

import com.XSanGo.Protocol.EquipPosition;
import com.XSanGo.Protocol.FormationSummaryView;
import com.XSanGo.Protocol.HeroConsumeView;
import com.XSanGo.Protocol.HeroEquipView;
import com.XSanGo.Protocol.HeroPracticeView;
import com.XSanGo.Protocol.HeroState;
import com.XSanGo.Protocol.HeroView;
import com.XSanGo.Protocol.IntIntPair;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.QualityColor;
import com.morefun.XSanGo.battle.DuelUnit;
import com.morefun.XSanGo.db.game.HeroPractice;
import com.morefun.XSanGo.db.game.HeroSkill;
import com.morefun.XSanGo.db.game.RoleHeroAwaken;
import com.morefun.XSanGo.equip.EquipItem;
import com.morefun.XSanGo.formation.IFormation;
import com.morefun.XSanGo.partner.IPartner;

/**
 * 武将操作接口
 * 
 * @author Su LingYun
 * 
 */
public interface IHero {

	/**
	 * 获取武将视图信息
	 * 
	 * @return
	 */
	HeroView getHeroView();
	
	/**
	 * 获取武将视图信息
	 * 
	 * @param isCalculationTempProp 是否计算临时属性
	 * @return 
	 */
	HeroView getHeroView(boolean isCalculationTempProp);

	/**
	 * 获取唯一标识
	 * 
	 * @return
	 */
	String getId();

	/**
	 * 设置武将位置数据
	 * 
	 * @param pd
	 */
	void setHeroPositionData(PositionData pd);

	PositionData getHeroPositionData();

	int getTemplateId();

	/**
	 * 设置装备
	 * 
	 * @param ep
	 *            装备位置
	 * @param equip
	 *            装备物品，可为null
	 */
	void setHeroEquip(EquipPosition ep, EquipItem equip);

	/**
	 * 获取指定位置的装备
	 * 
	 * @param equipPos
	 * @return
	 */
	EquipItem getEquipByPos(EquipPosition equipPos);

	/**
	 * 获取品质
	 * 
	 * @return
	 */
	QualityColor getColor();
	
	/**
	 * 获取显示颜色,用于武将进阶之后变色
	 * */
	QualityColor getShowColor();

	/**
	 * 是否带了随从
	 * 
	 * @return
	 */
	boolean hasAttendant();

	/**
	 * 获取指定位置的随从，没有则返回NULL
	 * 
	 * @param pos
	 * @return
	 */
	IHero getAttendantByPos(byte pos);

	/**
	 * 设置指定位置随从
	 * 
	 * @param pos
	 * @param attendant
	 */
	void setAttendant(byte pos, IHero attendant);

	/**
	 * 获取战斗力
	 * 
	 * @return
	 */
	int getBattlePower();

	/**
	 * 获取武将星级
	 * 
	 * @return
	 */
	byte getStar();

	/**
	 * 升星
	 */
	void starUp();

	/**
	 * 进阶
	 */
	void colorUp();

	/**
	 * 武将名字
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 能否装备指定物品
	 * 
	 * @param equip
	 * @return
	 */
	boolean canEquip(EquipItem equip);

	/**
	 * 初始化随从数据，因为随从本身也是武将，如果直接在构造函数中初始化会有顺序问题，所以单独调用
	 */
	void initAttendant();

	/**
	 * 获得武将等级
	 * 
	 * @return
	 */
	int getLevel();

	/**
	 * 获得武将经验
	 * 
	 * @param exp
	 */
	void winExp(int exp);

	/**
	 * 获取武将经验值
	 * */
	int getExp();
	
	/**
	 * 设置武将经验值
	 * */
	void setExp(int exp);

	/**
	 * 获取武将模板
	 * 
	 * @return
	 */
	HeroT getTemplate();

	/**
	 * 是否随从
	 * 
	 * @return
	 */
	boolean isAttendant();

	/**
	 * 获取技能数据
	 * 
	 * @param skillId
	 * @return
	 */
	HeroSkill getHeroSkill(int skillId);
	
	/**
	 * 设置技能等级
	 * */
	boolean setHeroSkill(int skillId, int level);

	/**
	 * 创建单挑战斗单位
	 * 
	 * @return
	 */
	DuelUnit createDuelUnit();

	/**
	 * 初始化技能数据
	 */
	void initSkill();

	/**
	 * 等级经验是否已满
	 * 
	 * @return
	 */
	boolean isExpFull();

	/**
	 * 此方法直接设置武将等级，正常游戏逻辑不应调用
	 * 
	 * @param level
	 */
	void setLevel(int level);

	/**
	 * 此方法直接设置武将星级，正常游戏逻辑不应调用
	 * 
	 * @param level
	 */
	void setStar(int star);

	/**
	 * 此方法直接设置武将品质，正常游戏逻辑不应调用
	 * 
	 * @param level
	 */
	void setColor(int color);

	/**
	 * 获取所处阵型，没有则返回NULL
	 * 
	 * @return
	 */
	IFormation getReferenceFormation();

	/**
	 * 获取主人武将，当自身非随从时返回NULL
	 * 
	 * @return
	 */
	IHero getMaster();

	/**
	 * 获取武将品质等级
	 * 
	 * @return
	 */
	int getQualityLevel();

	/**
	 * 获取缘分等级
	 * 
	 * @param orignalRelationId
	 *            武将的初始缘分
	 * @return
	 */
	int getRelationLevel(int orignalRelationId);

	/**
	 * 升级指定模板缘分
	 * 
	 * @param orignalRelationId
	 *            武将的初始缘分
	 * @param level
	 */
	void setRelationLevel(int orignalRelationId, int level);

	/**
	 * 获取武将升级,进阶,技能点消耗
	 * */
	HeroConsumeView getHeroConsumeView();

	/**
	 * 武将重置
	 * 
	 * @param level
	 *            重置到等级level, -1表示等级不变
	 * @param star
	 *            星级, -1表示星级不变
	 * @param color
	 *            进阶, -1 表示进阶不变
	 * @param resetSkill
	 *            重置技能
	 * @param breakLevel
	 *            重置突破等级, -1 表示维持不变
	 * @param resetPractice
	 *            重置修炼属性
	 * @param practiceToInit
	 *            修炼属性充值到1级状态
	 */
	void resetHero(int level, int star, int color, boolean resetSkill, int breakLevel, boolean resetPractice,
			boolean practiceToInit);

	/**
	 * 获取武将的上阵状态
	 */
	HeroState getState();
	
	/**
	 * 获取武将对应的伙伴信息
	 */
	public IPartner getPartner();

	/**
	 * 获取武将修炼view
	 * 
	 * @return
	 */
	List<HeroPracticeView> getHeroPracticeView();

	/**
	 * 增加修炼属性
	 * 
	 * @param heroPractice
	 */
	void addHeroPractice(HeroPractice heroPractice);

	/**
	 * 根据脚本ID查找修炼的属性
	 * 
	 * @param scriptId
	 * @return
	 */
	HeroPractice getHeroByScriptId(int scriptId);

	/**
	 * 移除修炼属性
	 * 
	 * @param scriptId
	 */
	void removeHeroPractice(int scriptId);
	
	/**
	 * 移除所有修炼属性
	 * */
	void removeAllHeroPractice();
	
	/**
	 * 所有修炼属性重置为1级
	 * */
	void resetAllHeroPractice();

	/**
	 * 是否包涵修炼的属性
	 * 
	 * @param prop
	 * @return
	 */
	boolean hasProp(String propName);

	/**
	 * 获取突破等级
	 * 
	 * @return
	 */
	int getBreakLevel();

	/**
	 * 突破
	 */
	void breakLevelUp();
	
	/**
	 * 设置突破等级
	 * */
	void setBreakLevel(int lvl);
	
	/**
	 * 获取已修炼属性数量
	 * @return
	 */
	int getPracticeSize();
	
	/**
	 * 获取武将技能
	 * */
	IntIntPair[] getSkills();
	/**
	 * 获取随从配置
	 * @return
	 */
	XsgAttendant[] getAttentantsConfig();
	/**
	 * 设置专属随从id
	 * @param itemId 随从武将模版id
	 * @param pos 随从位置
	 */
	void setSpecialAttendantId(int itemId, byte pos);
	
	/**
	 * 获取武将的装备明细
	 * 
	 * @return
	 */
	List<HeroEquipView> getEquipDetails();
	
	/**
	 * 查看其他玩家，获取武将身上的装备View
	 * 
	 * @return
	 */
	ItemView[] getHeroEquipViews();

	/**
	 * 是否已觉醒
	 * 
	 * @return
	 */
	boolean isAwaken();
	
	/**
	 * 获取重置后的特殊随从配置
	 */
	String getResetSpecAttendants();

	/**
	 * 新增技能
	 * 
	 * @param skillId
	 * @param skillLvl
	 */
	void addSkill(int skillId, int skillLvl);

	/**
	 * 获得武将觉醒数据
	 * 
	 * @return
	 */
	RoleHeroAwaken getRoleHeroAwaken();

	/**
	 * 设置武将觉醒数据
	 * 
	 * @param roleHeroAwaken
	 */
	void setRoleHeroAwaken(RoleHeroAwaken roleHeroAwaken);
	
	/**
	 * 武将摘要信息
	 * 
	 * @param index
	 * @return 
	 */
	FormationSummaryView getSummaryView(int index); 
}

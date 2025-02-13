/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.hero;

import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.AttendantView;
import com.XSanGo.Protocol.HeroEquipView;
import com.XSanGo.Protocol.HeroPracticeView;
import com.XSanGo.Protocol.HeroView;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.OthersHeroView;
import com.morefun.XSanGo.common.HeroSource;
import com.morefun.XSanGo.equip.EquipItem;

/**
 * 武将控制接口
 * 
 * @author Su LingYun
 * 
 */
public interface IHeroControler {

	/**
	 * 获取武将视图数据
	 * 
	 * @return
	 */
	HeroView[] getHeroViewList();

	/**
	 * 获取指定武将
	 * 
	 * @param heroId
	 * @return
	 */
	IHero getHero(String heroId);

	/**
	 * 获取所有武将
	 * */
	List<IHero> getAllHero();

	/**
	 * 给指定武将装备物品
	 * 
	 * @param heroId
	 * @param equip
	 * @throws NoteException
	 */
	void setHeroEquip(String heroId, EquipItem equip) throws NoteException;

	/**
	 * 卸载指定武将的装备物品
	 * 
	 * @param heroId
	 * @param equip
	 * @throws NoteException
	 * */
	public void removeHeroEquip(String heroId, EquipItem equip) throws NoteException;

	/**
	 * 卸载指定武将的装备物品, 不发送变更通知
	 * 
	 * @param heroId
	 * @param equip
	 * @throws NoteException
	 * */
	public void removeHeroEquipNotNotify(String heroId, EquipItem equip) throws NoteException;

	/**
	 * 设置武将随从
	 * 
	 * @param heroId
	 *            武将ID
	 * @param pos
	 *            随从位置
	 * @param attendant
	 *            随从武将
	 * @throws NoteException
	 */
	void setHeroAttendant(String heroId, byte pos, IHero attendant) throws NoteException;

	/**
	 * 把武将从相应的引用位置移除，如阵型，随从
	 * 
	 * @param hero
	 * @param checkLastHero
	 *            是否进行最后一名上阵武将检查
	 * @param removeFromPosition
	 *            是否将其从相关引用对象中的移除
	 * @throws NoteException
	 */
	void removeReference(IHero hero, boolean checkLastHero, boolean removeFromPosition) throws NoteException;

	/**
	 * 武将升星
	 * 
	 * @param heroId
	 * @throws NoteException
	 * @throws NotEnoughMoneyException
	 */
	void starUp(String heroId) throws NoteException, NotEnoughMoneyException;

	/**
	 * 武将进阶，颜色品质升级
	 * 
	 * @param heroId
	 * @return 奖励物品列表
	 * @throws NoteException
	 * @throws NotEnoughMoneyException
	 */
	ItemView[] colorUp(String heroId) throws NoteException, NotEnoughMoneyException;

	/**
	 * 初始化武将的随从数据，由于随从本身就是武将，所以需要在controler构造完之后再单独调用
	 */
	void initHeroAttendant();

	/**
	 * 检查装备，随从等数据的一致性
	 */
	void checkData();

	/**
	 * 根据模板获取武将
	 * 
	 * @param templateId
	 * @return
	 */
	IHero getHero(int templateId);

	/**
	 * 新增武将
	 * 
	 * @param heroT
	 * @param source
	 * @return
	 */
	IHero addHero(HeroT heroT, HeroSource source);

	/**
	 * 增加武将经验
	 * 
	 * @param heroExpMap
	 *            KEY为武将ID，value为增加的经验值
	 */
	void winHeroExp(Map<String, Integer> heroExpMap);

	/**
	 * 升级武将 技能
	 * 
	 * @param heroId
	 * @param skillId
	 * @param upLevel
	 *            提升等级
	 * @throws NotEnoughMoneyException
	 * @throws NoteException
	 */
	void skillLevelUp(String heroId, int skillId, int upLevel) throws NotEnoughMoneyException, NoteException;

	/**
	 * 武将的数量
	 * 
	 * @return
	 */
	public int getHeroNum();

	/**
	 * 达到指定星级的武将 数量
	 * 
	 * @return
	 */
	public int getHeroNumWhenStarGreaterThan(int star);

	/**
	 * 任意单挑武将
	 * 
	 * 
	 * @return
	 */
	IHero anyDuelHero();

	/**
	 * 刷新技能点数据
	 */
	void refreshHeroSkillData();

	/**
	 * 删除武将
	 * 
	 * @param heroId
	 *            武将ID
	 * */
	void removeHero(String heroId);

	/**
	 * 增加技能点
	 * */
	void addHeroSkillPoint(int num);

	/**
	 * 获取武将修炼信息
	 * 
	 * @param heroId
	 * @return
	 * @throws NoteException
	 */
	HeroPracticeView[] getHeroPracticeList(String heroId) throws NoteException;

	/**
	 * 重置修炼
	 * 
	 * @param heroId
	 * @param id
	 * @return
	 * @throws NoteException
	 */
	HeroPracticeView[] resetPractice(String heroId, int id) throws NoteException;

	/**
	 * 修炼
	 * 
	 * @param heroId
	 * @param id
	 * @param itemIds
	 * @throws NoteException
	 */
	void practice(String heroId, int id, String itemIds) throws NoteException;

	/**
	 * 设置武将缘分等级
	 * 
	 * @param heroId
	 * @param orignalRelationId
	 * @param level
	 * @throws NoteException
	 * @throws NotEnoughMoneyException
	 */
	void setRelationLevel(String heroId, int orignalRelationId, int level) throws NoteException,
			NotEnoughMoneyException;

	/**
	 * 重置武将的专属随从
	 * 
	 * @param heroId
	 *            当前武将id
	 * @param pos
	 *            随从位
	 */
	AttendantView resetSpecialAttendant(String heroId, byte pos) throws NoteException;

	/**
	 * 获取武将装备明细
	 * 
	 * @return
	 */
	List<HeroEquipView> getEquipDetails();

	/**
	 * 查看其他玩家，获取武将身上的装备View
	 * 
	 * @return
	 */
	OthersHeroView getHerosEquips();
}
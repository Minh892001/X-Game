/**
 * 
 */
package com.morefun.XSanGo.item;

import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.EquipLevelEntity;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.db.game.RoleItem;
import com.morefun.XSanGo.equip.EquipItem;

/**
 * 物品管理器操作接口
 * 
 * @author sulingyun
 * 
 */
public interface IItemControler {

	/**
	 * 获取背包中指定模板的物品数量
	 * 
	 * @param template
	 * @return
	 */
	int getItemCountInPackage(String template);

	/**
	 * 获取指定物品
	 * 
	 * @param itemId
	 * @return
	 */
	IItem getItem(String itemId);

	/**
	 * 获取所有装备列表
	 * 
	 * @return
	 */
	// List<EquipItem> getEquipList();

	/**
	 * 获取指定物品
	 * 
	 * @param id
	 * @param type
	 * @return
	 */
	<T> T getItem(String id, Class<T> type);

	/**
	 * 根据模板编号增减物品，适用奖励结算和消耗一些材料等场景
	 * 
	 * @param code
	 *            物品代码
	 * @param change
	 *            变更数量
	 */
	List<IItem> changeItemByTemplateCode(String code, int change);

	/**
	 * 根据模板编号 ,查询所有匹配装备
	 * 
	 * @param templetCode
	 * @return
	 */
	List<EquipItem> findEquipByTemplateCode(String code);

	/**
	 * 卖掉物品
	 * 
	 * @param id
	 * @param count
	 * @throws NoteException
	 */
	void sale(String id, int count) throws NoteException;

	/**
	 * 获取所有物品数据
	 * 
	 * @return
	 */
	ItemView[] getItemViewList();
	
	/**
	 * 获取所有装备数据 for GM
	 * 
	 * @return
	 */
	ItemView[] getItemEquipViewList4GM();

	/**
	 * 重铸装备
	 * 
	 * @param id
	 * @throws NoteException
	 */
	void rebuildEquip(String id) throws NoteException;

	/**
	 * 强化装备
	 * 
	 * @param id
	 * @throws NoteException
	 * @throws NotEnoughMoneyException
	 */
	void levelupEquip(String id) throws NoteException, NotEnoughMoneyException;

	/**
	 * 自动强化装备
	 * 
	 * @param id
	 * @throws NoteException
	 * @throws NotEnoughMoneyException
	 * */
	void levelupEquipAuto(String id) throws NoteException,
			NotEnoughMoneyException;

	/**
	 * 一键强化某个武将的所有装备
	 * 
	 * @param id
	 * @throws NoteException
	 * @throws NotEnoughMoneyException
	 * */
	EquipLevelEntity[] levelupEquipAll(String id) throws NoteException,
			NotEnoughMoneyException;

	/**
	 * 装备升星
	 * 
	 * @param id
	 * @param idArray
	 * @throws NoteException
	 * @throws NotEnoughMoneyException
	 */
	void starUpEquip(String id, String idArray, String items)
			throws NoteException, NotEnoughMoneyException;

	/**
	 * 获取指定编号的装备
	 * 
	 * @param id
	 * @return
	 */
	EquipItem getEquipItem(String id);

	/**
	 * 根据物品唯一标识变更数量，仅适用可叠加物品
	 * 
	 * @param id
	 * @param change
	 */
	void changeItemById(String id, int change);

	/**
	 * 使用物品，不带返回值
	 * 
	 * @param id
	 * @param count
	 * @param params
	 * @throws NoteException
	 */
	void useItem(String id, int count, String params) throws NoteException;

	/**
	 * 使用宝箱类物品，返回掉落物品数组
	 * 
	 * @param id
	 * @param count
	 * @return
	 * @throws NotEnoughMoneyException
	 * @throws NoteException
	 */
	ItemView[] useChestItem(String id, int count)
			throws NotEnoughMoneyException, NoteException;

	/**
	 * 是否背包满，暂时只用在副本挑战和抽卡中检查
	 * 
	 * @return
	 */
	boolean isPackageFull();

	/**
	 * 熔炼装备
	 * */
	ItemView[] smeltEquip(String idArrayStr) throws NoteException,
			NotEnoughMoneyException;

	/**
	 * 检查背包是否已满，是则抛出异常提示
	 */
	void checkPackageFull() throws NoteException;

	/**
	 * 直接增加一个数据库对象到物品容器
	 * 
	 * @param itemDb
	 * @return 运行时物品的内存对象
	 */
	IItem addItemFromDb(RoleItem itemDb);
	
	/**
	 * 道具开孔,镶嵌
	 * 
	 * @param equipId 装备id
	 * @param position 孔位置,从零开始计数
	 * */
	ItemView equipHole(String equipId, int position) throws NoteException;
	
	/**
	 * 镶嵌宝石
	 * 
	 * @param equipId 道具ID
	 * @param position 位置
	 * @param gemId 宝石模版ID
	 * */
	void setGem(String equipId, int position, String gemId) throws NoteException;
	
	/**
	 * 移除宝石
	 * 
	 * @param equipId 装备ID
	 * @param position 孔位置
	 * */
	void removeGem(String equipId, int position) throws NoteException;

	Map<String, Integer> removeAllGem(EquipItem item) throws NoteException;

	/**
	 * 销毁某个道具，即清除指定模版编号道具的所有数量
	 * 
	 * @param code
	 */
	void destroyItem(String code);
	
	/**
	 * 验证道具是否充足，目前支持元宝、金币、竞技币、拍卖币、技能点、背包道具的验证
	 * 
	 * @param code 物品模版编号
	 * @param itemNum 需要的数量
	 * @throws NoteException
	 * @throws NotEnoughMoneyException
	 * @throws NotEnoughYuanBaoException 
	 */
	void isItemNumEnough(String code,int itemNum) throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException;
	
	/**
	 * 领取复合宝箱道具
	 * 
	 * @param itemIndex 物品位置
	 * @param itemId 物品唯一编号
	 * @throws NoteException 
	 */
	void drawCompositeChestItem(int itemIndex, String itemId) throws NoteException;
	
	/**
	 * 获取物品对应品质的颜色色值
	 * 
	 * @param quality
	 * @return 
	 */
	String getItemColor(int quality);
	
	/**
	 * 返回物品背包集合，只作为获取物品数据，如需进行移除操作，无效；禁止针对物品对象数据修改
	 * 
	 * @return 
	 */
	List<IItem> getItemList();
	
	/**
	 * 选择阵法进阶类型
	 * @param type
	 * @throws NoteException
	 */
	void selectAdvancedType(int type) throws NoteException;

	/**
	 * 阵法进阶
	 * @param ids
	 * @throws NoteException
	 */
	void advancedFormationBuff(String idArray) throws NoteException;
}

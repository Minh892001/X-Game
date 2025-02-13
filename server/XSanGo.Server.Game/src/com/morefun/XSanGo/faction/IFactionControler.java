/**
 * 
 */
package com.morefun.XSanGo.faction;

import com.XSanGo.Protocol.AMD_Faction_approveJoin;
import com.XSanGo.Protocol.AMD_Faction_createFaction;
import com.XSanGo.Protocol.AMD_Faction_getFactionInfo;
import com.XSanGo.Protocol.AMD_Faction_getHarmRank;
import com.XSanGo.Protocol.AMD_Faction_getJoinRequestList;
import com.XSanGo.Protocol.AMD_Faction_getMemberRank;
import com.XSanGo.Protocol.AMD_Faction_getMyFaction;
import com.XSanGo.Protocol.AMD_Faction_getRivalFormation;
import com.XSanGo.Protocol.AMD_Faction_getTechnologyDonateLog;
import com.XSanGo.Protocol.AMD_Faction_getWarehouseItemQueue;
import com.XSanGo.Protocol.AMD_Faction_invite;
import com.XSanGo.Protocol.AMD_Faction_selectRival;
import com.XSanGo.Protocol.AMD_Faction_warehouseAllot;
import com.XSanGo.Protocol.FactionAllotLog;
import com.XSanGo.Protocol.FactionCopyInfoView;
import com.XSanGo.Protocol.FactionCopyResultView;
import com.XSanGo.Protocol.FactionHistoryView;
import com.XSanGo.Protocol.FactionMailLog;
import com.XSanGo.Protocol.FactionOviStoreView;
import com.XSanGo.Protocol.FactionRankView;
import com.XSanGo.Protocol.FactionShopView;
import com.XSanGo.Protocol.FactionStorehouseView;
import com.XSanGo.Protocol.FactionTechnologyView;
import com.XSanGo.Protocol.FactionWarehouseView;
import com.XSanGo.Protocol.GvgView;
import com.XSanGo.Protocol.IntIntPair;
import com.XSanGo.Protocol.MonsterView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.PurchaseLog;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * 公会操作接口
 * 
 * @author sulingyun
 * 
 */
public interface IFactionControler extends IRedPointNotable {

	/**
	 * 是否有公会
	 * 
	 * @return
	 */
	boolean isInFaction();

	/**
	 * 所在公会名
	 * 
	 * @return
	 */
	String getFactionName();

	/**
	 * 设置公会索引
	 * 
	 * @param id
	 */
	void setFaction(String id);

	/**
	 * 申请加入公会
	 * 
	 * @param faction
	 * @throws NoteException
	 */
	void applyForFaction(IFaction faction) throws NoteException;

	/**
	 * 获取我的公会数据
	 * 
	 * @param __cb
	 */
	void getMyFactionInfo(AMD_Faction_getMyFaction __cb);

	/**
	 * 批准入会申请
	 * 
	 * @param applyId
	 * @param __cb
	 */
	void approveReq(String applyId, AMD_Faction_approveJoin __cb);

	/**
	 * 拒绝入会申请
	 * 
	 * @param applyId
	 */
	void denyReq(String applyId);

	/**
	 * 自己离开公会
	 * 
	 * @throws NoteException
	 */
	void divorceFaction() throws NoteException;

	/**
	 * 剔除成员
	 * 
	 * @param memberId
	 */
	void deleteMember(String memberId) throws NoteException;

	/**
	 * 设置为无公会状态
	 */
	void setNoFaction();

	/**
	 * 获取入会申请列表
	 * 
	 * @param __cb
	 */
	void getJoinRequestList(AMD_Faction_getJoinRequestList __cb);

	/**
	 * 取消入会申请
	 * 
	 * @param factionId
	 */
	void cancelApplication(String factionId);

	/**
	 * 修改公会公告
	 * 
	 * @param newNotice
	 * @throws NoteException
	 */
	void updateNotice(String newNotice) throws NoteException;

	/**
	 * 设置成普通会员
	 * 
	 * @param targetId
	 * @throws NoteException
	 */
	void setCommon(String targetId) throws NoteException;

	/**
	 * 升级为长老
	 * 
	 * @param targetId
	 * @throws NoteException
	 */
	void upElder(String targetId) throws NoteException;

	/**
	 * 公会转让
	 * 
	 * @param targetId
	 * @throws NoteException
	 */
	void transferFaction(String targetId) throws NoteException;

	/**
	 * 修改公会配置
	 * 
	 * @param icon
	 * @param qq
	 * @param notice
	 * @param joinType
	 * @throws NoteException
	 */
	void factionConfig(String icon, String qq, String notice, int joinType, int joinLevel, int joinVip,
			String manifesto, int deleteDay) throws NoteException;

	/**
	 * 获取公会动态
	 * 
	 * @return
	 */
	FactionHistoryView[] getFactionHistorys();

	/**
	 * 获取公会编号
	 * 
	 * @return
	 */
	String getFactionId();

	/**
	 * 创建公会
	 * 
	 * @param cb
	 */
	void createFaction(String name, String icon, AMD_Faction_createFaction cb);

	/**
	 * 获取公会等级
	 * 
	 * @return
	 */
	int getFactionLevel();

	/**
	 * 获取我的公会接口 没有加入公会返回null
	 * 
	 * @return
	 */
	IFaction getMyFaction();

	/**
	 * 公会捐赠
	 * 
	 * @param num
	 * @throws NoteException
	 */
	void donation(int num) throws NoteException;

	IntIntPair factionCopyList() throws NoteException;

	/**
	 * 开启副本
	 * 
	 * @param copyId
	 */
	int openFactionCopy(int copyId) throws NoteException;

	/**
	 * 开启副本
	 * 
	 * @param copyId
	 */
	void closeFactionCopy() throws NoteException;

	/**
	 * 副本详情
	 * 
	 * @return
	 */
	FactionCopyInfoView factionCopyInfo() throws NoteException;

	/**
	 * 开始挑战 返回怪物血量和怒气信息
	 * 
	 * @return
	 */
	FactionCopyResultView beginChallenge() throws NoteException;

	/**
	 * 结束挑战传入怪物血量和怒气信息
	 * 
	 * @param monsterViews
	 */
	void endChallenge(MonsterView[] monsterViews, boolean isKill, boolean isHurtBlood, int dropBlood)
			throws NoteException;

	/**
	 * 获取公会商店商品
	 * 
	 * @return
	 * @throws NoteException
	 */
	public FactionShopView getFactionShops() throws NoteException;

	/**
	 * 购买公会商品
	 * 
	 * @param id
	 * @throws NoteException
	 */
	public void buyFactionShop(int id) throws NoteException;

	/**
	 * 获取公会战信息
	 * 
	 * @return
	 * @throws NoteException
	 */
	GvgView getGvgInfo() throws NoteException;

	/**
	 * 报名
	 * 
	 * @throws NoteException
	 */
	void applyGvg() throws NoteException;

	/**
	 * 选择对手
	 * 
	 * @return
	 * @throws NoteException
	 */
	void selectRival(AMD_Faction_selectRival __cb, int index) throws NoteException;

	/**
	 * 公会战个人排行榜
	 * 
	 * @param __cb
	 * @throws NoteException
	 */
	void getMemberRank(AMD_Faction_getMemberRank __cb) throws NoteException;

	/**
	 * 公会战公会排行榜
	 * 
	 * @return
	 * @throws NoteException
	 */
	FactionRankView getFactionRank() throws NoteException;

	/**
	 * 公会战获取对手阵容
	 * 
	 * @param __cb
	 * @param roleId
	 * @throws NoteException
	 */
	void getRivalFormation(AMD_Faction_getRivalFormation __cb, String roleId) throws NoteException;

	/**
	 * 公会战结束战斗
	 * 
	 * @param isWin
	 * @param heroNum
	 * @throws NoteException
	 */
	IntIntPair endGvg(boolean isWin, int heroNum) throws NoteException;

	/**
	 * 复活
	 * 
	 * @throws NoteException
	 */
	void reviveGvg() throws NoteException, NotEnoughYuanBaoException, NotEnoughMoneyException;

	/**
	 * 公会战开始战斗
	 * 
	 * @throws NoteException
	 */
	String beginGvg() throws NoteException;

	/**
	 * 获取公会副本伤害排行榜
	 * 
	 * @param __cb
	 * @throws NoteException
	 */
	void getHarmRank_async(AMD_Faction_getHarmRank __cb) throws NoteException;

	/**
	 * 改名
	 * 
	 * @param newName
	 * @throws NoteException
	 */
	void rename(String newName) throws NoteException;

	/**
	 * 发送公会邮件
	 * 
	 * @param type
	 * @param title
	 * @param content
	 */
	int sendFactionMail(int type, String title, String content) throws NoteException;

	/**
	 * 获取公会邮件记录
	 * 
	 * @return
	 */
	FactionMailLog[] getFactionMailLog() throws NoteException;

	/**
	 * 打开公会仓库
	 * 
	 * @return
	 * @throws NoteException
	 */
	FactionWarehouseView openWarehouse() throws NoteException;

	/**
	 * 仓库分配物品
	 * 
	 * @param roleId
	 * @param itemId
	 * @param num
	 * @throws NoteException
	 */
	void warehouseAllot(AMD_Faction_warehouseAllot __cb, String roleId, String itemId, int num) throws NoteException;

	/**
	 * 打开公会栈房
	 * 
	 * @return
	 * @throws NoteException
	 */
	FactionStorehouseView openStorehouse() throws NoteException;

	/**
	 * 公会栈房购置物品
	 * 
	 * @param itemId
	 * @param num
	 * @throws NoteException
	 */
	void storehousePurchase(String itemId, int num) throws NoteException;

	/**
	 * 打开公会商铺
	 * 
	 * @return
	 * @throws NoteException
	 */
	FactionOviStoreView openOviStore() throws NoteException;

	/**
	 * 公会商铺购买物品
	 * 
	 * @param itemId
	 * @param num
	 * @throws NoteException
	 */
	void oviStoreBuy(String itemId, int num) throws NoteException;

	/**
	 * 获取公会科技列表
	 * 
	 * @return
	 * @throws NoteException
	 */
	FactionTechnologyView technologyList() throws NoteException;

	/**
	 * 设置推荐科技
	 * 
	 * @param id
	 * @throws NoteException
	 */
	int setRecommendTechnology(int id) throws NoteException;

	/**
	 * 捐赠科技
	 * 
	 * @param id
	 * @param type
	 * @throws NoteException
	 */
	void donateTechnology(int id, int type) throws NoteException;

	/**
	 * 清除捐赠CD
	 * 
	 * @throws NoteException
	 */
	void clearDonateCD() throws NoteException;

	/**
	 * 研究
	 * 
	 * @param id
	 * @throws NoteException
	 */
	void studyTechnology(int id) throws NoteException;

	/**
	 * 根据ID获取公会科技收益值
	 * 
	 * @param id
	 * @return
	 */
	int getTechnologyValue(int id);

	/**
	 * 获取可分配仓库物品数据
	 * 
	 * @return
	 */
	int getCanAllotItemNum();

	/**
	 * 增加仓库物品分配次数
	 * 
	 * @param num
	 */
	void addAllotItemNum(int num);

	/**
	 * 获取仓库分配日志
	 * 
	 * @return
	 * @throws NoteException
	 */
	FactionAllotLog[] getFactionAllotLog() throws NoteException;

	/**
	 * 获取捐赠日志
	 * 
	 * @return
	 * @throws NoteException
	 */
	void getTechnologyDonateLog(AMD_Faction_getTechnologyDonateLog __cb) throws NoteException;

	/**
	 * 获取栈房购置日志
	 * 
	 * @return
	 * @throws NoteException
	 */
	PurchaseLog[] getPurchaseLog() throws NoteException;

	/**
	 * 获取仓库物品排队队列
	 * 
	 * @param __cb
	 * @param id
	 */
	void getWarehouseItemQueue(AMD_Faction_getWarehouseItemQueue __cb, int id);

	/**
	 * 索要物品
	 * 
	 * @param itemId
	 * @param type
	 * @throws NoteException
	 */
	void demandItem(String itemId, int type) throws NoteException;

	/**
	 * 申请物品(排队)
	 * 
	 * @param id
	 * @param type
	 * @throws NoteException
	 */
	void applyItem(int id, int type) throws NoteException;

	/**
	 * 获取仓库物品申请在我前面的人数
	 * 
	 * @param itemId
	 * @return
	 * @throws NoteException
	 */
	int getBeforePeople(String itemId) throws NoteException;

	/**
	 * 掉线后清除副本挑战状态
	 */
	void clearCopyRole();

	/**
	 * 招贤
	 * 
	 * @param isFree
	 * @return
	 * @throws NoteException
	 */
	boolean recruit(boolean isFree) throws NoteException;

	/**
	 * 邀请
	 * 
	 * @param isFree
	 * @param roleId
	 * @return
	 * @throws NoteException
	 */
	void invite(AMD_Faction_invite __cb, boolean isFree, String roleId);

	/**
	 * 获取招贤和邀请可用次数
	 * 
	 * @param type
	 * @return
	 * @throws NoteException
	 */
	IntIntPair getRecruitCount(int type) throws NoteException;

	void getFactionInfo(AMD_Faction_getFactionInfo __cb, String id);
}

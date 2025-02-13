/**
 * 
 */
package com.morefun.XSanGo.sns;

import java.util.Collection;
import java.util.List;

import com.XSanGo.Protocol.AMD_Sns_acceptJunLing;
import com.XSanGo.Protocol.AMD_Sns_queryBattleRecordView;
import com.XSanGo.Protocol.AMD_Sns_sendJunLing;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.SnsRoleView;
import com.morefun.XSanGo.db.game.FriendApplyingHistory;
import com.morefun.XSanGo.db.game.RoleSns;
import com.morefun.XSanGo.role.IRedPointNotable;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.sns.SnsController.OnSnsRoleViewCallback;

/**
 * 社交模块控制接口
 * 
 * @author sulingyun
 * 
 */
public interface ISnsControler extends IRedPointNotable {

	/**
	 * 是否有盟
	 * 
	 * @return
	 */
	boolean hasGroup();

	/**
	 * @param player 接受此玩家的好友申请
	 */
	void accept(SNSType type, String player) throws Exception;

	/**
	 * @param player 拒绝此玩家的好友申请
	 */
	void refuse(String player);

	/**
	 * @param player 添加好友
	 * @return successful or not
	 */
	boolean applyForFriend(String player) throws NoteException;

	/**
	 * @param type 操作的列表
	 * @param player 操作的目标
	 */
	void remove(SNSType type, String player);

	/**
	 * @param type 清除的列表
	 */
	void clear(SNSType type);

	/**
	 * @return 好友列表
	 */
	Collection<String> getFriends();

	/**
	 * @return 仇人列表
	 */
	Collection<String> getFoes();

	/**
	 * @return 黑名单
	 */
	Collection<String> getBlacklist();

	IActAbilityController getActAbilityController();

	/**
	 * @return 未处理的好友申请
	 */
	Collection<FriendApplyingHistory> unTreatedApplying();

	int maxSize(SNSType type);

	/**
	 * 好友送军令
	 * */
	void giveJunLingByFriend(String friendId, int num) throws NoteException;

	/**
	 * 送军令
	 * */
	void sendJunLing(String targetId, final AMD_Sns_sendJunLing __cb);

	/**
	 * 领军令
	 * */
	void acceptJunLing(String targetId, final AMD_Sns_acceptJunLing __cb);

	/**
	 * 重置军令的状态，每天零点的时候重置
	 * */
	void clearJunLingStatus();

	/**
	 * 获取今日送出军令的数量
	 * */
	int getSendJunLingNum();

	/**
	 * 获取今日领取军令的数量
	 * */
	int getAcceptJunLingNum();

	/**
	 * 获取给某个好友送出的军令数量
	 * */
	int getSendJunLingNumToFriend(String targetId);

	/**
	 * 获取某个好友可领取的军令数量
	 * */
	int getAcceptJunLingNumFromFriend(String targetId);

	/**
	 * 获取好友点数
	 * */
	int getFriendPoint(String targetId);

	/**
	 * 增加好友点数
	 * */
	void addFriendPoint(String targetId, int num);

	/**
	 * 获取好友RoleSns
	 * */
	List<RoleSns> getRoleSns(SNSType type);

	/**
	 * 获取最大好友点数
	 * */
	int getMaxFriendPoint();

	/**
	 * 处理小林志玲带给你的福利
	 * 
	 */
	void processBenefitFromMsLing();

	/**
	 * 获取好友SnsRoleView
	 * */
	public void getSnsRoleViews(final Collection<String> collection, final OnSnsRoleViewCallback cb);
	
	/** 包装SnsRoleView对象 */
	SnsRoleView asView(IRole rolefriend, boolean canApplying);
	
	/** 使SnsRoleView 缓存数据失效 */
	void makeSnsRoleViewExpire();
	
	/** 是否是我的好友 */
	boolean isMyFriend(String targetID);
	
	/** 获取发送军令个数限制 */
	int getSendJunLingLimit(String id);
	/** 更新发送军令个数 */
	void addOrUpdateSendJunLingLimit(String targetId, int num);
	/** 增加好友 */
	void addFriend(String id);
	
	/**
	 * 战报记录界面
	 */
	void queryBattleRecordView(final AMD_Sns_queryBattleRecordView __cb) throws NoteException;
}

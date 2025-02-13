package com.morefun.XSanGo.friendsRecall;

import java.util.List;

import Ice.Current;

import com.XSanGo.Protocol.AMD_FriendsRecall_openInvitation;
import com.XSanGo.Protocol.AMD_FriendsRecall_randomOfflineRole;
import com.XSanGo.Protocol.FriendsRecallTaskView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.RecallView;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.role.IRedPointNotable;

public interface IFriendsRecallController extends IRedPointNotable {

	/**
	 * 打开回归有礼界面
	 * 
	 * @return
	 * @throws NoteException
	 */
	public RecallView openRecall() throws NoteException;

	/**
	 * 打开邀请玩家界面
	 * 
	 * @param __cb
	 * @param __current
	 * @throws NoteException
	 */
	public void openInvitation_async(AMD_FriendsRecall_openInvitation __cb, Current __current) throws NoteException;

	/**
	 * 激活邀请码
	 * 
	 * @param code
	 * @return
	 * @throws NoteException
	 */
	public List<FriendsRecallTaskView> activeInvitationCode(String code) throws NoteException;

	/**
	 * 领取任务奖励
	 * 
	 * @param taskId
	 * @return
	 * @throws NoteException
	 */
	public List<FriendsRecallTaskView> receiveTaskReward(int taskId) throws NoteException, NotEnoughMoneyException;

	/**
	 * 新增一个成功的邀请
	 * 
	 * @param recalledId
	 */
	public void addSuccessfulInvitation(Role recalled);

	/**
	 * 处理可重复邀请任务，（累计签到，累计充值）
	 * 
	 * @param oldValue
	 * @param currValue
	 * @param taskTarget
	 */
	public void processRepeatTask(int oldValue, int currValue, String taskTarget);

	/**
	 * 处理一次性邀请任务
	 * 
	 * @param currValue
	 * @param taskTarget
	 */
	public void processStepTask(int currValue, String taskTarget);

	/**
	 * 新增一条一次性任务
	 * 
	 * @param taskT
	 */
	public void addNewStepTask(FriendsRecallBaseT taskT);

	/**
	 * 随机换一个有回归资格的玩家
	 * 
	 * @param __cb
	 * @param currOfflineRoleId
	 */
	public void randomOfflineRole(AMD_FriendsRecall_randomOfflineRole __cb, String currOfflineRoleId);
}

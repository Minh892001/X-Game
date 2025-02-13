package com.morefun.XSanGo.activity;

import com.XSanGo.Protocol.InviteActivityView;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.role.IRedPointNotable;

public interface IInviteActivityControler extends IRedPointNotable {
	/**
	 * 获取邀请好友活动VIEW
	 * @return
	 * @throws NoteException
	 */
	InviteActivityView getInviteActivityView() throws NoteException;

	/**
	 * 领取邀请好友奖励
	 * @param threshoId
	 * @throws NoteException
	 */
	void receiveRewardForInvite(int threshoId) throws NoteException;
}

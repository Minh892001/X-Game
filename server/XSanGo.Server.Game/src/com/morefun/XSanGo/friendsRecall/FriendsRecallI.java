package com.morefun.XSanGo.friendsRecall;

import Ice.Current;

import com.XSanGo.Protocol.AMD_FriendsRecall_openInvitation;
import com.XSanGo.Protocol.AMD_FriendsRecall_randomOfflineRole;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._FriendsRecallDisp;
import com.XSanGo.Protocol.showIconView;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

public class FriendsRecallI extends _FriendsRecallDisp {

	private IRole roleRt;

	public FriendsRecallI(IRole roleRt) {
		this.roleRt = roleRt;
	}

	@Override
	public String openRecall(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getFriendsRecallController().openRecall());
	}

	@Override
	public void openInvitation_async(AMD_FriendsRecall_openInvitation __cb, Current __current) throws NoteException {
		roleRt.getFriendsRecallController().openInvitation_async(__cb, __current);
	}

	@Override
	public String activeInvitationCode(String code, Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getFriendsRecallController().activeInvitationCode(code));
	}

	@Override
	public String receiveTaskReward(int taskId, Current __current) throws NoteException, NotEnoughMoneyException {
		return LuaSerializer.serialize(roleRt.getFriendsRecallController().receiveTaskReward(taskId));
	}

	@Override
	public String showFriendsRecallIcon(Current __current) throws NoteException {
		return LuaSerializer.serialize(new showIconView(XsgFriendsRecallManager.getInstance().isShowIcon()));
	}

	@Override
	public void randomOfflineRole_async(AMD_FriendsRecall_randomOfflineRole __cb, String currOfflineRoleId,
			Current __current) throws NoteException {
		roleRt.getFriendsRecallController().randomOfflineRole(__cb, currOfflineRoleId);
	}
}

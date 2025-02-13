package com.morefun.XSanGo.achieve;

import Ice.Current;

import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._AchieveInfoDisp;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

public class AchieveI extends _AchieveInfoDisp {

	private IRole roleRt;

	public AchieveI(IRole roleRt) {
		this.roleRt = roleRt;
	}

	@Override
	public String achievePageView(int functionId, Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getAchieveControler().achievePageView(functionId));
	}

	@Override
	public String achieveReward(int id, Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getAchieveControler().achieveReward(id));
	}

	@Override
	public String achieveProgressView(Current __current) throws NoteException {
		return roleRt.getAchieveControler().getAchieveProgressView();
	}

	@Override
	public String achieveProgressReward(int progress, Current __current) throws NoteException {
		return roleRt.getAchieveControler().recProgressAward(progress);
	}

}

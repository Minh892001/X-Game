package com.morefun.XSanGo.colorfulEgg;

import Ice.Current;

import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._ColorfulEggDisp;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

public class ColorfullEggI extends _ColorfulEggDisp{

	private static final long serialVersionUID = 4195744379976617908L;

	private IRole roleRt;
	
	public ColorfullEggI(IRole roleRt) {
		this.roleRt = roleRt;
	}

	@Override
	public String getView(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getColorfullEggController().getView());
	}

	@Override
	public String brokenEgg(byte eggFlag, Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getColorfullEggController().brokenEgg(eggFlag));
	}

	@Override
	public void acceptReward(String itemId, int num, Current __current) throws NoteException {
		roleRt.getColorfullEggController().acceptReward(itemId, num);
	}

}

package com.morefun.XSanGo.luckybag;

import Ice.Current;

import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._LuckyBagDisp;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

public class LuckyBagI extends _LuckyBagDisp {

	private static final long serialVersionUID = -7188057753999891186L;
	private IRole iRole;

	public LuckyBagI(IRole iRole) {
		this.iRole = iRole;
	}

	@Override
	public String getLuckBagView(Current __current) throws NoteException {
		return LuaSerializer.serialize(iRole.getLuckyBagControler()
				.getLuckBagView());
	}

	@Override
	public void receiveDayBag(int id, Current __current) throws NoteException {
		iRole.getLuckyBagControler().receiveDayBag(id);
	}

	@Override
	public void receiveMonthBag(int id, Current __current) throws NoteException {
		iRole.getLuckyBagControler().receiveMonthBag(id);
	}

}

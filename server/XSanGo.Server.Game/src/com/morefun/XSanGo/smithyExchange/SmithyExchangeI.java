package com.morefun.XSanGo.smithyExchange;

import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._SmithyExchangeDisp;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

import Ice.Current;

public class SmithyExchangeI extends _SmithyExchangeDisp{
	
	private static final long serialVersionUID = -2560208850375541188L;

	private IRole roleRt;

	public SmithyExchangeI(IRole role) {
		this.roleRt = role;
	}

	@Override
	public String selMallList(Current __current) throws NoteException {
		return LuaSerializer.serialize(this.roleRt.getSmithyExchangeController()
				.selMallList());
	}

	@Override
	public String refMallList(Current __current) throws NoteException, NotEnoughYuanBaoException {
		return LuaSerializer.serialize(this.roleRt.getSmithyExchangeController()
				.refMallList());
	}

	@Override
	public String exchangeItem(int itemId, Current __current) throws NoteException {
		return LuaSerializer.serialize(this.roleRt.getSmithyExchangeController()
				.exchangeItem(itemId));
	}

	@Override
	public String selBlueMallList(Current __current) throws NoteException {
		return LuaSerializer.serialize(this.roleRt.getSmithyExchangeController()
				.selBlueMallList());
	}

	@Override
	public String refBlueMallList(Current __current) throws NotEnoughMoneyException, NoteException {
		return LuaSerializer.serialize(this.roleRt.getSmithyExchangeController()
				.refBlueMallList());
	}

	@Override
	public String exchangeBlueItem(int itemId, Current __current) throws NoteException {
		return LuaSerializer.serialize(this.roleRt.getSmithyExchangeController()
				.exchangeBlueItem(itemId));
	}

	@Override
	public String preview(Current __current) throws NoteException {
		return LuaSerializer.serialize(this.roleRt.getSmithyExchangeController()
				.getPreview());
	}

}

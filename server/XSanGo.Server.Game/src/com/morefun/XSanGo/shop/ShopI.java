package com.morefun.XSanGo.shop;

import Ice.Current;

import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.ShopView;
import com.XSanGo.Protocol._ShopDisp;
import com.morefun.XSanGo.monitor.XsgMonitorManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

public class ShopI extends _ShopDisp {

	private static final long serialVersionUID = -2560208850375548588L;

	private IRole roleRt;

	public ShopI(IRole role) {
		this.roleRt = role;
	}

	@Override
	public String getShopView(int type, Current __current) throws NoteException {
		ShopView[] shopViews = this.roleRt.getShopControler().getShopView(type);
		return LuaSerializer.serialize(shopViews);
	}

	@Override
	public void buyItem(int num, String id, int type, Current __current)
			throws NotEnoughYuanBaoException, NoteException {
		this.roleRt.getShopControler().buyItem(num, id, type);
	}

}

package com.morefun.XSanGo.superCharge;

import com.XSanGo.Protocol.AMD_SuperCharge_getRaffleView;
import com.XSanGo.Protocol.AMD_SuperCharge_getReceivedViews;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._SuperChargeDisp;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

import Ice.Current;

public class SuperChargeI extends _SuperChargeDisp {

	private static final long serialVersionUID = -7188057753999891186L;
	private IRole iRole;

	public SuperChargeI(IRole iRole) {
		this.iRole = iRole;
	}
	@Override
	public String getSuperChargeView(Current __current) throws NoteException {
		return LuaSerializer.serialize(
				iRole.getSuperChargeControlle().getSuperChargeView());
	}

	@Override
	public String receiveSuperChargeReward(int id, Current __current) throws NoteException {
		return LuaSerializer.serialize(
		iRole.getSuperChargeControlle().receiveReward(id));
	}
	
	@Override
	public String acceptRaffleReward(Current __current) throws NoteException {
		return LuaSerializer.serialize(
				iRole.getSuperChargeControlle().acceptRaffleReward());
	}
	
	@Override
	public void getRaffleView_async(AMD_SuperCharge_getRaffleView __cb, Current __current) throws NoteException {
		iRole.getSuperChargeControlle().getRaffleView(__cb);
	}
	
	@Override
	public void getReceivedViews_async(AMD_SuperCharge_getReceivedViews __cb, Current __current) throws NoteException {
		iRole.getSuperChargeControlle().getReceivedViews(__cb);
	}

}

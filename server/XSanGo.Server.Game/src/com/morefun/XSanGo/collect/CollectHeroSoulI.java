package com.morefun.XSanGo.collect;

import java.util.Date;

import Ice.Current;

import com.XSanGo.Protocol.AMD_CollectHeroSoul_doCollectHeroSoul;
import com.XSanGo.Protocol.AMD_CollectHeroSoul_doRefresh;
import com.XSanGo.Protocol.AMD_CollectHeroSoul_reqCollectHeroSoul;
import com.XSanGo.Protocol.CollectHeroSoulView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._CollectHeroSoulDisp;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

public class CollectHeroSoulI extends _CollectHeroSoulDisp {

	private static final long serialVersionUID = -4580728025651356729L;

	private IRole roleRt;

	public CollectHeroSoulI(IRole role) {
		this.roleRt = role;
	}

	@Override
	public void reqCollectHeroSoul_async(
			AMD_CollectHeroSoul_reqCollectHeroSoul __cb, Current __current)
			throws NoteException {
		roleRt.getRoleOpenedMenu().setOpenHeroCallDate(new Date());
		try {
			CollectHeroSoulView[] views = this.roleRt
					.getCollectHeroSoulControler().reqCollectHeroSoul();
			__cb.ice_response(LuaSerializer.serialize(views));
		} catch (Exception e) {
			__cb.ice_exception(e);
		}
	}

	@Override
	public void doCollectHeroSoul_async(
			AMD_CollectHeroSoul_doCollectHeroSoul __cb, int cType,
			int consumType, Current __current) throws NotEnoughMoneyException,
			NoteException {
		try {
			__cb.ice_response(LuaSerializer.serialize(this.roleRt.getCollectHeroSoulControler()
					.doCollectHeroSoul(cType, consumType)));
		} catch (Exception e) {
			__cb.ice_exception(e);
		}
	}

	@Override
	public void doRefresh_async(AMD_CollectHeroSoul_doRefresh __cb, int cType,
			Current __current) throws NotEnoughYuanBaoException, NoteException,
			NotEnoughMoneyException {
		try {
			CollectHeroSoulView view = this.roleRt
					.getCollectHeroSoulControler().doRefresh(cType);
			__cb.ice_response(LuaSerializer.serialize(view));
		} catch (Exception e) {
			__cb.ice_exception(e);
		}
	}

}

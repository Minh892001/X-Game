package com.morefun.XSanGo.partner;

import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.PartnerView;
import com.XSanGo.Protocol.PartnerViewInfo;
import com.XSanGo.Protocol._PartnerDisp;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

import Ice.Current;

public class PartnerI extends _PartnerDisp {

	/** 序列化版本 */
	private static final long serialVersionUID = 1L;
	private IRole roleRt;
	
	public PartnerI(IRole roleRt) {
		super();
		this.roleRt = roleRt;
	}

	@Override
	public String getPartnerViewList(Current __current) throws NoteException {
		
		PartnerView result = roleRt.getPartnerControler().getPartnerView();
		//System.out.println(LuaSerializer.serialize(result));
		return LuaSerializer.serialize(result);
	}

	@Override
	public void setHeroPosition(byte postion, String heroId, String oldHeroId, Current __current) throws NoteException {
		
		//System.out.println("heroId: "+heroId);
		
		IHero hero = this.roleRt.getHeroControler().getHero(heroId);
		IHero oldHero = this.roleRt.getHeroControler().getHero(oldHeroId);
		//PartnerResult result = new PartnerResult(0, "set faild!");
		try {
			this.roleRt.getNotifyControler().setAutoNotify(false);
			this.roleRt.getPartnerControler().setPartnerPosition(
					postion, hero, oldHero, true);
		} finally {
			this.roleRt.getNotifyControler().setAutoNotify(true);
		}
		
		//return LuaSerializer.serialize(result);
	}

	@Override
	public String openPartnerShipPos(byte postion, Current __current) throws NoteException {
		PartnerViewInfo result = this.roleRt.getPartnerControler().openPartnerPosition(postion);
		return LuaSerializer.serialize(result);
		
	}

	@Override
	public String resetPartnerPos(byte postion, int cost, int isLock, Current __current) throws NoteException {
		PartnerViewInfo result = this.roleRt.getPartnerControler().resetPartnerPosition(postion,cost, isLock);
		return LuaSerializer.serialize(result);
	}

	@Override
	public void clearAll(Current __current) throws NoteException {

		this.roleRt.getPartnerControler().clearAll();
		//return LuaSerializer.serialize(result);
	}

	@Override
	public String getLevelRequired(Current __current) throws NoteException {
		
		return LuaSerializer.serialize(this.roleRt.getPartnerControler().getRequiredLevel());
	}


}

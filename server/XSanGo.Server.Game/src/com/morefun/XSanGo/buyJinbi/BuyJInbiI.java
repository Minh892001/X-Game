/**
 * 
 */
package com.morefun.XSanGo.buyJinbi;

import Ice.Current;

import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._BuyJinbiDisp;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

/**
 * 点金手 元宝购买金币 接口
 * 
 * @author 吕明涛
 * 
 */
public class BuyJInbiI extends _BuyJinbiDisp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3184685478549728787L;

	private IRole roleRt;

	public BuyJInbiI(IRole dragonRole) {
		this.roleRt = dragonRole;
	}

	/**
	 * 
	 */
	@Override
	public String selectBuyNum(Current __current) throws NoteException {
		return LuaSerializer.serialize(this.roleRt.getBuyJInbiControler()
				.selectBuyNum());
	}

	@Override
	public String buy(Current __current) throws NoteException,NotEnoughYuanBaoException {
		return LuaSerializer
				.serialize(this.roleRt.getBuyJInbiControler().buy());
	}
}

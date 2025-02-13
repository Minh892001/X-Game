/**
 * 
 */
package com.morefun.XSanGo.hero.market;

import Ice.Current;

import com.XSanGo.Protocol.BuyHeroResult;
import com.XSanGo.Protocol.MarketView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._MarketDisp;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

/**
 * @author sulingyun
 * 
 */
public class MarketI extends _MarketDisp {
	private static final long serialVersionUID = 2046674733741607263L;
	private IRole role;

	public MarketI(IRole role) {
		this.role = role;
	}

	@Override
	public String getMarketView(Current __current) {
		MarketView mv = this.role.getHeroMarketControler().getView();
		return LuaSerializer.serialize(mv);
	}

	@Override
	public String OneInTen(Current __current) throws NotEnoughMoneyException,
			NoteException {
		BuyHeroResult bhr = this.role.getHeroMarketControler().oneInTen();

		return LuaSerializer.serialize(bhr);
	}

	@Override
	public String OneInTen10(Current __current) throws NotEnoughMoneyException,
			NoteException {
		BuyHeroResult[] bhrArray = this.role.getHeroMarketControler()
				.oneInTen10();
		return LuaSerializer.serialize(bhrArray);
	}

	@Override
	public String OneInHundred(Current __current)
			throws NotEnoughYuanBaoException, NoteException {
		BuyHeroResult bhr = this.role.getHeroMarketControler().oneInHundred();
		return LuaSerializer.serialize(bhr);
	}

	@Override
	public String OneInHundred10(Current __current)
			throws NotEnoughYuanBaoException, NoteException {
		BuyHeroResult[] bhrArray = this.role.getHeroMarketControler()
				.oneInHundred10();
		return LuaSerializer.serialize(bhrArray);
	}

	@Override
	public String buyLimitHero(int type, Current __current)
			throws NotEnoughYuanBaoException, NoteException {
		return LuaSerializer.serialize(role.getHeroMarketControler()
				.buyLimitHero(type));
	}
}

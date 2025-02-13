package com.morefun.XSanGo.trader;

import Ice.Current;

import com.XSanGo.Protocol.DuelReportView;
import com.XSanGo.Protocol.DuelResult;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.TraderView;
import com.XSanGo.Protocol._TraderDisp;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

public class TraderI extends _TraderDisp {
	private IRole role;

	public TraderI(IRole role) {
		this.role = role;
	}

	@Override
	public String getTraderView(Current __current) throws NoteException {
		TraderView view = this.role.getTraderControler().getTraderView();
		return LuaSerializer.serialize(view);
	}

	@Override
	public String callJinbiTrader(Current __current)
			throws NotEnoughMoneyException, NoteException {
		TraderView view = this.role.getTraderControler().callJinbiTrader();
		return LuaSerializer.serialize(view);
	}

	@Override
	public String callYuanbaoTrader(Current __current)
			throws NotEnoughYuanBaoException, NoteException {
		TraderView view = this.role.getTraderControler().callYuanbaoTrader();
		return LuaSerializer.serialize(view);
	}

	@Override
	public void buyItem(String id, Current __current)
			throws NotEnoughMoneyException, NotEnoughYuanBaoException,
			NoteException {
		this.role.getTraderControler().buyItem(id);
	}

	@Override
	public String callJinbiHero(Current __current)
			throws NotEnoughMoneyException, NoteException {
		TraderView view = this.role.getTraderControler().callJinbiHero();
		return LuaSerializer.serialize(view);
	}

	@Override
	public String callYuanbaoHero(Current __current)
			throws NotEnoughYuanBaoException, NoteException {
		TraderView view = this.role.getTraderControler().callYuanbaoHero();
		return LuaSerializer.serialize(view);
	}

	@Override
	public DuelReportView beginChallenge(String heroId, Current __current)
			throws NoteException {
		return this.role.getTraderControler().beginChallengeHero(heroId);
	}

	@Override
	public String acceptConsolation(Current __current) {
		ItemView[] items = this.role.getTraderControler().acceptConsolation();

		return LuaSerializer.serialize(items);
	}

	@Override
	public void buyHeroItem(String id, Current __current)
			throws NotEnoughMoneyException, NotEnoughYuanBaoException,
			NoteException {
		this.role.getTraderControler().buyHeroItem(id);
	}

	@Override
	public void endChallenge(DuelResult result, Current __current)
			throws NoteException {
		this.role.getTraderControler().endChallenge(result);
	}
}

package com.morefun.XSanGo.crossServer;

import com.XSanGo.Protocol.AMD_Tournament_beginFightWith;
import com.XSanGo.Protocol.AMD_Tournament_bet;
import com.XSanGo.Protocol.AMD_Tournament_enterPVPView;
import com.XSanGo.Protocol.AMD_Tournament_enterTournament;
import com.XSanGo.Protocol.AMD_Tournament_fightWith;
import com.XSanGo.Protocol.AMD_Tournament_getBetView;
import com.XSanGo.Protocol.AMD_Tournament_getFightMovieByRecordId;
import com.XSanGo.Protocol.AMD_Tournament_getKnockOutMovie;
import com.XSanGo.Protocol.AMD_Tournament_getKnockOutMovieList;
import com.XSanGo.Protocol.AMD_Tournament_getKnockOutView;
import com.XSanGo.Protocol.AMD_Tournament_getRankList;
import com.XSanGo.Protocol.AMD_Tournament_getScoreAndWinNum;
import com.XSanGo.Protocol.AMD_Tournament_openTournamentView;
import com.XSanGo.Protocol.AMD_Tournament_refreshPVPView;
import com.XSanGo.Protocol.AMD_Tournament_setupFormation;
import com.XSanGo.Protocol.AMD_Tournament_signup;
import com.XSanGo.Protocol.Callback_CrossServerCallback_getMyRankScore;
import com.XSanGo.Protocol.CrossServerCallbackPrx;
import com.XSanGo.Protocol.IntIntPair;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._TournamentDisp;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

import Ice.Current;
import Ice.LocalException;
import Ice.UserException;

/**
 * 比武大会
 * 
 * @author guofeng.qin
 */
public class TournamentI extends _TournamentDisp {

	private static final long serialVersionUID = 1L;

	private IRole roleRt;

	public TournamentI(IRole r) {
		this.roleRt = r;
	}

	@Override
	public void enterTournament_async(AMD_Tournament_enterTournament __cb, Current __current) throws NoteException {
		roleRt.getTournamentController().openSignup(__cb);
	}

	@Override
	public void openTournamentView_async(AMD_Tournament_openTournamentView __cb, Current __current)
			throws NoteException {
		roleRt.getTournamentController().openTournamentView(__cb);
	}

	@Override
	public String preSignup(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getTournamentController().preSignup());
	}

	@Override
	public int buyRefreshCount(Current __current)
			throws NoteException, NotEnoughMoneyException, NotEnoughYuanBaoException {
		return roleRt.getTournamentController().buyRefreshCount();
	}

	@Override
	public String endFightWith(String opponentId, int flag, int remainHeroCount, int power, Current __current)
			throws NoteException {
		return roleRt.getTournamentController().endFight(opponentId, flag, remainHeroCount, power);
	}

	@Override
	public String openSetupFormation(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getTournamentController().openSetupFormation());
	}

	@Override
	public String getFightRecords(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getTournamentController().getFightRecords());
	}

	@Override
	public void signup_async(AMD_Tournament_signup __cb, Current __current) throws NoteException {
		roleRt.getTournamentController().doSignup(__cb);
	}

	@Override
	public void refreshPVPView_async(AMD_Tournament_refreshPVPView __cb, Current __current) throws NoteException {
		roleRt.getTournamentController().refreshPVP(__cb);
	}

	@Override
	public void enterPVPView_async(AMD_Tournament_enterPVPView __cb, Current __current) throws NoteException {
		roleRt.getTournamentController().getPVPInfoView(__cb);
	}

	@Override
	public void setupFormation_async(AMD_Tournament_setupFormation __cb, Current __current) throws NoteException {
		roleRt.getTournamentController().setupFormation(__cb);
	}

	@Override
	public int buyFightCount(Current __current)
			throws NotEnoughMoneyException, NotEnoughYuanBaoException, NoteException {
		return roleRt.getTournamentController().buyFightCount();
	}

	@Override
	public void getRankList_async(AMD_Tournament_getRankList __cb, Current __current) throws NoteException {
		roleRt.getTournamentController().getRankList(__cb);
	}

	@Override
	public void getFightMovieByRecordId_async(AMD_Tournament_getFightMovieByRecordId __cb, String recordId,
			Current __current) {
		roleRt.getTournamentController().getFightMovieByRecordId(recordId, __cb);
	}

	@Override
	public void getKnockOutView_async(AMD_Tournament_getKnockOutView __cb, Current __current) {
		roleRt.getTournamentController().getKnockOutView(__cb);
	}

	// @Override
	// public void getKnockOutMovie_async(AMD_Tournament_getKnockOutMovie __cb,
	// int id, Current __current) {
	// roleRt.getTournamentController().getKnockOutMovie(id, __cb);
	// }

	@Override
	public void getBetView_async(AMD_Tournament_getBetView __cb, Current __current) throws NoteException {
		roleRt.getTournamentController().getBetView(__cb);
	}

	@Override
	public void bet_async(AMD_Tournament_bet __cb, int stage, int id, String roleId, int num, Current __current)
			throws NotEnoughMoneyException, NotEnoughYuanBaoException, NoteException {
		roleRt.getTournamentController().bet(__cb, stage, id, roleId, num);
	}

	@Override
	public void getKnockOutMovieList_async(AMD_Tournament_getKnockOutMovieList __cb, int id, Current __current)
			throws NoteException {
		roleRt.getTournamentController().getKnockOutMovieList(__cb, id);
	}

	@Override
	public void getKnockOutMovie_async(AMD_Tournament_getKnockOutMovie __cb, int id, int index, Current __current)
			throws NoteException {
		roleRt.getTournamentController().getKnockOutMovie(id, index, __cb);
	}

	@Override
	public void beginFightWith_async(AMD_Tournament_beginFightWith __cb, String opponentId, Current __current)
			throws NoteException {
		roleRt.getTournamentController().beginFight(opponentId, __cb);
	}

	@Override
	public String getTournamentStatus(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getTournamentController().getTournamentStatus());
	}

	@Override
	public void getScoreAndWinNum_async(final AMD_Tournament_getScoreAndWinNum __cb, Current __current)
			throws NoteException {
		if (!XsgCrossServerManager.getInstance().isUsable()) {
			__cb.ice_response(LuaSerializer.serialize(new IntIntPair(0, roleRt.getTournamentController().getWinNum())));
			return;
		}
		CrossServerCallbackPrx server = XsgCrossServerManager.getInstance().getCrossServerCbPrx();

		server.begin_getMyRankScore(roleRt.getRoleId(), new Callback_CrossServerCallback_getMyRankScore() {

			@Override
			public void exception(LocalException __ex) {
				__cb.ice_response(
						LuaSerializer.serialize(new IntIntPair(0, roleRt.getTournamentController().getWinNum())));
			}

			@Override
			public void response(IntIntPair __ret) {
				__cb.ice_response(LuaSerializer
						.serialize(new IntIntPair(__ret.second, roleRt.getTournamentController().getWinNum())));
			}

			@Override
			public void exception(UserException __ex) {
				__cb.ice_response(
						LuaSerializer.serialize(new IntIntPair(0, roleRt.getTournamentController().getWinNum())));
			}
		});
	}

	@Override
	public void fightWith_async(AMD_Tournament_fightWith __cb, String opponentId, Current __current)
			throws NoteException {
		roleRt.getTournamentController().fight(opponentId, __cb);
	}

	@Override
	public String getTournamentShopView(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getTournamentController().getShopView());
	}

	@Override
	public String buyShopItem(String id, int num, Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getTournamentController().buyShopItem(id, num));
	}
}

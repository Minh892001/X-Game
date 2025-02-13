package com.morefun.XSanGo;

import Ice.Current;

import com.XSanGo.Protocol.AMD_CrossArenaCallback_getCrossMovie;
import com.XSanGo.Protocol.CrossArenaPvpView;
import com.XSanGo.Protocol.FightMovieView;
import com.XSanGo.Protocol.IntIntPair;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.PvpOpponentFormationView;
import com.XSanGo.Protocol.RivalRank;
import com.XSanGo.Protocol._CrossArenaCallbackDisp;
import com.morefun.XSanGo.db.CrossArenaMovie;
import com.morefun.XSanGo.db.CrossArenaMovieDAO;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

public class CrossArenaCallbackI extends _CrossArenaCallbackDisp {

	private static final long serialVersionUID = 3913776220449403864L;

	private int serverId;

	public CrossArenaCallbackI(int serverId) {
		this.serverId = serverId;
	}

	@Override
	public RivalRank updateArena(RivalRank rank, PvpOpponentFormationView pvpView, Current __current) {
		return CrossArenaManager.getInstance().updateArena(rank, pvpView);
	}

	@Override
	public RivalRank getRoleRivalRank(String roleId, Current __current) {
		return CrossArenaManager.getInstance().getRoleRivalRank(roleId);
	}

	@Override
	public PvpOpponentFormationView getRolePvpView(String roleId, Current __current) {
		return CrossArenaManager.getInstance().getRolePvpView(roleId);
	}

	@Override
	public RivalRank[] getArenaRank(int size, Current __current) {
		return CrossArenaManager.getInstance().getArenaRank(size,
				CrossArenaManager.getInstance().getRangeIdByServerId(serverId));
	}

	@Override
	public RivalRank[] refreshRival(String roleId, Current __current) {
		return CrossArenaManager.getInstance().refreshRival(roleId);
	}

	@Override
	public IntIntPair endFight(String sourceRoleId, boolean isWin, String rivalRoleId, String movieId,
			FightMovieView movieView, Current __current) {
		return CrossArenaManager.getInstance().endFight(sourceRoleId, isWin, rivalRoleId, movieId, movieView);
	}

	@Override
	public CrossArenaPvpView[] getCrossArenaPvpView(String leftRoleId, String rightRoleId, Current __current) {
		return CrossArenaManager.getInstance().getCrossArenaPvpView(leftRoleId, rightRoleId);
	}

	@Override
	public void setSignature(String roleId, String signature, Current __current) {
		CrossArenaManager.getInstance().setSignature(roleId, signature);
	}

	@Override
	public void getCrossMovie_async(final AMD_CrossArenaCallback_getCrossMovie __cb, final String id, Current __current)
			throws NoteException {
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				CrossArenaMovieDAO dao = CrossArenaMovieDAO.getFromApplicationContext(CrossArenaMain.getAC());
				CrossArenaMovie movie = dao.get(id);
				FightMovieView mv = null;
				if (movie != null) {
					try {
						mv = TextUtil.GSON.fromJson(TextUtil.ungzip(movie.getFightMovie()), FightMovieView.class);
					} catch (Exception e) {
						LogManager.error(e);
					}
					__cb.ice_response(mv);
				} else {
					__cb.ice_exception(new NoteException(""));
				}
			}
		});
	}
}

package com.morefun.XSanGo;

import java.util.List;

import Ice.Current;

import com.XSanGo.Protocol.CrossMovieView;
import com.XSanGo.Protocol.CrossRankView;
import com.XSanGo.Protocol.CrossRivalView;
import com.XSanGo.Protocol.CrossRoleView;
import com.XSanGo.Protocol.CrossScheduleView;
import com.XSanGo.Protocol.IntIntPair;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.PvpOpponentFormationView;
import com.XSanGo.Protocol._CrossServerCallbackDisp;
import com.morefun.XSanGo.db.CrossRank;

/**
 * @author guofeng.qin
 */
public class CrossServerCallbackI extends _CrossServerCallbackDisp {

	private static final long serialVersionUID = 3913776220449403864L;

	private int serverId;

	public CrossServerCallbackI(int serverId) {
		this.serverId = serverId;
	}

	@Override
	public void apply(CrossRoleView roleView, PvpOpponentFormationView pvpView, Current __current) throws NoteException {
		CrossServerManager.getInstance().addRank(roleView, pvpView);
	}

	@Override
	public CrossRankView getCrossRank(String roleId, Current __current) throws NoteException {
		return CrossServerManager.getInstance().getCrossRankView(serverId, roleId);
	}

	@Override
	public void saveBattle(CrossRoleView roleView, PvpOpponentFormationView pvpView, Current __current) throws NoteException {
		CrossServerManager.getInstance().saveRoleBattle(serverId, roleView, pvpView);
	}

	@Override
	public CrossRivalView[] matchRival(String roleId, Current __current) throws NoteException {
		return CrossServerManager.getInstance().matchRival(serverId, roleId);
	}

	@Override
	public CrossRivalView[] refreshRival(String roleId, Current __current) throws NoteException {
		return CrossServerManager.getInstance().refreshRival(serverId, roleId);
	}

	@Override
	public String endChallenge(CrossRoleView myRoleView, boolean isWin, String rivalRoleId, Current __current)
			throws NoteException {
		return CrossServerManager.getInstance().endChallenge(serverId, myRoleView, isWin, rivalRoleId);
	}

	@Override
	public CrossScheduleView[] getSchedule(Current __current) throws NoteException {
		return CrossServerManager.getInstance().getSchedule(serverId);
	}

	@Override
	public IntIntPair getMyRankScore(String roleId, Current __current) throws NoteException {
		return CrossServerManager.getInstance().getMyRankScore(serverId, roleId);
	}

	@Override
	public void crossBet(String winRoleId, Current __current) throws NoteException {
		CrossServerManager.getInstance().crossBet(serverId, winRoleId);
	}

	@Override
	public String[] getScheduleMovieList(int id, Current __current) throws NoteException {
		return CrossServerManager.getInstance().getScheduleMovieList(serverId, id);
	}

	@Override
	public CrossMovieView getScheduleMovieData(int id, int index, Current __current) throws NoteException {
		return CrossServerManager.getInstance().getScheduleMovieData(serverId, id, index);
	}

	@Override
	public PvpOpponentFormationView getRoleFormationView(String roleId, Current __current) throws NoteException {
		return CrossServerManager.getInstance().getRoleFormationView(serverId, roleId);
	}

	@Override
	public long getServerTime(Current __current) {
		return System.currentTimeMillis();
	}

	@Override
	public boolean isInRank(String roleId, Current __current) throws NoteException {
		List<CrossRank> ranks = CrossServerManager.getInstance().getCross32(serverId);
		for (CrossRank i : ranks) {
			if (i.getRoleId().equals(roleId)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isApply(String roleId, Current __current) throws NoteException {
		int crossId = CrossServerManager.getInstance().getCrossIdByServerId(serverId);
		return CrossServerManager.getInstance().getCrossRank(crossId, roleId) != null;
	}

	@Override
	public boolean isOut(String roleId, Current __current) throws NoteException {
		List<CrossRank> ranks = CrossServerManager.getInstance().getCross32(serverId);
		for (CrossRank i : ranks) {
			if (i.getRoleId().equals(roleId) && i.getIntoStage() != 0) {
				return true;
			}
		}
		return false;
	}

}

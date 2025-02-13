package com.morefun.XSanGo.timeBattle;

import Ice.Current;

import com.XSanGo.Protocol.BattleChallengeResultView;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._TimeBattleDisp;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

public class TimeBattleI extends _TimeBattleDisp {

	private static final long serialVersionUID = -4942214066050921285L;

	private IRole iRole;

	public TimeBattleI(IRole iRole) {
		this.iRole = iRole;
	}

	@Override
	public String getChallengeTimes(Current __current) throws NoteException {
		return LuaSerializer.serialize(this.iRole.getTimeBattleControler()
				.getChallengeTimes());
	}

	@Override
	public BattleChallengeResultView beginChallenge(String formationId, int id,
			Current __current) throws NoteException {
		return this.iRole.getTimeBattleControler().beginChallenge(formationId,
				id, false);
	}

	@Override
	public int endChallenge(int heroNum, Current __current)
			throws NoteException {
		return this.iRole.getTimeBattleControler().endChallenge(heroNum, false);
	}

	@Override
	public int endLimitChallenge(int heroNum, String items, Current __current)
			throws NoteException {
		return this.iRole.getTimeBattleControler().endLimitChallenge(heroNum,
				items);
	}

	@Override
	public void failChallenge(Current __current) throws NoteException {
		iRole.getTimeBattleControler().failChallenge();
	}

	@Override
	public String clear(int id, Current __current) throws NoteException {
		return LuaSerializer
				.serialize(iRole.getTimeBattleControler().clear(id));
	}
}

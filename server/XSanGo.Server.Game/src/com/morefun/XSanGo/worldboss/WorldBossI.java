package com.morefun.XSanGo.worldboss;

import Ice.Current;

import com.XSanGo.Protocol.AMD_WorldBoss_getCountRank;
import com.XSanGo.Protocol.AMD_WorldBoss_getHarmRank;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._WorldBossDisp;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

public class WorldBossI extends _WorldBossDisp {

	private static final long serialVersionUID = 3582090103591992213L;

	private IRole iRole;

	public WorldBossI(IRole iRole) {
		this.iRole = iRole;
	}

	@Override
	public String getWorldBossView(Current __current) throws NoteException {
		return LuaSerializer.serialize(iRole.getWorldBossControler()
				.getWorldBossView());
	}

	@Override
	public void getHarmRank_async(AMD_WorldBoss_getHarmRank __cb,
			Current __current) throws NoteException {
		iRole.getWorldBossControler().getHarmRank(__cb);
	}


	@Override
	public void clearCd(Current __current) throws NoteException {
		iRole.getWorldBossControler().clearCd();
	}

	@Override
	public String beginChallenge(Current __current) throws NoteException {
		return LuaSerializer.serialize(iRole.getWorldBossControler()
				.beginChallenge());
	}

	@Override
	public boolean endChallenge(int harm, int heroNum,Current __current) throws NoteException {
		return iRole.getWorldBossControler().endChallenge(harm, heroNum);
	}

	@Override
	public void getCountRank_async(AMD_WorldBoss_getCountRank __cb,
			Current __current) throws NoteException {
		iRole.getWorldBossControler().getCountRank(__cb);
	}

	@Override
	public void buyInspire(Current __current) throws NoteException {
		iRole.getWorldBossControler().buyInspire();
	}

	@Override
	public void getTailAward(int hp, Current __current) throws NoteException {
		iRole.getWorldBossControler().getTailAward(hp);
	}

	@Override
	public void trust(Current __current) throws NoteException {
		iRole.getWorldBossControler().trust();
	}

	@Override
	public void cancelTrust(Current __current) throws NoteException {
		iRole.getWorldBossControler().cancelTrust();
	}

}

package com.morefun.XSanGo.worldboss;

import com.XSanGo.Protocol.AMD_WorldBoss_getCountRank;
import com.XSanGo.Protocol.AMD_WorldBoss_getHarmRank;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.WorldBossChallengeView;
import com.XSanGo.Protocol.WorldBossView;

/**
 * 世界BOSS
 * 
 * @author xiongming.li
 *
 */
public interface IWorldBossControler {

	WorldBossView getWorldBossView() throws NoteException;

	void getHarmRank(AMD_WorldBoss_getHarmRank cb) throws NoteException;

	void getCountRank(AMD_WorldBoss_getCountRank cb) throws NoteException;

	void buyInspire() throws NoteException;

	void clearCd() throws NoteException;

	WorldBossChallengeView beginChallenge() throws NoteException;

	boolean endChallenge(int harm, int heroNum) throws NoteException;

	void getTailAward(int hp) throws NoteException;

	/**
	 * 托管
	 * 
	 * @throws NoteException
	 */
	void trust() throws NoteException;
	
	/**
	 * 取消托管
	 * @throws NoteException
	 */
	void cancelTrust() throws NoteException;

	/**
	 * 发放托管奖励
	 */
	void sendTrstAward();
}

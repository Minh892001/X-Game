package com.morefun.XSanGo.ArenaRank;

import Ice.Current;

import com.XSanGo.Protocol.AMD_ArenaRank_crossFight;
import com.XSanGo.Protocol.AMD_ArenaRank_crossRevenge;
import com.XSanGo.Protocol.AMD_ArenaRank_enterCrossArena;
import com.XSanGo.Protocol.AMD_ArenaRank_getCrossRank;
import com.XSanGo.Protocol.AMD_ArenaRank_getCrossReport;
import com.XSanGo.Protocol.AMD_ArenaRank_refreshCrossRival;
import com.XSanGo.Protocol.AMD_ArenaRank_saveBattle;
import com.XSanGo.Protocol.ArenaReportView;
import com.XSanGo.Protocol.NoteException;

/**
 * 跨服竞技场
 * 
 * @author xiongming.li
 *
 */
public interface ICrossArenaControler {

	void enterCrossArena(AMD_ArenaRank_enterCrossArena __cb) throws NoteException;

	void refreshCrossRival(AMD_ArenaRank_refreshCrossRival __cb) throws NoteException;

	void getCrossRank(AMD_ArenaRank_getCrossRank __cb) throws NoteException;

	void setSignature(String signature) throws NoteException;

	void saveBattle(AMD_ArenaRank_saveBattle __cb) throws NoteException;

	void buyCrossChallenge() throws NoteException;

	void getCrossReport(AMD_ArenaRank_getCrossReport __cb) throws NoteException;

	void crossFight(AMD_ArenaRank_crossFight __cb, String rivalId) throws NoteException;

	void crossRevenge(AMD_ArenaRank_crossRevenge __cb, String rivalId) throws NoteException;

	/**
	 * 添加战斗记录
	 * 
	 * @param report
	 */
	void addCrossArenaLog(ArenaReportView report);
	
	void clearCrossCD() throws NoteException;
}

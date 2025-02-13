package com.morefun.XSanGo.timeBattle;

import com.XSanGo.Protocol.BattleChallengeResultView;
import com.XSanGo.Protocol.BattleTimesView;
import com.XSanGo.Protocol.CopyClearResultView;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.role.IRedPointNotable;

public interface ITimeBattleControler extends IRedPointNotable {
	/**
	 * 获取可挑战次数
	 * 
	 * @return
	 */
	BattleTimesView[] getChallengeTimes() throws NoteException;

	/**
	 * 开始挑战
	 * 
	 * @param formationId
	 *            部队ID
	 * @param id
	 *            关卡ID
	 * @return
	 * @throws NoteException
	 */
	BattleChallengeResultView beginChallenge(String formationId, int id,
			boolean isClear) throws NoteException;

	/**
	 * 打赢或者战败时调用 heroNum-剩余武将数量，返回星级
	 */
	int endChallenge(int heroNum, boolean isClear) throws NoteException;

	/**
	 * 退出战斗
	 * */
	void failChallenge() throws NoteException;

	/**
	 * 重置红点
	 */
	void resetRedPoint();

	/**
	 * 结束限时关卡
	 * 
	 * @param heroNum
	 * @param items
	 * @return
	 * @throws NoteException
	 */
	int endLimitChallenge(int heroNum, String items) throws NoteException;

	/**
	 * 扫荡
	 * 
	 * @param id
	 * @return
	 * @throws NoteException
	 */
	CopyClearResultView clear(int id) throws NoteException;
}

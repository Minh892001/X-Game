package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 比武大会，开始战斗
 * 
 * @author guofeng.qin
 */
@signalslot
public interface ITournamentBeginFight {
	/**
	 * 比武大会，开始战斗
	 * 
	 * @param opponentId
	 *            对手id
	 * @param reduceCount
	 *            减少的次数
	 * @param count
	 *            剩余次数
	 */
	void onBeginFight(String opponentId, int reduceCount, int count);
}

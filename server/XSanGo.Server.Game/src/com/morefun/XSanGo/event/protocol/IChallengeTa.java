package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 挑战TA事件
 * 
 * @author lixiongming
 *
 */
@signalslot
public interface IChallengeTa {
	/**
	 * 
	 * @param byChallengeId
	 *            被挑战者ID
	 */
	public void onChallengeTaWin(String byChallengeId);
}

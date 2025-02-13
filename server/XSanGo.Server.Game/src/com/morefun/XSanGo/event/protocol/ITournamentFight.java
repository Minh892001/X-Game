package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 比武大会，战斗
 * 
 * @author guofeng.qin
 */
@signalslot
public interface ITournamentFight {

	/**
	 * 战斗
	 * 
	 * @param opponentId
	 *            对手id
	 * @param winornot
	 *            胜败；1，胜；0，败
	 * @param extra
	 *            附加信息
	 */
	void onFight(String opponentId, int winornot, String extra);
}

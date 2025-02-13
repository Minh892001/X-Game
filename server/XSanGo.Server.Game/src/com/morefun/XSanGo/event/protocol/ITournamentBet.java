package com.morefun.XSanGo.event.protocol;

import com.morefun.XSanGo.db.game.RoleTournamentBet;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 比武大会押注
 * 
 * @author guofeng.qin
 */
@signalslot
public interface ITournamentBet {

	/**
	 * 比武大会押注
	 * 
	 * @param bet
	 *            押注详情
	 */
	void onBet(RoleTournamentBet bet);
}

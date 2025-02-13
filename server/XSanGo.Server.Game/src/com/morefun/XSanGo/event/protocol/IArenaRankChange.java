package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 竞技场排名变化(被打)
 * 
 * @author guofeng.qin
 */
@signalslot
public interface IArenaRankChange {
	/**
	 * 竞技场被打
	 * 
	 * @param opponentId
	 *            对手ID
	 * @param from
	 *            战前排名
	 * @param to
	 *            战后排名
	 * @param type
	 *            类型，0普通战斗；1复仇
	 * @param flag
	 *            胜负
	 */
	void onChange(String opponentId, int from, int to, int type, int flag);
}

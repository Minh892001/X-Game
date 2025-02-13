package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 公会副本结束挑战事件
 * 
 * @author lixiongming
 *
 */
@signalslot
public interface IFactionCopyEndChallenge {
	/**
	 * 公会副本结束挑战
	 * 
	 * @param roleId
	 * @param factionId
	 * @param copyId
	 * @param harm 本次伤害
	 */
	public void onFactionCopyEndChallenge(String roleId, String factionId, int copyId, int harm);
}

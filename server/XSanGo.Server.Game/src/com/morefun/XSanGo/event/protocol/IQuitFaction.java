package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 退出公会
 * @author lixiongming
 *
 */
@signalslot
public interface IQuitFaction {
	public void onQuitFaction(String factionId,String roleId);
}

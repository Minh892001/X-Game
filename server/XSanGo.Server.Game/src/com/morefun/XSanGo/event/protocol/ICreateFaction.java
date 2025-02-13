package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 创建公会事件
 * 
 * @author lixiongming
 *
 */
@signalslot
public interface ICreateFaction {
	public void onCreateFaction(String factionId);
}

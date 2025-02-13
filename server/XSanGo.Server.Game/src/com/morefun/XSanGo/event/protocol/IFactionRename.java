package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 公会改名事件
 * 
 * @author lixiongming
 *
 */
@signalslot
public interface IFactionRename {
	public void onFactionRename(String factionId, String oldName, String newName);
}

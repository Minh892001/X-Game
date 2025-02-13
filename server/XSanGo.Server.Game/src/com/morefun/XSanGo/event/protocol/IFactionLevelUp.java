package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 公会升级事件
 * 
 * @author lixiongming
 *
 */
@signalslot
public interface IFactionLevelUp {
	public void onFactionLevelUp(String factionId, int level);
}

package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;
/**
 * 拒绝加入公会
 * @author lixiongming
 *
 */
@signalslot
public interface IDenyJoin {
	public void onDenyJoin(String factionId,String roleId);
}

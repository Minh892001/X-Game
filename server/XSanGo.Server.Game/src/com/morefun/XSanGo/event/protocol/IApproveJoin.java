package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;
/**
 * 批准加入公会
 * @author lixiongming
 *
 */
@signalslot
public interface IApproveJoin {
	public void onApproveJoin(String factionId,String roleId);
}

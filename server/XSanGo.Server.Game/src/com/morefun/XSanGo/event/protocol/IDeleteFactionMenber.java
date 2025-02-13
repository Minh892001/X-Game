package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 删除公会会员
 * @author lixiongming
 *
 */
@signalslot
public interface IDeleteFactionMenber {
	public void onDeleteFactionMenber(String factionId,String roleId);
}

package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 领取俸禄
 * 
 * @author lixiongming
 *
 */
@signalslot
public interface IAddSalary {
	public void onAddSalary(String roleId, int yuanbao);
}

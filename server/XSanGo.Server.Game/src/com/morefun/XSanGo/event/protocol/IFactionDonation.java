package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 公会捐赠事件
 * 
 * @author lixiongming
 *
 */
@signalslot
public interface IFactionDonation {
	/**
	 * 公会捐赠事件
	 * @param roleId  捐赠者
	 * @param num     捐赠数量
	 */
	public void onFactionDonation(String roleId, int num);
}

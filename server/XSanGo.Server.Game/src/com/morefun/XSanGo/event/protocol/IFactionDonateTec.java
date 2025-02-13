package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 捐献1次任意科技
 * 
 * @author zhuzhi.yang
 */
@signalslot
public interface IFactionDonateTec {
	
	/**
	 * 捐献公会科技
	 * 
	 * */
	void onDonateTec();
}

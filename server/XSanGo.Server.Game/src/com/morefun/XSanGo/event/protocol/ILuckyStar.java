package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 运气王
 * 
 * @author guofeng.qin
 */
@signalslot
public interface ILuckyStar {

	/**
	 * 运气王
	 * */
	void onLuckyStar();
}

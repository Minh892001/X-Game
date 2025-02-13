package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 好友模块领行动力
 *
 */
@signalslot
public interface IAddVitFromSns {
	/**
	 * @param from
	 *            赠送者
	 */
	void onAddVitFromSns(String from);
}

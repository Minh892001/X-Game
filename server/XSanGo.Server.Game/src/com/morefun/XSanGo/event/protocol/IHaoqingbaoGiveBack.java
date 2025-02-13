package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 红包退回
 * 
 * @author guofeng.qin
 */
@signalslot
public interface IHaoqingbaoGiveBack {
	/**
	 * 红包退回
	 * 
	 * @param id
	 *            红包id
	 * @param num
	 *            退回金额
	 * @param after
	 *            退回后金额
	 */
	void onGiveBack(String id, int num, int after);
}

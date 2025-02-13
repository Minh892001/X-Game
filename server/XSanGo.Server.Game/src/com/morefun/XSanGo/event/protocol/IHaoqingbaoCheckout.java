package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 豪情宝提出
 * 
 * @author guofeng.qin
 */
@signalslot
public interface IHaoqingbaoCheckout {
	/**
	 * 豪情宝提出
	 * 
	 * @param num
	 *            提出金额
	 * */
	void onCheckout(int num);
}

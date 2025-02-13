package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 *
 * @author qinguofeng
 */
@signalslot
public interface IBuyFund {
	/**
	 * 购买基金
	 * 
	 * @param price
	 *            价格
	 * */
	void onBuyFund(int price);
}

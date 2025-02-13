package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;
/**
 * 铁匠铺紫装商城兑换
 * @author xiaojun.zhang
 *
 */
@signalslot
public interface ISmithyMallExchange {
	/**
	 * 铁匠铺紫装商城兑换
	 * @param itemId 道具ID
	 * @param cost 花费
	 */
	void onExchange(String itemId, int cost);
}

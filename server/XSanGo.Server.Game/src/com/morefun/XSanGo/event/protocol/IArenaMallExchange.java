/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 竞技场 商城兑换
 * 
 * @author lvmingtao
 */
@signalslot
public interface IArenaMallExchange {
	/**
	 * 竞技场 商城兑换
	 * @param itemId 道具ID
	 * @param cost 花费
	 */
	void onExchange(String itemId, int cost);
}

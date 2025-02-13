/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 竞技场 商城刷新
 * 
 * @author lvmingtao
 */

@signalslot
public interface IArenaMallRefresh {
	/**
	 * 
	 * @param num 刷新次数
	 * @param cost 刷新花费
	 * @param itemStr 刷新得到的物品
	 */
	void onRefresh(int num, int cost, String itemStr);
}

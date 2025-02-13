package com.morefun.XSanGo.event.protocol;

import java.util.Date;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 铁匠铺 商城刷新
 * 
 */

@signalslot
public interface ISmithyMallRefresh {
	/**
	 * 
	 * @param num 刷新次数 0表示自动刷新
	 * @param cost 刷新花费 0表示自动刷新，此时不计算刷新消耗
	 * @param itemStr 刷新得到的物品
	 * @param lastRefTime 上次刷新时间
	 */
	void onRefresh(int num, int cost, String itemStr, Date lastRefTime);
}

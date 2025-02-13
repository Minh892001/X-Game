package com.morefun.XSanGo.event.protocol;

import com.XSanGo.Protocol.Money;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 领取资源回收
 * 
 * @author guofeng.qin
 */
@signalslot
public interface IAcceptResourceBack {
	/**
	 * 领取资源回收
	 * 
	 * @param date
	 *            时间
	 * @param items
	 *            领取的物品
	 * @param money
	 *            货币数量
	 */
	void onAcceptResourceBack(String date, String items, Money money);
}

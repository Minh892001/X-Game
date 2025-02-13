/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 军令购买事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IBuyJunLing {

	/**
	 * @param todayNum
	 *            今天第几次购买
	 * @param yuanbao
	 *            花费元宝
	 * @param militaryOrder
	 *            获得军令数量
	 */
	void onBuyJunLing(int todayNum, int yuanbao, int militaryOrder);

}

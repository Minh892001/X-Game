package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 领取活跃度宝箱
 * 
 * @author guofeng.qin
 */
@signalslot
public interface IActPointReward {

	/**
	 * 领取活跃度宝箱
	 * 
	 * @param point
	 *            活跃度档位
	 */
	void onAccept(int point);
}

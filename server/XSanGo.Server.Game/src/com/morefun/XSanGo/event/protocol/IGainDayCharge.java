package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 领取日充值奖励
 * 
 * @author lixiongming
 *
 */
@signalslot
public interface IGainDayCharge {
	/**
	 * 领取日充值奖励
	 * @param charge 领取条件
	 * @param itemId 领取的物品
	 */
	public void onGainDayCharge(int charge, String itemId);
}

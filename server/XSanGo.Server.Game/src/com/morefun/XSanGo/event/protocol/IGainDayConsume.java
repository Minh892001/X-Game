package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 领取日消费奖励
 * 
 * @author lixiongming
 *
 */
@signalslot
public interface IGainDayConsume {
	/**
	 * 领取日消费奖励
	 * @param consume 领取条件
	 * @param itemId 领取的物品
	 */
	public void onGainDayConsume(int consume, String itemId);
}

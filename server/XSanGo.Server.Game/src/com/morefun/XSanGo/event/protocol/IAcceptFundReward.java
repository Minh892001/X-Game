package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 领取成长基金奖励
 * 
 * @author qinguofeng
 */
@signalslot
public interface IAcceptFundReward {
	/**
	 * 领取成长基金奖励
	 * 
	 * @param level
	 *            达到的等级
	 * @param money
	 *            奖励金额
	 * */
	void onAcceptFundReward(int level, int money);
}

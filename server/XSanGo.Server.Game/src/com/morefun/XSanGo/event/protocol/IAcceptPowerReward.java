package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 战力嘉奖
 * @author qinguofeng
 */
@signalslot
public interface IAcceptPowerReward {
	/**
	 * 领取战力嘉奖的奖励
	 * 
	 * @param power 达到的战力
	 * @param templateId 奖励宝箱的模版ID
	 * */
	void onAcceptPowerReward(int power, String templateId);
}

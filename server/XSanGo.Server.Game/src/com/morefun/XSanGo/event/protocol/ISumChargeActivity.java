/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 累计充值活动事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface ISumChargeActivity {

	/**
	 * 领取活动奖励
	 * 
	 * @param threshold
	 *            累计活动的领奖阈值
	 */
	void onReceiveSumReward(int threshold);

}

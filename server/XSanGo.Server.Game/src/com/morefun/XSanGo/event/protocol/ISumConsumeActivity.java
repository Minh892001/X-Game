/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 累计消费活动事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface ISumConsumeActivity {

	/**
	 * 领取活动奖励
	 * 
	 * @param threshold
	 *            累计活动的领奖阈值
	 */
	void onReceiveSumReward(int threshold);

}

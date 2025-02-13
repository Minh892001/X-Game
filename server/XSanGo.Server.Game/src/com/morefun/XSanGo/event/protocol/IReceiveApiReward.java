/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IReceiveApiReward
 * 功能描述：
 * 文件名：IReceiveApiReward.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * API活动领奖事件
 * 
 * @author zwy
 * @since 2015-12-24
 * @version 1.0
 */
@signalslot
public interface IReceiveApiReward {

	/**
	 * API领奖事件
	 * 
	 * @param actId 活动编号
	 * @param rewardId 奖励节点编号
	 */
	void onReceiveApiReward(int actId, int rewardId);
}

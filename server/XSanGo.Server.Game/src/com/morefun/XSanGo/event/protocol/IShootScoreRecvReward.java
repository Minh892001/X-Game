/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import java.util.Map;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 百步穿杨积分领奖事件
 * 
 * @author zhouming
 * 
 */
@signalslot
public interface IShootScoreRecvReward {

	/**
	 * 积分领奖
	 * 
	 * @param recvType
	 *            领取类型
	 * @param score
	 *            当日已经领取奖励的积分
	 * @param itemsMap
	 *            获取的物品列表
	 */
	void onShootScoreRecvReward(String score,
			Map<String, Integer> itemsMap);
}

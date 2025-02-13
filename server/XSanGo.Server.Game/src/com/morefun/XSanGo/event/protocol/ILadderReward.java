/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 群雄争霸 领取奖励
 * 
 * @author lvmingtao
 */

@signalslot
public interface ILadderReward {
	/**
	 * @param rewardId 奖励模板ID
	 */
	void onReward(int rewardId);
}

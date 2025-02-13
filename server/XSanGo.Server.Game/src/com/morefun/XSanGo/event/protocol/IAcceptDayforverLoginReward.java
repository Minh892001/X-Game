package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 领取每日登录奖励 永久
 * @author sunjie
 */
@signalslot
public interface IAcceptDayforverLoginReward {
	/**
	 * 领取冲级有奖的奖励
	 * 
	 * @param level 达到的等级
	 * @param templateId 奖励宝箱的模版ID
	 * */
	void onAcceptDayforverLoginReward(int level, String templateId);
}

package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 领取冲级有奖 奖励
 * @author qinguofeng
 */
@signalslot
public interface IAcceptLevelReward {
	/**
	 * 领取冲级有奖的奖励
	 * 
	 * @param level 达到的等级
	 * @param templateId 奖励宝箱的模版ID
	 * */
	void onAcceptLevelReward(int level, String templateId);
}

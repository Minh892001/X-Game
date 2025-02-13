package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 领取成就奖励事件
 * @author sunjie
 *
 */
@signalslot
public interface IGetAchieve {
	/**
	 * 领取成就奖励(完成成就)
	 */
	public void onGetAchieve(int achieveId);
}

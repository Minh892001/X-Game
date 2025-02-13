package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 发送奖励奖励事件
 * @author sunjie
 *
 */
@signalslot
public interface IGetShareAward {
	/**
	 * 发送奖励
	 */
	public void onGetAward(int taskId);
}

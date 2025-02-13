package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 领取任务奖励事件
 * @author lixiongming
 *
 */
@signalslot
public interface IGetTask {
	/**
	 * 领取任务
	 */
	public void onGetTask(int taskId, int actBefore, int actAfter, int change);
}

package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 领取七日任务奖励事件
 * @author sunjie
 *
 */
@signalslot
public interface IGetSevenTask {
	/**
	 * 
	 * @param type 1:普通每日任务 2：三星奖励 3：总星宝箱奖励
	 * @param id
	 */
	public void onGetSevenTask(int type,int id);
}

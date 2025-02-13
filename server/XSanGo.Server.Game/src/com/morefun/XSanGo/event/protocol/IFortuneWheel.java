package com.morefun.XSanGo.event.protocol;

import com.XSanGo.Protocol.ItemView;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 进行幸运大转盘
 * 
 * @author qinguofeng
 */
@signalslot
public interface IFortuneWheel {
	/**
	 * 进行幸运大转盘
	 * 
	 * @param count
	 *            转盘次数
	 * @param rewards
	 *            获得的奖励
	 * @param lastCount
	 *            剩余次数
	 */
	void onFortuneWheel(int count, ItemView[] rewards, int lastCount);
}

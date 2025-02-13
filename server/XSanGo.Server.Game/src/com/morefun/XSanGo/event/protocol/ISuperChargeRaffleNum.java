package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 抽奖次数
 * @author xiaojun.zhang
 *
 */
@signalslot
public interface ISuperChargeRaffleNum {

	/**
	 * 抽奖次数更新
	 * @param oldValue 充值前的抽奖次数
	 * @param newValue 充值后获取的抽奖次数
	 */
	public void onRaffleNumUpdate(int oldValue, int newValue);
}

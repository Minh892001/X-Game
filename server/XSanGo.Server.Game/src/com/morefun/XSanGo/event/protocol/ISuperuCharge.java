package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 领取超级充值
 * @author xiaojun.zhang
 *
 */
@signalslot
public interface ISuperuCharge {
	/**
	 * 领取充值奖励
	 * @param scriptId 脚本ID
	 */
	public void onReceiveChargeReward(int scriptId);
}

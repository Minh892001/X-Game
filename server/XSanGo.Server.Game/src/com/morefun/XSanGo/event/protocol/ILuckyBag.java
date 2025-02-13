package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 领取充值福袋
 * 
 * @author lixiongming
 *
 */
@signalslot
public interface ILuckyBag {
	/**
	 * 领取充值福袋
	 * @param type     0-当日 1-当月
	 * @param scriptId 脚本ID
	 */
	public void onReceiveLuckyBag(int type, int scriptId);
}

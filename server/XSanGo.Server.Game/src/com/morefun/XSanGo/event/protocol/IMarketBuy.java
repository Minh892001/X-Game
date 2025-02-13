package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 好友送军令
 * 
 * @author zhuzhi.yang
 */
@signalslot
public interface IMarketBuy {
	/**
	 * 好友送军令
	 * 
	 * @param sender
	 *            送出者
	 * @param accepter
	 *            接收者
	 * @param num
	 *            数量
	 * */
	void onSnsSendJunLing(String sender, String accepter, int num);
}

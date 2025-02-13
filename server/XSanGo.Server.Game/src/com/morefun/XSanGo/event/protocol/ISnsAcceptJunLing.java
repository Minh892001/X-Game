package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 好友领取军令
 * 
 * @author guofeng.qin
 */
@signalslot
public interface ISnsAcceptJunLing {
	/**
	 * 好友领取军令
	 * 
	 * @param accepter
	 *            领取者
	 * @param sender
	 *            发送者
	 * */
	void onSnsAcceptJunLing(String accepter, String sender);
}

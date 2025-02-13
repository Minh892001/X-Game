/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * @author BruceSu
 * 
 */
@signalslot
public interface IOffline {
	/**
	 * @param onlineInterval
	 *            本次在线时长毫秒数
	 */
	void onRoleOffline(long onlineInterval);
}

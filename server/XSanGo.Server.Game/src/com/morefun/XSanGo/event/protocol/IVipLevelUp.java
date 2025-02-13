/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * vip升级事件
 * 
 */
@signalslot
public interface IVipLevelUp {
	
	/**
	 * VIP升级
	 * @param newLevel 
	 */
	void onVipLevelUp(int newLevel);
}

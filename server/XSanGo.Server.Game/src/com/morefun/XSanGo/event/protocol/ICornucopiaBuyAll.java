/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 聚宝盆购买所有
 * 
 * @author lxm
 */
@signalslot
public interface ICornucopiaBuyAll {
	/**
	 * 聚宝盆购买所有
	 * @param yuanbao 花费元宝
	 */
	void onCornucopiaBuyAll(int yuanbao);
}

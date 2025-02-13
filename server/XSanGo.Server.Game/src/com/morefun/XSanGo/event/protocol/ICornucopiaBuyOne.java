/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 聚宝盆购买一项
 * 
 * @author lxm
 */
@signalslot
public interface ICornucopiaBuyOne {
	/**
	 * 聚宝盆购买一项
	 * 
	 * @param id
	 * @param yuanbao
	 *            花费元宝
	 */
	void onCornucopiaBuyOne(int id, int yuanbao);
}

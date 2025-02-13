/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 竞技场 设置嘲讽模式
 * 
 * @author lvmingtao
 */
@signalslot
public interface IArenaSneer {
	/**
	 * 
	 * @param sneerId 嘲讽ID
	 * @param sneerStr 嘲讽字符
	 * @param cost 嘲讽花费
	 */
	void onSet(int sneerId, String sneerStr, int cost);
}

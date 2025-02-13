/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 竞技场 清除CD
 * 
 * @author lvmingtao
 */

@signalslot
public interface IArenaClearCD {
	/**
	 * @param num 次数
	 * @param cost 花费
	 */
	void onClear(int num, int cost);
}

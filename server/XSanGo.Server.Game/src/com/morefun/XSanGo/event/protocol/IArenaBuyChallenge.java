/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 竞技场 购买挑战令
 * 
 * @author lvmingtao
 */

@signalslot
public interface IArenaBuyChallenge {
	/**
	 * @param num 数量
	 * @param count 次数
	 * @param cost 花费
	 */
	void onbuy(int num, int count, int cost);
}

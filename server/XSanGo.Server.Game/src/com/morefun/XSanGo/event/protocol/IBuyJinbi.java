/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 点金手事件
 * 
 * @author lvmingtao
 */

@signalslot
public interface IBuyJinbi {
	/**
	 * @param num 当天购买次数
	 * @param crit 暴击倍数
	 * @param jinbi 最后的得到金币的数量，乘以倍数之后的值
	 */
	void onbuy(int num, int crit, int jinbi);
}

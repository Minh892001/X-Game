/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 群雄争霸 购买挑战
 * 
 * @author lvmingtao
 */

@signalslot
public interface ILadderBuy {
	/**
	 * @param id 购买模板ID
	 * @param sumNum 购买总次数
	 */
	void onBuy(int id, int sumNum);
}

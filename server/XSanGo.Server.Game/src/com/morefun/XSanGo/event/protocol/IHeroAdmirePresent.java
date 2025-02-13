/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 名将仰慕 仰慕武将
 * 
 * @author lvmingtao
 */

@signalslot
public interface IHeroAdmirePresent {
	/**
	 * @param heroValue 名将的仰慕的值
	 * @param itemId 仰慕使用的道具
	 * @param num 道具的数量
	 */
	void onPresent(int heroValue, String itemId, int num);
}

/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 复仇 碎片掠夺
 * 
 * @author lvmingtao
 */

@signalslot
public interface IItemChipRevenge {
	/**
	 * 
	 * @param flag	复仇结果  0:失败，1：胜利
	 * @param itemChipId 得到物品的模板ID
	 */
	void onRevenge(int flag, String itemChipId);
}

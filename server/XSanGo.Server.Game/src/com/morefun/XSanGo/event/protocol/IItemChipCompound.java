/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 碎片 合成
 * 
 * @author lvmingtao
 */

@signalslot
public interface IItemChipCompound {
	/**
	 * @param itemChipId 合成得到物品的模板ID
	 */
	void onCompound(String itemChipId);
}

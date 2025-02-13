/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: ICompositeChestItemDraw
 * 功能描述：
 * 文件名：ICompositeChestItemDraw.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 领取复合宝箱产出道具事件
 * 
 * @author zwy
 * @since 2015-12-2
 * @version 1.0
 */
@signalslot
public interface ICompositeChestItemDraw {

	/**
	 * 领取复合宝箱产出道具事件
	 * 
	 * @param index
	 * @param itemId
	 * @param itemCode
	 * @param gainCode
	 * @param gainNum
	 */
	void doDrawCompositeChestItem(int index, String itemId, String itemCode, String gainCode, int gainNum);
}

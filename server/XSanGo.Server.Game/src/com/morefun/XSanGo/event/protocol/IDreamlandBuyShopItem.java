/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IDreamlandBuyShopItem
 * 功能描述：
 * 文件名：IDreamlandBuyShopItem.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 南华幻境物品兑换
 * 
 * @author weiyi.zhao
 * @since 2016-4-25
 * @version 1.0
 */
@signalslot
public interface IDreamlandBuyShopItem {

	/**
	 * 购买物品
	 * 
	 * @param id 唯一编号
	 * @param itemCode 物品代码
	 * @param itemNum 物品数量
	 */
	void onBuyShopItem(int id, String itemCode, int itemNum);
}

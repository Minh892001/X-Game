/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IDreamlandRefreshShop
 * 功能描述：
 * 文件名：IDreamlandRefreshShop.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 南华幻境 商店刷新
 * 
 * @author weiyi.zhao
 * @since 2016-4-25
 * @version 1.0
 */
@signalslot
public interface IDreamlandRefreshShop {

	/**
	 * 刷新事件
	 * 
	 * @param isFree 是否免费刷新（免费则为定点刷新）
	 * @param refreshNum 刷新次数
	 * @param items 旧的物品列表
	 * @param newItems 新的
	 */
	void onRefreshDreamlandShop(boolean isFree, int refreshNum, String items, String newItems);
}

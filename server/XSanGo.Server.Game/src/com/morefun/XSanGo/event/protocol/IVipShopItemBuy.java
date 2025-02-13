/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 购买VIP商城物品事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IVipShopItemBuy {

	/**
	 * @param code
	 *            物品模板编号
	 * @param count
	 *            数量
	 */
	void onBuyVipShopItem(String code, int count);

}

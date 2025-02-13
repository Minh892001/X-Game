/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import com.XSanGo.Protocol.CommodityView;
import com.morefun.XSanGo.trader.TraderType;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 购买召唤商人/名将商品事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface ITraderItemBuy {

	/**
	 * 购买召唤商人物品
	 * 
	 * @param item
	 *            商品信息
	 * @param traderType
	 *            商人类型
	 */
	void onItemBuy(CommodityView item, TraderType traderType);

}

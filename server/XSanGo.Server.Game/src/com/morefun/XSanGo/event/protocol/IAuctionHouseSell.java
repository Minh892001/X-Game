package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 *
 * @author qinguofeng
 */
@signalslot
public interface IAuctionHouseSell {

	/**
	 * 拍卖行上架商品
	 * 
	 * @param sellItemTemplateId
	 *            卖的物品的模版id
	 * @param num
	 *            卖的物品数量
	 * @param price
	 *            起拍价格
	 * @param fixedPrice
	 *            一口价, 为 -1 表示没有设置一口价
	 * */
	void onAuctionHouseSell(String sellItemTemplateId, int num, long price,
			long fixedPrice);
}

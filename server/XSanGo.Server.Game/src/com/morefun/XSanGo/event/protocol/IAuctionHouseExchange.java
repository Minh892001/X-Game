package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 *
 * @author qinguofeng
 */
@signalslot
public interface IAuctionHouseExchange {

	/**
	 * 兑换拍卖币
	 * 
	 * @param yuanbaoPrice
	 *            花费的元宝数
	 * @param paimaibiPrice
	 *            所得拍卖币数量
	 **/
	void onAuctionHouseExchange(int yuanbaoPrice, long paimaibiPrice);
}

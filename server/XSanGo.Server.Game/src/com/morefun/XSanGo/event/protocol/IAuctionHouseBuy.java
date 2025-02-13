package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 *
 * @author qinguofeng
 */
@signalslot
public interface IAuctionHouseBuy {
	/**
	 * 竞拍
	 * 
	 * @param buyItemTemplateId
	 *            买的物品的模版ID
	 * @param type
	 *            1表示普通的出价,2表示一口价
	 * @param price 付出的价格
	 * */
	void onAuctionHouseBuy(String buyItemTemplateId, int type, long price);
}

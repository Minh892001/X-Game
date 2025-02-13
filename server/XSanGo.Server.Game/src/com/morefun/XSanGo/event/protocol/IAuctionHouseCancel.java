package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 *
 * @author qinguofeng
 */
@signalslot
public interface IAuctionHouseCancel {
	/**
	 * 拍卖品下架
	 * 
	 * @param buyItemTemplateId
	 *            买的物品的模版ID
	 * @param num
	 *            数量
	 * */
	void onAuctionHouseCancel(String buyItemTemplateId, int num);
}

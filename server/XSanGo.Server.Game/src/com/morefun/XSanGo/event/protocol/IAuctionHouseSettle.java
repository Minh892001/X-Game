package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 *
 * @author qinguofeng
 */
@signalslot
public interface IAuctionHouseSettle {

	/**
	 * 拍卖行结算
	 * 
	 * @param sellerId
	 *            拍卖者ID
	 * @param bidderId
	 *            竞拍者ID
	 * @param templateId
	 *            拍卖品模版ID
	 * @param num
	 *            拍卖品数量
	 * @param price
	 *            拍卖品成交价格
	 * @param type
	 *            成交类型, 1 拍卖结束成交, 2一口价成交
	 * @param success
	 *            交易是否成功, 0 失败, 1 成功
	 * */
	void onAuctionHouseSettle(String sellerId, String bidderId,
			String templateId, int num, long price, int type, int success);
}

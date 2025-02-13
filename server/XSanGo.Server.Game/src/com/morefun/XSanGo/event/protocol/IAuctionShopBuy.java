package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 拍卖行兑换事件
 * @author lixiongming
 *
 */
@signalslot
public interface IAuctionShopBuy {
	void onAuctionShopBuy(int shopId);
}

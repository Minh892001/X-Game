package com.morefun.XSanGo.auction;

import com.morefun.XSanGo.util.DelayedTask;

/**
 *
 * @author qinguofeng
 * @date Apr 1, 2015
 */
public class AuctionHouseDelayedTask extends DelayedTask {

	private String auctionId;

	public AuctionHouseDelayedTask(String auctionId, long delayed) {
		super(delayed);
		this.auctionId = auctionId;
	}

	@Override
	public void run() {
		XsgAuctionHouseManager manager = XsgAuctionHouseManager.getInstance();
		// 结算拍卖结束的拍品
		manager.closeAuctionWithSettle(auctionId);
		// 把下一个结束的拍品加入延迟任务队列
		manager.startNextAuction();
	}

	public String getAuctionId() {
		return auctionId;
	}
}

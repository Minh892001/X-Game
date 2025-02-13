package com.morefun.XSanGo.auction;

import com.XSanGo.Protocol.AuctionBuyResView;
import com.XSanGo.Protocol.AuctionHouseView;
import com.XSanGo.Protocol.AuctionStoreView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;

/**
 * 拍卖行controller接口
 * 
 * @author qinguofeng
 * @date Mar 30, 2015
 */
public interface IAuctionHouseController {

	/**
	 * 增加拍卖币
	 * 
	 * @param price
	 *            增加数量, 为负数的话表示减少
	 * */
	void addAuctionMoney(long price) throws NoteException;

	/**
	 * 兑换拍卖币
	 * 
	 * @param money
	 * */
	long exchange(long money) throws NoteException, NotEnoughMoneyException,
			NotEnoughYuanBaoException;

	/**
	 * 获取拍卖品
	 * 
	 * @param startIndex
	 *            开始索引
	 * @param count
	 *            数量
	 * @param type
	 *            类型
	 * @param key
	 *            关键字
	 * @param quality
	 *            品质
	 * @param direction
	 *            排序的方向;0,按照上架时间正续;1,倒序
	 * */
	AuctionHouseView getAuctionHouseItems(int startIndex, int count, int type,
			String key, int quality, int direction);

	/**
	 * 获取我拍卖的拍卖品
	 * 
	 * @param startIndex
	 *            开始索引
	 * @param count
	 *            数量
	 * @param type
	 *            类型
	 * @param key
	 *            关键字
	 * @param quality
	 *            品质
	 * @param direction
	 *            排序的方向;0,按照上架时间正续;1,倒序
	 * */
	AuctionHouseView getMySellItems(int startIndex, int count, int type,
			String key, int quality, int direction);

	/**
	 * 获取我竞拍的拍卖品
	 * 
	 * @param startIndex
	 *            开始索引
	 * @param count
	 *            数量
	 * @param type
	 *            类型
	 * @param key
	 *            关键字
	 * @param quality
	 *            品质
	 * @param direction
	 *            排序的方向;0,按照上架时间正续;1,倒序
	 * */
	AuctionHouseView getMyBidItems(int startIndex, int count, int type,
			String key, int quality, int direction);

	/**
	 * 拍卖一件物品
	 * 
	 * @param id
	 *            拍卖品ID
	 * @param num
	 *            拍卖数量
	 * @param price
	 *            起拍价
	 * @param fixedPrice
	 *            一口价
	 * */
	void sell(String id, int num, long price, long fixedPrice)
			throws NoteException, NotEnoughMoneyException,
			NotEnoughYuanBaoException;

	/**
	 * 竞拍
	 * 
	 * @param id
	 *            拍卖品id
	 * @param type
	 *            出价类型(1,普通竞拍/2,一口价)
	 * */
	AuctionBuyResView buy(String id, int type) throws NoteException;

	/**
	 * 下架拍卖品
	 * 
	 * @param id
	 *            拍卖品ID
	 * */
	void cancelAuction(String id) throws NoteException;

	/**
	 * 获取当前拍卖币
	 * 
	 * @return
	 */
	long getAuctionMoney();

	AuctionStoreView getAuctionShops() throws NoteException;

	AuctionStoreView refreshAuctionShop() throws NoteException;

	long buyAuctionShop(int id) throws NoteException;
}

package com.morefun.XSanGo.auction;

import java.util.Date;

import com.morefun.XSanGo.db.game.AuctionHouseRecord;

/**
 * 竞价记录
 * 
 * @author qinguofeng
 * @date Apr 3, 2015
 */
public class XsgAuctionHouseRecord {

	private AuctionHouseRecord recordDB;

	private int subType;
	private int quality;
	private String name;
	// 拍卖品竞拍结束时间, 排序用
	private long itemEndTime;

	private XsgAuctionHouseRecord(XsgAuctionHouseItem auctionItem) {
		subType = auctionItem.getSubType();
		quality = auctionItem.getQuality();
		name = auctionItem.getName();
		itemEndTime = auctionItem.getEndTime();
	}

	public XsgAuctionHouseRecord(String id, XsgAuctionHouseItem auctionItem,
			String roleId, long price, int count, Date time) {
		this(auctionItem);
		recordDB = new AuctionHouseRecord(id, auctionItem.getDBItem(), roleId,
				price, count, time);
	}

	public XsgAuctionHouseRecord(XsgAuctionHouseItem auctionItem,
			AuctionHouseRecord record) {
		this(auctionItem);
		this.recordDB = record;
	}

	public String getId() {
		return recordDB.getId();
	}

	public String getRoleId() {
		return recordDB.getRoleId();
	}

	public String getAuctionId() {
		return recordDB.getAuctionItem().getId();
	}

	public AuctionHouseRecord getRecordDB() {
		return recordDB;
	}

	public void setCount(int count) {
		recordDB.setCount(count);
	}

	public void setPrice(long price) {
		recordDB.setPrice(price);
	}

	public void setTime(Date time) {
		recordDB.setTime(time);
	}

	/**
	 * @return the subType
	 */
	public int getSubType() {
		return subType;
	}

	/**
	 * @param subType
	 *            the subType to set
	 */
	public void setSubType(int subType) {
		this.subType = subType;
	}

	/**
	 * @return the quality
	 */
	public int getQuality() {
		return quality;
	}

	/**
	 * @param quality
	 *            the quality to set
	 */
	public void setQuality(int quality) {
		this.quality = quality;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public long getUpdateTime() {
		return recordDB.getTime().getTime();
	}

	public long getEndTime() {
		return itemEndTime;
	}
}

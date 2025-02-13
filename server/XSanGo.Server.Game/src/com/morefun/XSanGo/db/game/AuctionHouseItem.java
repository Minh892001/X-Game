package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * 拍卖行上架物品
 * 
 * @author qinguofeng
 * @date Mar 28, 2015
 */
@Entity
@Table(name = "auction_house_item")
public class AuctionHouseItem implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 拍卖行上架物品ID */
	private String id;
	/** 拍卖人 */
	private String sellerId;
	/** 起拍价 */
	private long basePrice;
	/** 一口价 */
	private long fixedPrice;
	/** 叫价次数 */
	private int bidNum;
	/** 当前叫价 */
	private long currentPrice;
	/** 当前叫价人 */
	private String bidRoleId;
	/** 上架时间 */
	private Date startTime;
	/** 停服时间 */
	private Date pauseTime;// 拍卖期间服务器停止的时间, 暂时不考虑此功能, 该字段暂时没用
	/** 拍卖结束时间 */
	private long endTime;
	/** 拍卖品种类 */
	private int type;
	/** 物品详情 */
	private String itemJsonStr;
	/** 物品数量 */
	private int num;
	/** 叫价记录 */
	private List<AuctionHouseRecord> records = new ArrayList<AuctionHouseRecord>();

	/**
	 * @return the id
	 */
	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the sellerId
	 */
	@Column(name = "role_id", nullable = false)
	public String getSellerId() {
		return sellerId;
	}

	/**
	 * @param sellerId
	 *            the sellerId to set
	 */
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	/**
	 * @return the basePrice
	 */
	@Column(name = "base_price", nullable = false, columnDefinition = "int default 0")
	public long getBasePrice() {
		return basePrice;
	}

	/**
	 * @param basePrice
	 *            the basePrice to set
	 */
	public void setBasePrice(long basePrice) {
		this.basePrice = basePrice;
	}

	/**
	 * @return the fixedPrice
	 */
	@Column(name = "fixed_price", nullable = false, columnDefinition = "int default -1")
	public long getFixedPrice() {
		return fixedPrice;
	}

	/**
	 * @param fixedPrice
	 *            the fixedPrice to set
	 */
	public void setFixedPrice(long fixedPrice) {
		this.fixedPrice = fixedPrice;
	}

	/**
	 * @return the bidNum
	 */
	@Column(name = "bid_num", nullable = false, columnDefinition = "int default 0")
	public int getBidNum() {
		return bidNum;
	}

	/**
	 * @param bidNum
	 *            the bidNum to set
	 */
	public void setBidNum(int bidNum) {
		this.bidNum = bidNum;
	}

	/**
	 * @return the currentPrice
	 */
	@Column(name = "current_price", nullable = false, columnDefinition = "int default 0")
	public long getCurrentPrice() {
		return currentPrice;
	}

	/**
	 * @param currentPrice
	 *            the currentPrice to set
	 */
	public void setCurrentPrice(long currentPrice) {
		this.currentPrice = currentPrice;
	}

	/**
	 * @return the bidRoleId
	 */
	@Column(name = "bid_role_id", nullable = true)
	public String getBidRoleId() {
		return bidRoleId;
	}

	/**
	 * @param bidRoleId
	 *            the bidRoleId to set
	 */
	public void setBidRoleId(String bidRoleId) {
		this.bidRoleId = bidRoleId;
	}

	/**
	 * @return the startTime
	 */
	@Column(name = "start_time", nullable = false)
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the pauseTime
	 */
	@Column(name="pause_time", nullable = true)
	public Date getPauseTime() {
		return pauseTime;
	}

	/**
	 * @param pauseTime the pauseTime to set
	 */
	public void setPauseTime(Date pauseTime) {
		this.pauseTime = pauseTime;
	}

	/**
	 * @return the endTime
	 */
	@Column(name="end_time", nullable = false)
	public long getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the type
	 */
	@Column(name = "item_type", nullable = false)
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the itemJsonStr
	 */
	@Column(name = "item_json", nullable = false, columnDefinition = "text")
	public String getItemJsonStr() {
		return itemJsonStr;
	}

	/**
	 * @param itemJsonStr
	 *            the itemJsonStr to set
	 */
	public void setItemJsonStr(String itemJsonStr) {
		this.itemJsonStr = itemJsonStr;
	}

	/**
	 * @return the num
	 */
	@Column(name = "item_num", nullable = false)
	public int getNum() {
		return num;
	}

	/**
	 * @param num
	 *            the num to set
	 */
	public void setNum(int num) {
		this.num = num;
	}

	/**
	 * @return the records
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "auctionItem")
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL)
	@Fetch(FetchMode.SELECT)
	public List<AuctionHouseRecord> getRecords() {
		return records;
	}

	/**
	 * @param records the records to set
	 */
	public void setRecords(List<AuctionHouseRecord> records) {
		this.records = records;
	}

}

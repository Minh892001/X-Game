package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 玩家参与叫价的物品
 * 
 * @author qinguofeng
 * @date Mar 30, 2015
 */
@Entity
@Table(name = "auction_house_record")
public class AuctionHouseRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	/** 拍卖品上架 ID */
	private AuctionHouseItem auctionItem;
	/** 出价人 */
	private String roleId;
	/** 出价 */
	private long price;
	/** 出价顺序(第几次出价) */
	private int count;
	/** 出价时间 */
	private Date time;

	public AuctionHouseRecord() {

	}

	public AuctionHouseRecord(String id, AuctionHouseItem auctionItem,
			String roleId, long price, int count, Date time) {
		super();
		this.id = id;
		this.auctionItem = auctionItem;
		this.roleId = roleId;
		this.price = price;
		this.count = count;
		this.time = time;
	}

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
	 * @return the price
	 */
	@Column(name = "price", nullable = false, columnDefinition = "int default 0")
	public long getPrice() {
		return price;
	}

	/**
	 * @return the auctionItem
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auction_id", nullable = false)
	public AuctionHouseItem getAuctionItem() {
		return auctionItem;
	}

	/**
	 * @param auctionItem
	 *            the auctionItem to set
	 */
	public void setAuctionItem(AuctionHouseItem auctionItem) {
		this.auctionItem = auctionItem;
	}

	/**
	 * @return the roleId
	 */
	@Column(name = "role_id", nullable = false)
	public String getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId
	 *            the roleId to set
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(long price) {
		this.price = price;
	}

	/**
	 * @return the count
	 */
	@Column(name = "count", nullable = false, columnDefinition = "int default 0")
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the time
	 */
	@Column(name = "time", nullable = false)
	public Date getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}

}

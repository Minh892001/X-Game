package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author qinguofeng
 * @date Apr 18, 2015
 */
@Entity
@Table(name = "auction_house_item_log")
public class AuctionHouseItemLog implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	/** 拍卖项目的ID */
	private String auctionId;
	/** 拍卖品ID */
	private String itemId;
	/** 拍卖品的附加属性序列化 */
	private String data;
	/** 更新日期 */
	private Date updateTime;

	/**
	 * @return the id
	 */
	@GenericGenerator(name = "generator", strategy = "identity")
	@GeneratedValue(generator = "generator")
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
	 * @return the auctionId
	 */
	@Column(name = "auction_id", nullable = false)
	public String getAuctionId() {
		return auctionId;
	}

	/**
	 * @param auctionId
	 *            the auctionId to set
	 */
	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
	}

	/**
	 * @return the itemId
	 */
	@Column(name = "item_id", nullable = false)
	public String getItemId() {
		return itemId;
	}

	/**
	 * @param itemId
	 *            the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the data
	 */
	@Column(name = "data", nullable = true)
	public String getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return the updateTime
	 */
	@Column(name = "update_time", nullable = false)
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 *            the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}

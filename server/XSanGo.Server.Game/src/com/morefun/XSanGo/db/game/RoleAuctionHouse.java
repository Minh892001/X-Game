package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 用户拥有的拍卖币
 * 
 * @author qinguofeng
 * @date Mar 30, 2015
 */
@Entity
@Table(name = "role_auction_house")
public class RoleAuctionHouse implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 用户ID */
	private String roleId;
	private Role role;
	/** 拍卖币数量 */
	private long money;
	/** 上次更新时间 */
	private Date updateTime;
	private int refreshShopTimes;
	private Date lastRefreshDate;
	private String shopItemJson;// AuctionShopView[]的JSON

	public RoleAuctionHouse() {

	}

	public RoleAuctionHouse(Role r, long money, Date time) {
		this.role = r;
		this.money = money;
		this.updateTime = time;
	}

	/**
	 * @return the id
	 */
	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "role"))
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "role_id", nullable = false)
	public String getRoleId() {
		return roleId;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setRoleId(String id) {
		this.roleId = id;
	}

	/**
	 * @return the role
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Role getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @return the money
	 */
	@Column(name = "money", nullable = false, columnDefinition = "int default 0")
	public long getMoney() {
		return money;
	}

	/**
	 * @param money
	 *            the money to set
	 */
	public void setMoney(long money) {
		this.money = money;
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

	@Column(name = "refresh_shop_times")
	public int getRefreshShopTimes() {
		return refreshShopTimes;
	}

	public void setRefreshShopTimes(int refreshShopTimes) {
		this.refreshShopTimes = refreshShopTimes;
	}

	@Column(name = "last_refresh_date")
	public Date getLastRefreshDate() {
		return lastRefreshDate;
	}

	public void setLastRefreshDate(Date lastRefreshDate) {
		this.lastRefreshDate = lastRefreshDate;
	}

	@Column(name="shop_item_json")
	public String getShopItemJson() {
		return shopItemJson;
	}

	public void setShopItemJson(String shopItemJson) {
		this.shopItemJson = shopItemJson;
	}

}

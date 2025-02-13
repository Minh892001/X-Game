package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
/**
 * 限时武将购买记录
 * 
 * @author lixiongming
 *
 */
@Entity
@Table(name = "role_limit_hero")
public class RoleLimitHero implements Serializable{
	private static final long serialVersionUID = -8883348285651572833L;
	private String id;
	private Role role;
	// 购买限时武将次数--控制伪随机
	private int buyCount;
	// 购买限时武将元宝--控制伪随机
	private int buyYuanbao;
	// 当天购买次数--控制前几次购买无vip等级限制
	private int todayBuyCount;
	private Date lastBuyDate;
	private int itemBuyCount;//当天用物品已购买次数

	public RoleLimitHero() {

	}

	public RoleLimitHero(String id, Role role, int buyCount, int buyYuanbao,
			int todayBuyCount, Date lastBuyDate) {
		super();
		this.id = id;
		this.role = role;
		this.buyCount = buyCount;
		this.buyYuanbao = buyYuanbao;
		this.todayBuyCount = todayBuyCount;
		this.lastBuyDate = lastBuyDate;
	}

	@Id
	@Column(name = "id", nullable = false, length = 64)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "buy_count", nullable = false)
	public int getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}

	@Column(name = "buy_yuanbao", nullable = false)
	public int getBuyYuanbao() {
		return buyYuanbao;
	}

	public void setBuyYuanbao(int buyYuanbao) {
		this.buyYuanbao = buyYuanbao;
	}

	@Column(name = "today_buy_count", nullable = false)
	public int getTodayBuyCount() {
		return todayBuyCount;
	}

	public void setTodayBuyCount(int todayBuyCount) {
		this.todayBuyCount = todayBuyCount;
	}

	@Column(name = "last_buy_date")
	public Date getLastBuyDate() {
		return lastBuyDate;
	}

	public void setLastBuyDate(Date lastBuyDate) {
		this.lastBuyDate = lastBuyDate;
	}

	@Column(name = "item_buy_count")
	public int getItemBuyCount() {
		return itemBuyCount;
	}

	public void setItemBuyCount(int itemBuyCount) {
		this.itemBuyCount = itemBuyCount;
	}

}

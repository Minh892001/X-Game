package com.morefun.XSanGo.db.game;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "role_vip_trader_item")
public class RoleVipTraderItem implements Serializable {
	private static final long serialVersionUID = 5711326837606640133L;

	private String id;
	private RoleVip vip;
	private String itemTid;
	private int count;
	private boolean boughtToday;

	public RoleVipTraderItem() {
	}

	public RoleVipTraderItem(String id, RoleVip vip, String itemTid, int count,
			boolean boughtToday) {
		this.id = id;
		this.vip = vip;
		this.itemTid = itemTid;
		this.count = count;
		this.boughtToday = boughtToday;
	}

	@Id
	@Column(name = "id", nullable = false, length = 64)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	public RoleVip getVip() {
		return this.vip;
	}

	public void setVip(RoleVip vip) {
		this.vip = vip;
	}

	@Column(name = "item_tid", nullable = false, length = 64)
	public String getItemTid() {
		return itemTid;
	}

	public void setItemTid(String itemTid) {
		this.itemTid = itemTid;
	}

	@Column(name = "count", nullable = false, length = 64)
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Column(name = "bought_today", nullable = false, length = 64)
	public boolean getBoughtToday() {
		return boughtToday;
	}

	public void setBoughtToday(boolean boughtToday) {
		this.boughtToday = boughtToday;
	}

}

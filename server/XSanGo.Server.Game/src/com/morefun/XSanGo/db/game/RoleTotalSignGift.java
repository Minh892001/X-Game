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
@Table(name = "role_total_sign_gift")
public class RoleTotalSignGift implements Serializable {
	private static final long serialVersionUID = -848340874658969306L;

	private String id;
	private Role role;
	private String giftId;
	private int month;

	public RoleTotalSignGift() {
	}

	public RoleTotalSignGift(String id, Role role, String giftId, int month) {
		super();
		this.id = id;
		this.role = role;
		this.giftId = giftId;
		this.month = month;
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
	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "gift_id", nullable = false, length = 64)
	public String getGiftId() {
		return giftId;
	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}

	@Column(name = "month", nullable = false, length = 64)
	public int getMonth() {
		return month;
	}

	public void setMonth(int m) {
		this.month = m;
	}

}

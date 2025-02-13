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
@Table(name = "role_vip_gift_packs")
public class RoleVipGiftPacks implements Serializable {
	private static final long serialVersionUID = 5711326837606640133L;

	private String id;
	private RoleVip vip;
	private int vipLevel;

	public RoleVipGiftPacks() {
	}

	public RoleVipGiftPacks(String id, RoleVip vip, int vipLevel) {
		this.id = id;
		this.vip = vip;
		this.vipLevel = vipLevel;
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

	@Column(name = "vip_level", nullable = false, length = 64)
	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

}

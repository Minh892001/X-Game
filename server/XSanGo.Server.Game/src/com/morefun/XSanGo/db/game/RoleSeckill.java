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

@Entity
@Table(name = "role_seckill")
public class RoleSeckill implements Serializable {
	private static final long serialVersionUID = -9181468973881831470L;
	private String id;
	private Role role;
	private int seckillId;// 秒杀的商品ID
	private Date seckillDate;

	public RoleSeckill() {

	}

	public RoleSeckill(String id, Role role, int seckillId, Date seckillDate) {
		super();
		this.id = id;
		this.role = role;
		this.seckillId = seckillId;
		this.seckillDate = seckillDate;
	}

	@Id
	@Column(name = "id", nullable = false, length = 64)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "seckill_id")
	public int getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(int seckillId) {
		this.seckillId = seckillId;
	}

	@Column(name = "seckill_date")
	public Date getSeckillDate() {
		return seckillDate;
	}

	public void setSeckillDate(Date seckillDate) {
		this.seckillDate = seckillDate;
	}

}

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
@Table(name = "role_market")
public class RoleMarket implements Serializable {

	private static final long serialVersionUID = 4633869070479377798L;
	private String id;
	private Role role;
	private int type;
	/** 是否用过首抽 */
	private boolean first;
	private int freeNum;
	private Date lastFreeTime;

	public RoleMarket() {

	}

	public RoleMarket(String id, Role role, int type) {
		this.id = id;
		this.role = role;
		this.type = type;
	}

	// Property accessors
	@Id
	@Column(name = "id", nullable = false)
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

	@Column(name = "type", nullable = false)
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Column(name = "free_num", nullable = false)
	public int getFreeNum() {
		return freeNum;
	}

	public void setFreeNum(int num) {
		this.freeNum = num;
	}

	@Column(name = "last_free_time")
	public Date getLastFreeTime() {
		return lastFreeTime;
	}

	public void setLastFreeTime(Date lastFreeTime) {
		this.lastFreeTime = lastFreeTime;
	}

	@Column(name = "first")
	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}
}

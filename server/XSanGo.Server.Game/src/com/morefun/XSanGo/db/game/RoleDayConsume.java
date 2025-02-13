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
@Table(name = "role_day_consume")
public class RoleDayConsume implements Serializable {
	private static final long serialVersionUID = -7495791846495615923L;
	private String id;
	private Role role;
	/** 领取阈值 */
	private int threshold;
	/** 领取时间 */
	private Date createDate;

	public RoleDayConsume() {

	}

	public RoleDayConsume(String id, Role role, int threshold, Date createDate) {
		super();
		this.id = id;
		this.role = role;
		this.threshold = threshold;
		this.createDate = createDate;
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

	@Column(name = "threshold", nullable = false)
	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}

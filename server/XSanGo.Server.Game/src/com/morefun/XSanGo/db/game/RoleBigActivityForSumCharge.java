/**
 * 
 */
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
 * big累计消费活动数据
 * 
 * @author sulingyun
 *
 */
@Entity
@Table(name = "role_big_activity_sum_charge")
public class RoleBigActivityForSumCharge implements Serializable {
	private static final long serialVersionUID = -4377977489892721947L;

	private String id;
	private Role role;
	/** 领取阈值 */
	private int threshold;
	/** 上次领取时间 */
	private Date lastReceiveTime;

	public RoleBigActivityForSumCharge() {
	}

	public RoleBigActivityForSumCharge(String id, Role role, int threshold,
			Date lastReceiveTime) {
		this.id = id;
		this.role = role;
		this.threshold = threshold;
		this.lastReceiveTime = lastReceiveTime;
	}

	@Id
	@Column(name = "id", nullable = false)
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

	@Column(name = "last_receive_time", nullable = false)
	public Date getLastReceiveTime() {
		return lastReceiveTime;
	}

	public void setLastReceiveTime(Date lastReceiveTime) {
		this.lastReceiveTime = lastReceiveTime;
	}
}

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
 * 玩家已领取的升级礼包
 */
@Entity
@Table(name = "role_up_gift")
public class RoleUpGift implements Serializable {
	private static final long serialVersionUID = 7711219041340145057L;
	private String id;
	private Role role;
	// private int activityType;// 活动类型
	private int infoId;// 具体礼包ID
	private Date time;// 领取时间

	public RoleUpGift() {

	}

	public RoleUpGift(String id, Role role, int infoId, Date time) {
		this.id = id;
		this.role = role;
		// this.activityType = activityType;
		this.infoId = infoId;
		this.time = time;
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
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	// @Column(name = "activity_type", nullable = false)
	// public int getActivityType() {
	// return activityType;
	// }
	//
	// public void setActivityType(int activityType) {
	// this.activityType = activityType;
	// }

	@Column(name = "info_id", nullable = false)
	public int getInfoId() {
		return infoId;
	}

	public void setInfoId(int infoId) {
		this.infoId = infoId;
	}

	@Column(name = "time", nullable = false, length = 19)
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}

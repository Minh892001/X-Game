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
@Table(name = "role_task_apoint")
public class RoleTaskActive implements Serializable {
	private static final long serialVersionUID = -9181468973881831470L;
	private String id;
	private Role role;
	private int point;
	private String received;
	private Date recDate;
	private Date updateDate;
	private int maxQualityLevel;// 有武将进阶到过的最高等级

	public RoleTaskActive() {

	}

	public RoleTaskActive(String id, Role role, int point, String received, Date recDate) {
		super();
		this.id = id;
		this.role = role;
		this.point = point;
		this.received = received;
		this.recDate = recDate;
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
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "received")
	public String getReceived() {
		return received;
	}

	public void setReceived(String received) {
		this.received = received;
	}

	@Column(name = "rec_time")
	public Date getRecDate() {
		return recDate;
	}

	public void setRecDate(Date recDate) {
		this.recDate = recDate;
	}

	@Column(name = "point")
	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	@Column(name = "update_time")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(name = "max_quality_level")
	public int getMaxQualityLevel() {
		return maxQualityLevel;
	}

	public void setMaxQualityLevel(int maxQualityLevel) {
		this.maxQualityLevel = maxQualityLevel;
	}

}

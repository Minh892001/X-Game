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
@Table(name = "role_achieve")
public class RoleAchieve implements Serializable {
	private static final long serialVersionUID = -9181468973881831470L;
	private String id;
	private Role role;
	private String type;
	private String progress;
	private String continuedayinfo;
	private String received;
	private Date recDate;
	private Date updateDate;

	public RoleAchieve() {

	}

	public RoleAchieve(String id, Role role, String type, String progress,String received,Date recDate,Date updateDate) {
		super();
		this.id = id;
		this.role = role;
		this.type = type;
		this.progress = progress;
		this.received = received;
		this.recDate = recDate;
		this.updateDate = updateDate;
	}

	
	public RoleAchieve(String id, Role role, String type, String progress,
			Date updateDate) {
		super();
		this.id = id;
		this.role = role;
		this.type = type;
		this.progress = progress;
		this.updateDate = updateDate;
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

	@Column(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "progress")
	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
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

	@Column(name = "update_time")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(name = "continue_day_info")
	public String getContinuedayinfo() {
		return continuedayinfo;
	}

	public void setContinuedayinfo(String continuedayinfo) {
		this.continuedayinfo = continuedayinfo;
	}
	
	
}

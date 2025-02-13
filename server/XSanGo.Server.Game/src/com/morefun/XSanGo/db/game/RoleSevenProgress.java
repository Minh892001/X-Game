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
@Table(name = "role_seven_progress")
public class RoleSevenProgress implements Serializable {
	private static final long serialVersionUID = -9181468973881831470L;
	private String id;
	private Role role;
	private int sId;
	private String progress;
	private Date recDate;

	public RoleSevenProgress() {

	}

	public RoleSevenProgress(String id, Role role, int sId, String progress,Date recDate) {
		super();
		this.id = id;
		this.role = role;
		this.progress = progress;
		this.recDate = recDate;
		this.sId = sId;
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
	
	@Column(name = "s_id")
	public int getsId() {
		return sId;
	}

	public void setsId(int sId) {
		this.sId = sId;
	}

	@Column(name = "progress")
	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	@Column(name = "rec_time")
	public Date getRecDate() {
		return recDate;
	}

	public void setRecDate(Date recDate) {
		this.recDate = recDate;
	}
	
}

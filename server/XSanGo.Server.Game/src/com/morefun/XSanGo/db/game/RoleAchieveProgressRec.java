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
@Table(name = "role_achieve_progress_rec")
public class RoleAchieveProgressRec implements Serializable {
	private static final long serialVersionUID = -9181468973881831470L;
	private String id;
	private Role role;
	private String received;
	private Date updateDate;

	public RoleAchieveProgressRec() {

	}

	public RoleAchieveProgressRec(String id, Role role, String received, Date updateDate) {
		super();
		this.id = id;
		this.role = role;
		this.received = received;
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

	@Column(name = "received")
	public String getReceived() {
		return received;
	}

	public void setReceived(String received) {
		this.received = received;
	}

	@Column(name = "update_time")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}

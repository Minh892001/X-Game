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
@Table(name = "role_announce")
public class RoleAnnounce implements Serializable {

	private static final long serialVersionUID = -7955937320748120523L;
	private String id;
	private Role role;
	private int announceId;

	public RoleAnnounce() {
	}

	public RoleAnnounce(String id, Role role, int announceId) {
		this.id = id;
		this.role = role;
		this.announceId = announceId;
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

	@Column(name = "announce_id", nullable = false)
	public int getAnnounceId() {
		return announceId;
	}

	public void setAnnounceId(int announceId) {
		this.announceId = announceId;
	}
}

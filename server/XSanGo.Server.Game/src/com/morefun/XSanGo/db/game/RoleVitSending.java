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
@Table(name = "role_vit_send")
public class RoleVitSending implements Serializable {
	private static final long serialVersionUID = -848340874658969306L;

	private String id;
	private Role role;
	private String targetRoleId;
	private String sendDate;

	public RoleVitSending() {
	}

	public RoleVitSending(String id, Role role, String targetRoleId,
			String sendDate) {
		super();
		this.id = id;
		this.role = role;
		this.targetRoleId = targetRoleId;
		this.sendDate = sendDate;
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
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "target", nullable = false, length = 64)
	public String getTargetRoleId() {
		return targetRoleId;
	}

	public void setTargetRoleId(String targetRoleId) {
		this.targetRoleId = targetRoleId;
	}

	@Column(name = "send_date", nullable = false, length = 64)
	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

}

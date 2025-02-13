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
@Table(name = "role_vit_receive")
public class RoleVitReceiving implements Serializable {
	private static final long serialVersionUID = -848340874658969306L;

	private String id;
	private Role role;
	private String senderRoleId;
	private String receiveDate;
	private String sendDate;

	public RoleVitReceiving() {
	}

	public RoleVitReceiving(String id, Role role, String senderRoleId,
			String sendDate, String receiveDate) {
		super();
		this.id = id;
		this.role = role;
		this.senderRoleId = senderRoleId;
		this.sendDate = sendDate;
		this.receiveDate = receiveDate;
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

	@Column(name = "sender", nullable = false, length = 64)
	public String getSenderRoleId() {
		return senderRoleId;
	}

	public void setSenderRoleId(String sender) {
		this.senderRoleId = sender;
	}

	@Column(name = "receive_date", length = 64)
	public String getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}

	@Column(name = "send_date", nullable = false, length = 64, columnDefinition = "varchar(64) default '20101001'")
	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

}

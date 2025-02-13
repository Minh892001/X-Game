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
 * 用户邀请记录
 * 
 * @author lixiongming
 *
 */
@Entity
@Table(name = "role_invite_log")
public class RoleInviteLog implements Serializable {
	private static final long serialVersionUID = 2286298952818408639L;
	private String id;
	private Role role;
	private String mac;
	private Date createDate;
	private String targetId;

	public RoleInviteLog() {
	}

	public RoleInviteLog(String id, Role role, String mac, Date createDate,
			String targetId) {
		super();
		this.id = id;
		this.role = role;
		this.mac = mac;
		this.createDate = createDate;
		this.targetId = targetId;
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

	@Column(name = "mac")
	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "target_id", nullable = false)
	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

}

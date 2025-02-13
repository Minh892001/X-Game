/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 加入帮派申请
 * 
 * @author BruceSu
 * 
 */
@Entity
@Table(name = "faction_req")
public class FactionReq implements Serializable {
	private static final long serialVersionUID = 8443343943906173395L;
	private String id;
	private String factionId;
	private String roleId;
	private Date reqDate = new Date();// 申请时间

	public FactionReq() {
	}

	public FactionReq(String id, String factionId, String roleId) {
		this.id = id;
		this.factionId = factionId;
		this.setRoleId(roleId);
		this.reqDate = new Date();
	}

	// Property accessors
	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "faction_id", nullable = false)
	public String getFactionId() {
		return factionId;
	}

	public void setFactionId(String factionId) {
		this.factionId = factionId;
	}

	@Column(name = "role_id", nullable = false)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Column(name = "req_date")
	public Date getReqDate() {
		return reqDate;
	}

	public void setReqDate(Date reqDate) {
		this.reqDate = reqDate;
	}

}

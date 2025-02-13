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
@Table(name = "role_open_server_active")
public class RoleOpenServerActive implements Serializable {
	private static final long serialVersionUID = -9181468973881831470L;
	private String id;
	private Role role;
	private int nodeId;
	private String progress;
	private Date recDate;
	private Date updateDate;

	public RoleOpenServerActive() {

	}

	public RoleOpenServerActive(String id, Role role, int nodeId, String progress, Date updateDate) {
		super();
		this.id = id;
		this.role = role;
		this.nodeId = nodeId;
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

	@Column(name = "update_time")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	@Column(name = "active_node_id")
	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}
}

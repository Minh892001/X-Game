package com.morefun.XSanGo.db.stat;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stat_copy_progress")
public class StatCopyProgress implements Serializable {
	private long id;
	private Date statDate;
	private int serverId;
	private int copyId;
	private int roleCount;

	public StatCopyProgress(Date statDate, int serverId, int copyId,
			int roleCount) {
		this.statDate = statDate;
		this.serverId = serverId;
		this.copyId = copyId;
		this.roleCount = roleCount;
	}

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "stat_date", nullable = false, columnDefinition = "date not null")
	public Date getStatDate() {
		return statDate;
	}

	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}

	@Column(name = "server_id", nullable = false)
	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	@Column(name = "copy_id", nullable = false)
	public int getCopyId() {
		return copyId;
	}

	public void setCopyId(int copyId) {
		this.copyId = copyId;
	}

	@Column(name = "role_count", nullable = false)
	public int getRoleCount() {
		return roleCount;
	}

	public void setRoleCount(int roleCount) {
		this.roleCount = roleCount;
	}

}

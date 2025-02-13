package com.morefun.XSanGo.db.game;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "worship_rank")
public class WorshipRank implements Serializable{
	private static final long serialVersionUID = 7997832078468046098L;
	private String id;
	private String roleId;
	private int worshipCount;
	private String roleName;

	public WorshipRank() {

	}

	public WorshipRank(String id, String roleId, int worshipCount,
			String roleName) {
		this.id = id;
		this.roleId = roleId;
		this.worshipCount = worshipCount;
		this.roleName = roleName;
	}

	@Id
	@Column(name = "id", nullable = false, length = 64)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "role_id")
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Column(name = "worship_count", nullable = false)
	public int getWorshipCount() {
		return worshipCount;
	}

	public void setWorshipCount(int worshipCount) {
		this.worshipCount = worshipCount;
	}

	@Column(name = "role_name")
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}

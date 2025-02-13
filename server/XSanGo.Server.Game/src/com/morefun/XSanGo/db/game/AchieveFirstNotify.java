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
 * 成就全服公告  数据库结构
 * 
 * @author sunjie
 * 
 */
@Entity
@Table(name = "achieve_first_notify")
public class AchieveFirstNotify implements java.lang.Cloneable, Serializable {
	private static final long serialVersionUID = -9181468973881831470L;
	private String id;
	private int achieveId;
	private String roleId;
	private Date updateDate;

	public AchieveFirstNotify() {

	}

	public AchieveFirstNotify(String id, int achieveId, String roleId, Date updateDate) {
		super();
		this.id = id;
		this.achieveId = achieveId;
		this.roleId = roleId;
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

	@Column(name = "achieve_id")
	public int getAchieveId() {
		return achieveId;
	}

	public void setAchieveId(int achieveId) {
		this.achieveId = achieveId;
	}

	@Column(name = "role_id")
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Column(name = "update_time")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}

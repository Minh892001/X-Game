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
 * 统计用户每天是否登陆过,有记录则表示登陆过
 * 
 * @author guofeng.qin
 */
@Entity
@Table(name = "role_daily_login")
public class RoleDailyLoginStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private Role role;

	private String dateTag;
	private Date updateTime;

	public RoleDailyLoginStatus() {
		super();
	}

	public RoleDailyLoginStatus(String id, Role role, String dateTag, Date updateTime) {
		super();
		this.id = id;
		this.role = role;
		this.dateTag = dateTag;
		this.updateTime = updateTime;
	}

	/**
	 * @return the id
	 */
	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the role
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	public Role getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @return the dateTag
	 */
	@Column(name = "date_tag", nullable = false)
	public String getDateTag() {
		return dateTag;
	}

	/**
	 * @param dateTag
	 *            the dateTag to set
	 */
	public void setDateTag(String dateTag) {
		this.dateTag = dateTag;
	}

	/**
	 * @return the updateTime
	 */
	@Column(name = "update_time", nullable = false)
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 *            the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}

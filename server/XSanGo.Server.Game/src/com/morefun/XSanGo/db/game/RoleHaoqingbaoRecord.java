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
 * 用户豪情宝账户操作记录
 * 
 * @author guofeng.qin
 */
@Entity
@Table(name = "role_haoqingbao_record")
public class RoleHaoqingbaoRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;

	private Role role;

	private int yuanbaoNum;
	private String description;
	private Date updateTime;

	public RoleHaoqingbaoRecord(String id, Role role, int yuanbaoNum,
			String description, Date updateTime) {
		super();
		this.id = id;
		this.role = role;
		this.yuanbaoNum = yuanbaoNum;
		this.description = description;
		this.updateTime = updateTime;
	}

	public RoleHaoqingbaoRecord() {
		super();
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
	 * @return the yuanbaoNum
	 */
	@Column(name = "yuanbao_num", nullable = false)
	public int getYuanbaoNum() {
		return yuanbaoNum;
	}

	/**
	 * @param yuanbaoNum
	 *            the yuanbaoNum to set
	 */
	public void setYuanbaoNum(int yuanbaoNum) {
		this.yuanbaoNum = yuanbaoNum;
	}

	/**
	 * @return the description
	 */
	@Column(name = "description", nullable = false)
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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

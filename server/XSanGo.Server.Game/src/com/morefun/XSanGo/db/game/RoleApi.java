/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: RoleApi
 * 功能描述：
 * 文件名：RoleApi.java
 **************************************************
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
 * api记录
 * 
 * @author zhangwei02.zhang
 * @since 2015年11月11日
 * @version 1.0
 */
@Entity
@Table(name = "role_api")
public class RoleApi implements Serializable {
	private static final long serialVersionUID = -9118000212732020732L;

	/** 唯一编号 */
	private String id;

	/** 活动id */
	private int actId;

	/** 角色对象 */
	private Role role;

	/** 进度 计数类 记录总数量，返回类：id组 */
	private String process;

	/** 领奖记录 */
	private String rewardsHistory;

	/** 时间 */
	private Date time;

	public RoleApi() {
	}

	public RoleApi(String id, int actId, Role role, String process, String rewardsHistory, Date time) {
		super();
		this.id = id;
		this.actId = actId;
		this.role = role;
		this.process = process;
		this.rewardsHistory = rewardsHistory;
		this.time = time;
	}

	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "active_id", nullable = false)
	public int getActId() {
		return actId;
	}

	public void setActId(int actId) {
		this.actId = actId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @return Returns the process.
	 */
	@Column(name = "process", nullable = false)
	public String getProcess() {
		return process;
	}

	/**
	 * @param process The process to set.
	 */
	public void setProcess(String process) {
		this.process = process;
	}

	@Column(name = "rewards_history", nullable = true)
	public String getRewardsHistory() {
		return rewardsHistory;
	}

	public void setRewardsHistory(String rewardsHistory) {
		this.rewardsHistory = rewardsHistory;
	}

	/**
	 * @return Returns the time.
	 */
	@Column(name = "time", nullable = false)
	public Date getTime() {
		return time;
	}

	/**
	 * @param time The time to set.
	 */
	public void setTime(Date time) {
		this.time = time;
	}
}

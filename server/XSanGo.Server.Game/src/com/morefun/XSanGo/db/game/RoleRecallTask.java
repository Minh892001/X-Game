/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: RoleRecallTask
 * 功能描述：
 * 文件名：RoleRecallTask.java
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
 * 老友召回任务
 * 
 * @author zhangwei02.zhang
 * @since 2015年10月28日
 * @version 1.0
 */
@Entity
@Table(name = "role_recall_task")
public class RoleRecallTask implements Serializable {

	private static final long serialVersionUID = 908451987396267874L;

	private String id;

	private Role role;

	/** 任务模板ID */
	private int taskId;

	/** 0: 未完成; 1:已完成,待领奖; 2:已领奖 */
	private int state;

	private Date createTime;

	public RoleRecallTask() {

	}

	public RoleRecallTask(String id, Role role, int taskId, int state, Date createTime) {
		super();
		this.id = id;
		this.role = role;
		this.taskId = taskId;
		this.state = state;
		this.createTime = createTime;
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

	@Column(name = "task_id", nullable = false)
	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	@Column(name = "state", nullable = false)
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Column(name = "create_time", nullable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}

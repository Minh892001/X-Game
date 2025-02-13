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
 * 角色的任务
 * 
 * @author lvmingtao
 *
 */
@Entity
@Table(name = "role_task")
public class RoleTask implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2036725580129793271L;

	private String id;
	private int taskId; // 任务模板ID
	private int num; // 完成任务的数量
	private int star; // 完成任务的星级
	private int state; // 任务状态 ,0:未完成，1：未领取，2：过时未完成，3：完成
	private Date taskDate; // 完成任务时间
	private Role role;

	/** default constructor */
	public RoleTask() {

	}

	/** full constructor */
	public RoleTask(String id, Role role, int taskId, int num, int state,
			Date taskDate) {
		this.id = id;
		this.role = role;
		this.taskId = taskId;
		this.num = num;
		this.state = state;
		this.taskDate = taskDate;
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

	@Column(name = "num", nullable = false)
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	@Column(name = "state", nullable = false)
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Column(name = "task_date", nullable = false)
	public Date getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}

	@Column(name = "star", nullable = false)
	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

}

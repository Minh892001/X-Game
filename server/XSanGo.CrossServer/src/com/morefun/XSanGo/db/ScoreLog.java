package com.morefun.XSanGo.db;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 历史积分记录
 * 
 * @author xiongming.li
 *
 */
@Entity
public class ScoreLog implements Serializable {

	private static final long serialVersionUID = -7622579291195350471L;
	private int id;
	private String roleId;
	private int score;

	public ScoreLog() {

	}

	public ScoreLog(String roleId, int score) {
		this.roleId = roleId;
		this.score = score;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}

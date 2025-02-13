/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 竞技场排行 数据库结构
 * 
 * @author 吕明涛
 * 
 */
@Entity
@Table(name = "arena_rank")
public class ArenaRank implements Serializable {
	
	private static final long serialVersionUID = 5412864647385745330L;
	
	private String roleId;
	private int rank;			//排名
	private boolean robot; 	//是否机器人

	
	public ArenaRank() {
	}

	public ArenaRank(String roleId, int rank, boolean robot) {
		this.roleId = roleId;
		this.rank = rank;
		this.robot = robot;
	}
	
	@Id
	@Column(name = "role_id", nullable = false)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Column(name = "rank", columnDefinition = "INT default 0")
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
	
	@Column(name = "robot")
	public boolean getRobot() {
		return robot;
	}

	public void setRobot(boolean robot) {
		this.robot = robot;
	}
}

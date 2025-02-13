/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattleRobot
 * 功能描述：
 * 文件名：FactionBattleRobot.java
 **************************************************
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 公会战机器人（怪物）
 * 
 * @author weiyi.zhao
 * @since 2016-5-6
 * @version 1.0
 */
@Entity
@Table(name = "faction_battle_robot")
public class FactionBattleRobot implements Serializable {
	private static final long serialVersionUID = 5523363059297837915L;

	/** 唯一编号 */
	private String id;

	/** 据点 */
	private int strongholdId;

	/** 机器人编号 */
	private String robotRoleId;

	/** DEBUFF等级 */
	private int debuffLvl;

	/** 被攻击等待结束时间 */
	private Date beAttackWaitEndTime;

	/** 时间 */
	private Date time;

	/**
	 * @return Returns the id.
	 */
	@Id
	@Column(name = "id")
	public String getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return Returns the strongholdId.
	 */
	@Column(name = "stronghold_id")
	public int getStrongholdId() {
		return strongholdId;
	}

	/**
	 * @param strongholdId The strongholdId to set.
	 */
	public void setStrongholdId(int strongholdId) {
		this.strongholdId = strongholdId;
	}

	/**
	 * @return Returns the robotRoleId.
	 */
	@Column(name = "robot_role_id")
	public String getRobotRoleId() {
		return robotRoleId;
	}

	/**
	 * @param robotRoleId The robotRoleId to set.
	 */
	public void setRobotRoleId(String robotRoleId) {
		this.robotRoleId = robotRoleId;
	}

	/**
	 * @return Returns the debuffLvl.
	 */
	@Column(name = "debuff_lvl")
	public int getDebuffLvl() {
		return debuffLvl;
	}

	/**
	 * @param debuffLvl The debuffLvl to set.
	 */
	public void setDebuffLvl(int debuffLvl) {
		this.debuffLvl = debuffLvl;
	}

	/**
	 * @return Returns the beAttackWaitEndTime.
	 */
	@Column(name = "be_attack_wait_endtime", nullable = true, length = 19)
	public Date getBeAttackWaitEndTime() {
		return beAttackWaitEndTime;
	}

	/**
	 * @param beAttackWaitEndTime The beAttackWaitEndTime to set.
	 */
	public void setBeAttackWaitEndTime(Date beAttackWaitEndTime) {
		this.beAttackWaitEndTime = beAttackWaitEndTime;
	}

	/**
	 * @return Returns the time.
	 */
	@Column(name = "time", nullable = true, length = 19)
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

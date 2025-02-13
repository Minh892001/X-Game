/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattleConfig
 * 功能描述：
 * 文件名：FactionBattleConfig.java
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
 * 公会战参数
 * 
 * @author weiyi.zhao
 * @since 2016-5-5
 * @version 1.0
 */
@Entity
@Table(name = "faction_battle_config")
public class FactionBattleConfig implements Serializable {
	private static final long serialVersionUID = 8756013815548236077L;

	/** 唯一编号 主键 */
	private String id;

	/** 上一轮公会战 */
	private String firstFactionId;

	/** 上一轮公会战第一名所在阵营 */
	private int firstCampId;

	/** 报名阵营序列 */
	private String enrollCampId;

	/** 机器人创建时间 */
	private Date robotCreateTime;

	/** 更新时间 */
	private Date updateTime;

	/**
	 * @return Returns the id.
	 */
	@Id
	@Column(name = "id", nullable = false)
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
	 * @return Returns the firstFactionId.
	 */
	@Column(name = "first_faction_id")
	public String getFirstFactionId() {
		return firstFactionId;
	}

	/**
	 * @param firstFactionId The firstFactionId to set.
	 */
	public void setFirstFactionId(String firstFactionId) {
		this.firstFactionId = firstFactionId;
	}

	/**
	 * @return Returns the firstCampId.
	 */
	@Column(name = "first_camp_id")
	public int getFirstCampId() {
		return firstCampId;
	}

	/**
	 * @param firstCampId The firstCampId to set.
	 */
	public void setFirstCampId(int firstCampId) {
		this.firstCampId = firstCampId;
	}

	/**
	 * @return Returns the enrollCampId.
	 */
	@Column(name = "enroll_camp_id")
	public String getEnrollCampId() {
		return enrollCampId;
	}

	/**
	 * @param enrollCampId The enrollCampId to set.
	 */
	public void setEnrollCampId(String enrollCampId) {
		this.enrollCampId = enrollCampId;
	}

	/**
	 * @return Returns the robotCreateTime.
	 */
	@Column(name = "robot_create_time", nullable = true, length = 19)
	public Date getRobotCreateTime() {
		return robotCreateTime;
	}

	/**
	 * @param robotCreateTime The robotCreateTime to set.
	 */
	public void setRobotCreateTime(Date robotCreateTime) {
		this.robotCreateTime = robotCreateTime;
	}

	/**
	 * @return Returns the updateTime.
	 */
	@Column(name = "update_time", nullable = true, length = 19)
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime The updateTime to set.
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}

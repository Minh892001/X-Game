/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattleLog
 * 功能描述：
 * 文件名：FactionBattleLog.java
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
 * 公会战日志
 * 
 * @author weiyi.zhao
 * @since 2016-3-4
 * @version 1.0
 */
@Entity
@Table(name = "faction_battle_log")
public class FactionBattleLog implements Serializable {
	private static final long serialVersionUID = 1405551719304760621L;

	/** 唯一编号 */
	private String id;

	/** 角色编号 */
	private String role_id;

	/** 日志类型 0战斗1挖宝 */
	private byte type;

	/** 日志KEY */
	private String logKey;

	/** 日志参数 */
	private String logValue;

	/** 生成时间 */
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
	 * @return Returns the role_id.
	 */
	@Column(name = "role_id")
	public String getRole_id() {
		return role_id;
	}

	/**
	 * @param role_id The role_id to set.
	 */
	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	/**
	 * @return Returns the type.
	 */
	@Column(name = "type")
	public byte getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(byte type) {
		this.type = type;
	}

	/**
	 * @return Returns the logKey.
	 */
	@Column(name = "log_key")
	public String getLogKey() {
		return logKey;
	}

	/**
	 * @param logKey The logKey to set.
	 */
	public void setLogKey(String logKey) {
		this.logKey = logKey;
	}

	/**
	 * @return Returns the logValue.
	 */
	@Column(name = "log_value")
	public String getLogValue() {
		return logValue;
	}

	/**
	 * @param logValue The logValue to set.
	 */
	public void setLogValue(String logValue) {
		this.logValue = logValue;
	}

	/**
	 * @return Returns the time.
	 */
	@Column(name = "time")
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

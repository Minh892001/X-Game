/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.db.stat;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Online entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "online")
public class Online implements java.io.Serializable {

	// Fields

	private int id;
	private Integer serverId;
	private Timestamp statTime;
	private int onlineCount;

	// Constructors

	/** default constructor */
	public Online() {
	}

	/** full constructor */
	public Online(int serverId, Timestamp statTime, int onlineCount) {
		this.serverId = serverId;
		this.statTime = statTime;
		this.onlineCount = onlineCount;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "server_id", nullable = false)
	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	@Column(name = "stat_time", nullable = false, length = 19)
	public Timestamp getStatTime() {
		return this.statTime;
	}

	public void setStatTime(Timestamp statTime) {
		this.statTime = statTime;
	}

	@Column(name = "online_count", nullable = false)
	public int getOnlineCount() {
		return this.onlineCount;
	}

	public void setOnlineCount(int onlineCount) {
		this.onlineCount = onlineCount;
	}

}
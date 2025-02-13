/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * GameServer entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "game_server")
public class GameServer implements java.io.Serializable {

	// Fields

	private int id;
	private String name;
	private boolean isNew;
	private String host;
	private int showId;
	/** 是否只有CP厂家可见 */
	private boolean cpShowOnly;
	/** 是否只有CP厂家可以进入 */
	private boolean cpEnterOnly;
	/** GM工具在线人数控制 */
	private int onlineLimit;
	/** 实际进入的服务器ID，默认为0，不为0则表示服务器已经被合并 */
	private int targetId;

	// Constructors

	/** default constructor */
	public GameServer() {
	}

	// Property accessors
	@Id
	@Column(name = "id", nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "is_new", nullable = false)
	public boolean getIsNew() {
		return this.isNew;
	}

	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
	}

	@Column(name = "host", nullable = false, length = 50)
	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@Column(name = "show_id", nullable = false)
	public int getShowId() {
		return showId;
	}

	public void setShowId(int showId) {
		this.showId = showId;
	}

	@Column(name = "cp_show_only", nullable = false)
	public boolean isCpShowOnly() {
		return cpShowOnly;
	}

	public void setCpShowOnly(boolean cpShowOnly) {
		this.cpShowOnly = cpShowOnly;
	}

	@Column(name = "cp_enter_only", nullable = false)
	public boolean isCpEnterOnly() {
		return cpEnterOnly;
	}

	public void setCpEnterOnly(boolean cpEnterOnly) {
		this.cpEnterOnly = cpEnterOnly;
	}

	@Column(name = "online_limit", nullable = false)
	public int getOnlineLimit() {
		return onlineLimit;
	}

	public void setOnlineLimit(int onlineLimit) {
		this.onlineLimit = onlineLimit;
	}

	@Column(name = "target_id")
	public int getTargetId() {
		return targetId;
	}

	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}
}
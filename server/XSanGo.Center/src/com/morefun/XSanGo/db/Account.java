/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Account entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "account")
public class Account implements java.io.Serializable {

	// Fields
	private int id;
	private String account;
	private String password;
	private int channelId;
	private Date createTime;
	private Date lastLoginTime;
	// private Date lastEnterGameTime;
	/** 最近登录服务器数据 */
	private String recentServers;
	private boolean valid;
	private String mobile;
	private int roleCount;
	private int active;
	private Date frozenExpireTime;
	/** 最高角色等级 */
	private int maxLevel;
	/** 充值次数 */
	private int chargeCount;
	/** 注册MAC地址 */
	private String registerMac;

	// Constructors

	/** default constructor */
	public Account() {
	}

	/** minimal constructor */
	public Account(int id, String account, String password, int channelId,
			String mac, Date createTime) {
		this.id = id;
		this.account = account;
		this.password = password;
		this.channelId = channelId;
		this.registerMac = mac;
		this.createTime = createTime;
		this.valid = false;
		this.roleCount = 0;
	}

	// Property accessors
	@Id
	@Column(name = "account", nullable = false, length = 32)
	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "password", nullable = false, length = 32)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "create_time", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "last_login_time", length = 19)
	public Date getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	// @Column(name = "last_enter_game_time", length = 19)
	// public Date getLastEnterGameTime() {
	// return this.lastEnterGameTime;
	// }
	//
	// public void setLastEnterGameTime(Date lastEnterGameTime) {
	// this.lastEnterGameTime = lastEnterGameTime;
	// }

	@Column(name = "valid", nullable = false)
	public boolean getValid() {
		return this.valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	@Column(name = "mobile", length = 11)
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "role_count", nullable = false)
	public int getRoleCount() {
		return this.roleCount;
	}

	public void setRoleCount(int roleCount) {
		this.roleCount = roleCount;
	}

	@Column(name = "id", nullable = false, unique = true)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "channel_id", nullable = false)
	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	@Column(name = "recent_servers")
	public String getRecentServers() {
		return recentServers;
	}

	public void setRecentServers(String recentServers) {
		this.recentServers = recentServers;
	}

	@Column(name = "active", nullable = false, columnDefinition = "int default 0")
	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	@Column(name = "forzen_expire_time", nullable = true)
	public Date getFrozenExpireTime() {
		return frozenExpireTime;
	}

	public void setFrozenExpireTime(Date forzenExpireTime) {
		this.frozenExpireTime = forzenExpireTime;
	}

	@Column(name = "max_level", nullable = false, columnDefinition = "int default 0")
	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	@Column(name = "charge_count", nullable = false, columnDefinition = "int default 0")
	public int getChargeCount() {
		return chargeCount;
	}

	public void setChargeCount(int chargeCount) {
		this.chargeCount = chargeCount;
	}

	@Transient
	public boolean isFrozen() {
		return this.frozenExpireTime != null
				&& this.frozenExpireTime.getTime() > System.currentTimeMillis();
	}

	@Column(name = "register_mac", nullable = false, columnDefinition = "varchar(64) default ''")
	public String getRegisterMac() {
		return registerMac;
	}

	public void setRegisterMac(String registerMac) {
		this.registerMac = registerMac;
	}
}
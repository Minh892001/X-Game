/**
 * 
 */
package com.morefun.XSanGo.db.stat;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 进出游戏日志
 * 
 * @author sulingyun
 * 
 */
@Entity
@Table(name = "login_out_log")
public class LoginAndOutLog implements Serializable {
	private long id;
	private int serverId;
	private String account;
	private int channel;
	private String roleId;
	private Date loginTime;
	private Date logoutTime;
	private int onlineInterval;

	public LoginAndOutLog(int serverId, String account, int channel,
			String roleId, Date loginTime, Date logoutTime, int onlineInterval) {
		this.serverId = serverId;
		this.account = account;
		this.channel = channel;
		this.roleId = roleId;
		this.loginTime = loginTime;
		this.logoutTime = logoutTime;
		this.onlineInterval = onlineInterval;
	}

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "server_id", nullable = false)
	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	@Column(name = "role_id", nullable = false)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Column(name = "login_time", nullable = false)
	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	@Column(name = "logout_time", nullable = false)
	public Date getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	@Column(name = "online_interval", nullable = false)
	public int getInterval() {
		return onlineInterval;
	}

	public void setInterval(int interval) {
		this.onlineInterval = interval;
	}

	@Column(name = "account", nullable = false)
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "channel", nullable = false)
	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}
}

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
 * @author sulingyun
 *
 */
@Entity
@Table(name = "login_log")
public class LoginLog implements Serializable {
	private long id;
	private String account;
	private int channel;
	private String mac;
	private String ip;
	private Date loginTime;
	private String clientVersion;
	/** 程序安装签名 */
	private String installSign;

	public LoginLog(String account, int channel, String mac, String ip,
			String clientVersion, Date loginTime, String sign) {
		this.account = account;
		this.channel = channel;
		this.mac = mac;
		this.ip = ip;
		this.loginTime = loginTime;
		this.clientVersion = clientVersion;
		this.installSign = sign;
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

	@Column(name = "account", nullable = false)
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "mac")
	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	@Column(name = "ip")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "login_time", nullable = false)
	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	@Column(name = "channel", nullable = false)
	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	@Column(name = "client_version")
	public String getClientVersion() {
		return clientVersion;
	}

	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}
	@Column(name = "install_sign")
	public String getInstallSign() {
		return installSign;
	}

	public void setInstallSign(String installSign) {
		this.installSign = installSign;
	}

}

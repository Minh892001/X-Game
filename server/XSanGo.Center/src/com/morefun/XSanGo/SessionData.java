/**
 * 
 */
package com.morefun.XSanGo;

/**
 * 玩家会话数据
 * 
 * @author sulingyun
 *
 */
public class SessionData {
	private String account;
	private String mac;
	private String ip;
	private String version;
	private int currentChannel;

	public SessionData(String account, String mac, String ip, String version, int currentChannel) {
		this.account = account;
		this.mac = mac;
		this.ip = ip;
		this.version = version;
		this.currentChannel = currentChannel;
	}

	public String getAccount() {
		return account;
	}

	public String getMac() {
		return mac;
	}

	public String getIp() {
		return ip;
	}

	public String getVersion() {
		return version;
	}

	public int getCurrentChannel() {
		return currentChannel;
	}
}

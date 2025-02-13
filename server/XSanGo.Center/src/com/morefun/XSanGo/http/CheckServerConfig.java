/**
 * 
 */
package com.morefun.XSanGo.http;

/**
 * 审核服务器配置
 * 
 * @author sulingyun
 *
 */
public class CheckServerConfig {
	/** 审核中的版本号 */
	private String checkingVersion;
	/** 审核服务器编号 */
	private int serverId;

	public CheckServerConfig(String checkingVersion, int serverId) {
		this.checkingVersion = checkingVersion;
		this.serverId = serverId;
	}

	public String getVersion() {
		return checkingVersion;
	}

	public int getServerId() {
		return serverId;
	}

}

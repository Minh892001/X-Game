/**
 * 
 */
package com.morefun.XSanGo.center;

import java.io.Serializable;

import com.XSanGo.Protocol.DeviceInfo;

/**
 * 授权数据
 * 
 * @author sulingyun
 *
 */
public class AuthData implements Serializable {

	/** 序列化版本号 */
	private static final long serialVersionUID = 1L;
	private String pwd;
	private DeviceInfo device;

	public AuthData(String password, DeviceInfo device) {
		this.pwd = password;
		this.device = device;
	}

	public String getPwd() {
		return pwd;
	}

	public DeviceInfo getDevice() {
		return device;
	}

}

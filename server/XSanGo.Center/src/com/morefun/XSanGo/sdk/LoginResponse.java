/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.sdk;

/**
 * SDK登录返回信息
 * 
 * @author sulingyun
 * 
 */
public class LoginResponse {
	private boolean success;
	private String message;

	public LoginResponse(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}
}

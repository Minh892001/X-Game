package com.morefun.XSanGo.MFBI;

import com.morefun.bi.sdk.RoleInfo;
import com.morefun.bi.sdk.UserInfo;

public class BIMessage {

	private int type;

	private long timestamps = System.currentTimeMillis();

	private Object[] params = new Object[0];

	private UserInfo userInfo;

	private RoleInfo roleInfo;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getTimestamps() {
		return timestamps;
	}

	public void setTimestamps(long timestamps) {
		this.timestamps = timestamps;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object... params) {
		this.params = params;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public RoleInfo getRoleInfo() {
		return roleInfo;
	}

	public void setRoleInfo(RoleInfo roleInfo) {
		this.roleInfo = roleInfo;
	}
}

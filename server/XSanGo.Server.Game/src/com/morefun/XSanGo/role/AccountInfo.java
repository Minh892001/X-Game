/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.role;

/**
 * @author BruceSu
 * 
 */
public class AccountInfo {
	private int id;
	private boolean invite;

	public AccountInfo(int id, boolean invite) {
		this.id = id;
		this.invite = invite;
	}

	public int getId() {
		return id;
	}

	public boolean isInvite() {
		return invite;
	}
}

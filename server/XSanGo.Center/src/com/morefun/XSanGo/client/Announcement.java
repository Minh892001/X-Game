/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.client;

/**
 * 登陆公告
 * 
 * @author BruceSu
 * 
 */
public class Announcement {
	private boolean show;
	private String content;

	public Announcement(boolean show, String content) {
		this.show = show;
		this.content = content;
	}

	public boolean isShow() {
		return show;
	}

	public String getContent() {
		return content;
	}
}

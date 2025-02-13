/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.sdk;

/**
 * SDK接口定义
 * 
 * @author sulingyun
 * 
 */
public interface ISdk {
	/**
	 * 验证用户登录状态
	 * 
	 * @param userName
	 * @param sessionId
	 * @param time 
	 * @return
	 */
	LoginResponse checkUserLogin(String userName, String sessionId, String time);
}

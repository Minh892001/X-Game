/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.http;

import com.morefun.XSanGo.sdk.ISdk;
import com.morefun.XSanGo.sdk.LoginResponse;

/**
 * @author WFY
 * 仅供开发测试环境使用，此时不验证帐号，直接返回成功
 */
public class DevelopSdk implements ISdk {

	@Override
	public LoginResponse checkUserLogin(String userName, String sessionId,
			String time) {
		LoginResponse res = new LoginResponse(true, "");
		return res;
	}

}

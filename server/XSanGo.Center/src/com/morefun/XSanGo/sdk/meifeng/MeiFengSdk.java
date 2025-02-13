/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.sdk.meifeng;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.morefun.XSanGo.LoginDatabase;
import com.morefun.XSanGo.sdk.ISdk;
import com.morefun.XSanGo.sdk.LoginResponse;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

public class MeiFengSdk implements ISdk {
	private static Log log = LogFactory.getLog(MeiFengSdk.class);
	private int appId = NumberUtil.parseInt(LoginDatabase.instance().getAc()
			.getBean("BI_gameId", String.class));
	// private String key = "morefuntek_yht_key_)!@#0!@#";
	// private String requestUrl = "http://192.168.1.44:18080/passport/common";
	private String requestUrl = LoginDatabase.instance().getAc()
			.getBean("MfLoginUrl", String.class);

	// "http://yht.morefuntek.com/common";

	@Override
	public LoginResponse checkUserLogin(String userName, String sign,
			String time) {
		// String sign = EncryptUtil
		// .bytesToHexString(
		// EncryptUtil.getMD5((userName
		// + System.currentTimeMillis() + this.key)
		// .getBytes())).toLowerCase();

		// noservlist 用于http登录协议2 ，空或者0为默认获取，1为不获取（假如服务器列表不放一号通的时候）
		// returntype 用于http所有协议, 空或者0为默认xml返回，1为json返回s
		LoginRequestExpansion expansion = new LoginRequestExpansion();
		expansion.time = time;
		expansion.sign = sign;
		String url = TextUtil
				.format("{0}?appid={1}&type=32&username={2}&expansion={3}&noservlist=1&returntype=1",
						this.requestUrl, this.appId, userName,
						TextUtil.GSON.toJson(expansion));

		boolean success = false;
		String msg = "";

		try {
			// {"status":"1","message":"成功","returntype":"json"}
			log.debug("Request:" + url);
			String text = httpGet(url, 5000);
			log.debug("Response:" + text);

			SdkResponse res = TextUtil.GSON.fromJson(text, SdkResponse.class);
			if (res.status.equals("1")) {
				success = true;
			} else {
				System.err.println(text);
			}
		} catch (Exception e) {
			msg = "网络异常";
			log.error(e, e);
		}

		return new LoginResponse(success, msg);
	}

	/**
	 * 发送GET请求并获取结果
	 * 
	 * @param address
	 * @param timeout
	 * @return
	 * @throws Exception
	 */
	private static String httpGet(String address, int timeout) throws Exception {
		StringBuffer readOneLineBuff = new StringBuffer();
		String content = "";
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		try {
			URL url = new URL(address);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(timeout);
			conn.setReadTimeout(timeout);
			reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "utf-8"));
			String line = "";
			while ((line = reader.readLine()) != null) {
				readOneLineBuff.append(line);
			}
			content = readOneLineBuff.toString();
		} finally {
			if (conn != null) {
				try {
					conn.disconnect();
				} catch (Exception e) {
					log.error("Disconnect yht connection error.", e);
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
					log.error("Close reader error.", e);
				}
			}
		}

		return content;
	}
}

class LoginRequestExpansion {
	public String time;
	public String sign;
}

class SdkResponse {
	public String status;
	public String message;
	public String returntype;
}

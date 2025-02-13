package com.morefun.XSanGo.sdk.miyu;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.morefun.XSanGo.http.HttpUtil;
import com.morefun.XSanGo.sdk.ISdk;
import com.morefun.XSanGo.sdk.LoginResponse;
import com.morefun.XSanGo.util.EncryptUtil;
import com.morefun.XSanGo.util.TextUtil;

public class MiYuSdk implements ISdk{
	private static Log log = LogFactory.getLog(MiYuSdk.class);
	private final int gameid = 1129;//米娱提供给游戏方的游戏 id
	private int userid = 0;//从 sdk 登录接口处获取 userid
	private String sessionid = "";//从 sdk 登录接口处获取 sessionid
	private String sign = "";//MD5 签名结果,签名示例 sign=md5(gameid=1234&sessionid=yrggrrs1223& userid=5345354:secret) 
	private final String secret  = "08be0c92204a925eb2385d541522f747";//米娱给定的密钥
	private String requestUrl = "http://sdkdrive.miyugame.com/CheckLogin";//米娱帐号验证链接
//	private String requestUrl = LoginDatabase.instance().getAc()
//			.getBean("MiYuLoginUrl", String.class);TODO 临时写死requestUrl

	@Override
	public LoginResponse checkUserLogin(String userName, String sessionId, String time) {
		String sign = null;
		boolean success = false;
		String msg = "";
		try {
			String toMD5 = TextUtil.format("gameid={0}&userid={1}&sessionid={2}:{3}", this.gameid, userName,sessionId,secret);

			try {
				sign = EncryptUtil.bytesToHexString(EncryptUtil.getMD5(toMD5.getBytes("UTF-8")));
			} catch (NoSuchAlgorithmException e1) {
				log.error(e1);
			}
			String body = TextUtil
					.format("gameid={0}&userid={1}&sessionid={2}&sign={3}",
							this.gameid, userName,sessionId,sign);
			//以 json 形式返还通知结果 成功示例： {"result":1} 失败示例： {"result":0,"msg":"sign 验证失败"}
//			log.debug("Request:" + url);
			String text = HttpUtil.doPost(requestUrl, body);
			log.debug("Response:" + text);
			Result res = TextUtil.GSON.fromJson(text, Result.class);
			if (res.result==1) {
				success = true;
			} else {
				System.err.println(text);
				msg = res.msg;
			}
		} catch (Exception e) {
			msg = "网络异常";
			log.error(e, e);
		}

		return new LoginResponse(success, msg);
	}
	
	private class Result{
		public int result;
		public String msg = null;
	}
	
	public static void main(String[] args) {
//		Result res = 
////				TextUtil.GSON.fromJson( "{\"result\":0,\"msg\":\"sign验证失败\"}", Result.class);
//				TextUtil.GSON.fromJson("{'result':1}", Result.class);
//		System.out.println(res.result + ":" + res.msg);
		
		LoginResponse response = new MiYuSdk().checkUserLogin("aaa","bbb", String.valueOf(new Date().getTime()));
	}
}

/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.http.handler;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.morefun.XSanGo.util.TextUtil;
import com.sun.net.httpserver.HttpExchange;

/**
 * 帐号中心未建立的情况下，模拟美峰一号通登录验证接口，仅限于射雕三国
 * 
 * @author wfy
 * 
 */
public class PassportHandler extends AbsHandler {
	private static Logger log = LogManager.getLogger(PassportHandler.class);
	
	private class LoginResult{
		public int status = 1;
		public String username = "";
		public String sign = "";
		public String time = "";
		public String channel_username = "";
	}
	
	private class CheckVersionResult{
		public  int isupdate=0;
		public String updateurl="";
		public int status=0;
		public String msg="";
	}
	
	@Override
	protected void innerHandle(HttpExchange httpExchange) throws IOException {
		Map<String, String> paramsMap = this.parseParamsMap(httpExchange);
		String type = paramsMap.get("type");
		
		StringBuffer sb = new StringBuffer();
		for(String key:paramsMap.keySet()){
			sb.append(key+"=");
			sb.append(paramsMap.get(key)+"\n");
		}
		log.debug(type);
		

		if(type!=null && type.equals("29")){
//			isupdate=0
//					updateurl=""
//					status=0
//					msg=""
			String verMsg = TextUtil.GSON.toJson(new CheckVersionResult());
			this.sendResponse(httpExchange, verMsg);
			return;
		}
		
		
//		status=1&username=username&sign=sign&time=tim&channel_username=channel_username
//		返回这些参数的json
		String responseMsg = TextUtil.GSON.toJson(new LoginResult());
		this.sendResponse(httpExchange, responseMsg);
	}

	
}

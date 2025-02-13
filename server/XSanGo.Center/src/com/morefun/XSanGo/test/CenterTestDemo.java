package com.morefun.XSanGo.test;

import com.morefun.XSanGo.LoginDatabase;
import com.morefun.XSanGo.http.HttpUtil;
import com.morefun.XSanGo.util.TextUtil;

public class CenterTestDemo {

	public static void main(String[] args) {

		String content = TextUtil
				.format("appid={0}&money={1}&channel={2}&roleid={3}&serverid={4}&username={5}&orderid={6}&datasignatrue={7}&mac={8}&expand={9}",
						69, 10000, 10000, "dev-test", 1,
						"test", 111111,"sign_test", "mac","expand");
		try {
			content = encodeForPayCenter(content);
		HttpUtil.doPost(
				"http://101.251.206.232:18088/pay_world/pay/Taiwan-Gamania/gp.jsp", content ).trim();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static String encodeForPayCenter(String sourceStr) {
		if (sourceStr == null || sourceStr.equals("")) {
			return null;
		}
		byte[] data = sourceStr.getBytes();
		byte[] encrypt = new byte[data.length];
		int index = 0;
		for (int i = data.length - 1; i >= 0; i--) {
			byte temp = data[i];
			encrypt[index] = (byte) (127 - temp);
			index++;
		}
		return new String(encrypt, 0, encrypt.length);
	}

}

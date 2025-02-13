/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.http.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * HTTP处理器抽象类，提供基础操作
 * 
 * @author sulingyun
 * 
 */
public abstract class AbsHandler implements HttpHandler {
	private static Logger log = LogManager.getLogger(AbsHandler.class);

	protected final String getRemoteIp(HttpExchange httpExchange) {
		return httpExchange.getRemoteAddress().getAddress().getHostAddress();
	}

	/**
	 * 解析HTTP请求中的键值信息
	 * 
	 * @return
	 * @throws IOException
	 */
	protected final Map<String, String> parseParamsMap(HttpExchange httpExchange) throws IOException {
		InputStream in = httpExchange.getRequestBody();
		String cl = httpExchange.getRequestHeaders().getFirst("Content-Length");
		int contentLength = TextUtil.isNullOrEmpty(cl) ? 0 : NumberUtil.parseInt(cl);

		String temp = null;
		String queryString = httpExchange.getRequestURI().getQuery();
		String postData = "";
		if (contentLength > 0) {
			byte[] buff = new byte[contentLength];
			in.read(buff);
			in.close();
			postData = URLDecoder.decode(new String(buff), "utf-8");
		}

		if (queryString != null) {
			if (postData.intern() == "") {
				postData = queryString;
			} else {
				postData = postData + "&" + queryString;
			}
		}

		Map<String, String> map = new HashMap<String, String>();
		StringTokenizer token = new StringTokenizer(postData, "&");
		while (token.hasMoreElements()) {
			String[] pair = ((String) token.nextElement()).split("=");
			if (pair.length == 2) {
				if (map.containsKey(pair[0])) {
					temp = map.get(pair[0]);
					map.put(pair[0], temp + "," + pair[1]);
				} else {
					map.put(pair[0], pair[1]);
				}
			} else if (pair.length == 1) {
				map.put(pair[0], "");
			}
		}

		return map;
	}

	protected final void sendResponse(HttpExchange httpExchange, String responseText) throws IOException {
		OutputStream out = null;
		try {
			httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, responseText.getBytes().length);// 设置响应头属性及响应信息的长度
			out = httpExchange.getResponseBody(); // 获得输出流
			out.write(responseText.getBytes());
			out.flush();
		} catch (Exception e) {
			log.error("Response error.", e);
		} finally {
			if (out != null) {
				out.close();
			}
			if (httpExchange != null) {
				httpExchange.close();
			}
		}
	}

	/**
	 * 获取MAP中的值，可以设置一个默认返回值
	 * 
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	protected final String getValue(Map<String, String> map, String key, String defaultValue) {
		if (map == null) {
			return defaultValue;
		}

		return map.containsKey(key) ? map.get(key) : defaultValue;
	}

	/**
	 * 根据一号通返回帐号生成X三国帐号，第三方渠道会采用类似".uc"方式，因此自有渠道帐号也会在后面加后缀后返回，方便统一处理
	 * 
	 * @param userNameFromYHT
	 *            一号通返回的帐号
	 * @return
	 */
	protected final String generateUserName(String userNameFromYHT) {//.mf modified to .wn
		return userNameFromYHT.indexOf(".") < 0 ? userNameFromYHT + ".wn" : userNameFromYHT;
	}

	@Override
	public final void handle(HttpExchange httpExchange) throws IOException {
		try {
			this.innerHandle(httpExchange);
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract void innerHandle(HttpExchange httpExchange) throws IOException;
}

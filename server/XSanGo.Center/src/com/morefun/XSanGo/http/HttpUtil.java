/**
 * 
 */
package com.morefun.XSanGo.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * HTTP协议操作工具类
 * 
 * @author sulingyun
 *
 */
public class HttpUtil {

	/**
	 * 执行一个HTTP POST请求，返回请求响应的内容
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param params
	 *            请求的查询参数,可以为null
	 * @return 返回请求响应的内容
	 * @throws IOException
	 */
	public static String doPost(String url, String body) throws IOException {
		StringBuffer stringBuffer = new StringBuffer();
		HttpEntity entity = null;
		BufferedReader in = null;
		HttpResponse response = null;
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpParams params = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 20000);
			HttpConnectionParams.setSoTimeout(params, 20000);
			HttpPost httppost = new HttpPost(url);
			httppost.setHeader("Content-Type", "text/html");
			// "application/x-www-form-urlencoded");

			httppost.setEntity(new ByteArrayEntity(body.getBytes("UTF-8")));
			response = httpclient.execute(httppost);
			entity = response.getEntity();
			in = new BufferedReader(new InputStreamReader(entity.getContent()));
			String ln;
			while ((ln = in.readLine()) != null) {
				stringBuffer.append(ln);
				stringBuffer.append("\r\n");
			}
			httpclient.getConnectionManager().shutdown();
		} catch (IOException e) {
			throw e;
		} finally {
			if (null != in) {
				try {
					in.close();
					in = null;
				} catch (IOException e3) {
					e3.printStackTrace();
				}
			}
		}
		return stringBuffer.toString();
	}

	/**
	 * 执行一个HTTP POST请求，返回请求响应的内容
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param params
	 *            请求的查询参数,可以为null
	 * @return 返回请求响应的内容
	 */
	public static String doPost(String url, byte[] body) {
		StringBuffer stringBuffer = new StringBuffer();
		HttpEntity entity = null;
		BufferedReader in = null;
		HttpResponse response = null;
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpParams params = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 20000);
			HttpConnectionParams.setSoTimeout(params, 20000);
			HttpPost httppost = new HttpPost(url);
			httppost.setHeader("Content-Type", "application/octet-stream");
			// "application/x-www-form-urlencoded");

			httppost.setEntity(new ByteArrayEntity(body));
			response = httpclient.execute(httppost);
			entity = response.getEntity();
			in = new BufferedReader(new InputStreamReader(entity.getContent()));
			String ln;
			while ((ln = in.readLine()) != null) {
				stringBuffer.append(ln);
				stringBuffer.append("\r\n");
			}
			httpclient.getConnectionManager().shutdown();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (IllegalStateException e2) {
			e2.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
					in = null;
				} catch (IOException e3) {
					e3.printStackTrace();
				}
			}
		}
		return stringBuffer.toString();
	}
	
	
//	private static String doGet(String address, int timeout) throws Exception {
//		StringBuffer readOneLineBuff = new StringBuffer();
//		String content = "";
//		HttpURLConnection conn = null;
//		BufferedReader reader = null;
//		try {
//			URL url = new URL(address);
//			conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestMethod("GET");
//			conn.setConnectTimeout(timeout);
//			conn.setReadTimeout(timeout);
//			reader = new BufferedReader(new InputStreamReader(
//					conn.getInputStream(), "utf-8"));
//			String line = "";
//			while ((line = reader.readLine()) != null) {
//				readOneLineBuff.append(line);
//			}
//			content = readOneLineBuff.toString();
//		} finally {
//			if (conn != null) {
//				try {
//					conn.disconnect();
//				} catch (Exception e) {
//					log.error("Disconnect yht connection error.", e);
//				}
//			}
//			if (reader != null) {
//				try {
//					reader.close();
//				} catch (Exception e) {
//					log.error("Close reader error.", e);
//				}
//			}
//		}
//
//		return content;
//	}
}

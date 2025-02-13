/**
 * 
 */
package com.morefun.XSanGo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

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
	 */
	public static ResponseContent doPost(String url, String body) {
		ResponseContent responseResult = null;
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
			StringBuffer stringBuffer = new StringBuffer();
			String ln;
			while ((ln = in.readLine()) != null) {
				stringBuffer.append(ln);
				stringBuffer.append("\r\n");
			}
			responseResult = new ResponseContent(response.getStatusLine().getStatusCode(), stringBuffer.toString());
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
		return responseResult;
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
	public static ResponseContent doPost(String url, byte[] body) {
		ResponseContent responseResult = null;
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
			StringBuffer stringBuffer = new StringBuffer();
			String ln;
			while ((ln = in.readLine()) != null) {
				stringBuffer.append(ln);
				stringBuffer.append("\r\n");
			}
			responseResult = new ResponseContent(response.getStatusLine().getStatusCode(), stringBuffer.toString());
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
		return responseResult;
	}

	public static class ResponseContent {
		private int statusCode;
		private String content;

		public ResponseContent(int statusCode, String content) {
			this.statusCode = statusCode;
			this.content = content;
		}

		public int getStatusCode() {
			return this.statusCode;
		}

		public String getContent() {
			return this.content;
		}
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		OutputStream out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(10000);
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = conn.getOutputStream();
			// 发送请求参数
			// out.print(param.getBytes("UTF-8"));
			out.write(param.getBytes("UTF-8"));
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			LogManager.error(e);
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
}

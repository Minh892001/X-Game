/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.springframework.security.core.codec.Base64;

import com.google.gson.Gson;

/**
 * 字符串文本处理类
 * 
 * @author BruceSu
 * 
 */
public class TextUtil {
	public static final Gson GSON = new Gson();
	private static final String DEFAULT_COLOR = "#000000";

	/**
	 * 格式化字符串
	 * 
	 * @param msg
	 * @param paramer
	 * @return
	 */
	public static String format(String msg, Object... paramers) {
		Object[] strParamers = new Object[paramers.length];
		for (int i = 0; i < paramers.length; i++) {
			Object paramer = paramers[i];
			strParamers[i] = paramer.toString();
		}

		return MessageFormat.format(msg, strParamers);
	}

	/**
	 * 检测制定字符串是否为空
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isNullOrEmpty(String text) {
		return text == null || text.isEmpty() || text.equals("");
	}

	/**
	 * 连接字符串
	 * 
	 * @param list
	 * @param symbol
	 */
	public static String join(Iterable<String> list, String symbol) {
		StringBuilder builder = new StringBuilder();
		int i = 0;
		for (String string : list) {
			if (i > 0) {
				builder.append(symbol);
			}

			builder.append(string);
			i++;
		}

		return builder.toString();
	}

	/**
	 * 过滤空白字符
	 * 
	 * @param str
	 * @return
	 */
	public static String filterBlank(String str) {
		String regEx = "\t|\r|\n";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("");
	}

	/**
	 * gzip压缩
	 * 
	 * @param primStr
	 * @return
	 * @throws IOException
	 */
	public static String gzip(String primStr) throws IOException {
		if (primStr == null || primStr.length() == 0) {
			return primStr;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(primStr.getBytes());
		gzip.close();

		return new String(Base64.encode(out.toByteArray()));
	}

	/**
	 * gzip解压
	 * 
	 * @param compressedStr
	 * @return
	 * @throws IOException
	 */
	public static String ungzip(String compressedStr) throws IOException {
		if (compressedStr == null) {
			return null;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] compressed = Base64.decode(compressedStr.getBytes());
		ByteArrayInputStream in = new ByteArrayInputStream(compressed);
		GZIPInputStream ginzip = new GZIPInputStream(in);

		byte[] buffer = new byte[1024];
		int offset = -1;
		while ((offset = ginzip.read(buffer)) != -1) {
			out.write(buffer, 0, offset);
		}
		String decompressed = out.toString();
		ginzip.close();
		in.close();
		out.close();

		return decompressed;
	}

	public static byte[] objectToBytes(Object obj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(baos);
		out.writeObject(obj);
		out.close();

		return baos.toByteArray();
	}

	public static Object bytesToObject(byte[] bytes) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream in = new ObjectInputStream(bais);
		Object obj = in.readObject();
		in.close();

		return obj;
	}
}

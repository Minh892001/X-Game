/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * 二进制辅助类
 * 
 * @author BruceSu
 * 
 */
public class BinaryUtil {
	public static void writeString(DataOutputStream os, String content)
			throws IOException {
		byte[] bytes = content.getBytes("UTF-8");
		os.writeShort(bytes.length);
		os.write(bytes);
	}

	/**
	 * 读取字符串
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String readString(DataInputStream in) throws IOException {
		short length = in.readShort();
		byte[] bytes = new byte[length];
		in.read(bytes);
		return new String(bytes, "UTF-8");
	}

	public static void main(String[] args) {
		try {
			ByteArrayOutputStream buff = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(buff);
			writeString(dos, "test-temp");
			dos.writeInt(13593);

			DataInputStream in = new DataInputStream(new ByteArrayInputStream(
					buff.toByteArray()));
			System.out.println(readString(in));
			System.out.println(in.readInt());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static Date readDate(DataInputStream in) throws IOException {
		return new Date(in.readLong());
	}

	/**
	 * @param out
	 * @param date
	 * @throws IOException
	 */
	public static void writeDate(DataOutputStream out, Date date)
			throws IOException {
		out.writeLong(date == null ? 0 : date.getTime());
	}

	/**
	 * @param out
	 * @param values
	 * @throws IOException
	 */
	public static void writeInts(DataOutputStream out, int[] values)
			throws IOException {
		for (int i : values) {
			out.writeInt(i);
		}
	}

	/**
	 * @param out
	 * @param ls
	 * @throws IOException
	 */
	public static void writeLongs(DataOutputStream out, long[] values)
			throws IOException {
		for (long i : values) {
			out.writeLong(i);
		}
	}

	/**
	 * @param out
	 * @param values
	 * @throws IOException
	 */
	public static void writeShorts(DataOutputStream out, short[] values)
			throws IOException {
		for (short i : values) {
			out.writeShort(i);
		}
	}
}

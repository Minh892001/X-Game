/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: CodeUtil
 * 功能描述：
 * 文件名：CodeUtil.java
 **************************************************
 */
package com.morefun.XSanGo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 各种随机码生成
 * 
 * @author zhangwei02.zhang
 * @since 2015年10月29日
 * @version 1.0
 */
public class CodeUtil {

	public static final String CHARACTER_COMBINE_NO_CONFUSE = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";

	public static final String CHARACTER_COMBINE = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static final String CHARACTER_DIGITAL = "1234567890";

	public static final String CHARACTER_ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * 随机码生成器
	 * 
	 * @param length 码长度
	 * @param validChar 允许出现的字符集和
	 * @param number 生成的数量
	 * @return
	 */
	public static List<String> generateRandomCode(int length, String validChar, int number) {
		List<String> list = new ArrayList<String>();
		if (length <= 0 || number <= 0) {
			return list;
		}
		String tmp = null;
		while (true) {
			tmp = generateRandomStr(length, validChar);
			if (!list.contains(tmp)) {
				list.add(tmp);
			}
			if (list.size() == number) {
				break;
			}
		}
		return list;
	}

	/**
	 * 
	 * 生成随机字符串
	 * 
	 * @param length
	 * @param limit
	 * @return
	 */
	private static String generateRandomStr(int length, String validChar) {
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append(validChar.charAt(random.nextInt(validChar.length())));
		}
		return sb.toString();
	}
}

/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author BruceSu
 * 
 */
public class NumberUtil {
	
	private static Random random = new Random();

	/**
	 * 获得0-1之间的float随机值
	 * 
	 * @return
	 */
	public static float randomFloat() {
		return random.nextFloat();
	}

	/**
	 * 获取随机数
	 * 
	 * @param max
	 * @return
	 */
	public static int random(int max) {
		return random.nextInt(max);
	}

	/**
	 * 获取值在min和max之间的随机值，不包括max
	 * 
	 * @param min int
	 * @param max int
	 * @return int
	 */
	public static int random(int min, int max) {
		if (min == max) {
			return min;
		} else if (max > min) {
			return min + random.nextInt(max - min);
		}
		throw new IllegalArgumentException("min(" + min + ") is larger than max(" + max + ")");
	}

	/**
	 * 获取值在min和max之间的随机值，包括max
	 * 
	 * @param min int
	 * @param max int
	 * @return int
	 */
	public static int randomContain(int min, int max) {
		return random(min, max + 1);
	}

	// public static void main(String[] args) {
	// for (int i = 0; i < 100; i++) {
	// int min = i;
	// int max = i * (1 + new Random().nextInt(10));
	// int r = getRandomInt(min, max);
	// System.out.println(r + "\t" + Boolean.valueOf((min <= r && r <= max)));
	// }
	// System.out.println(getRandomInt(1, 1));
	// }

	/**
	 * 是否在指定区间内，包括下限但不包括上限
	 * 
	 * @param value
	 * @param lower
	 * @param upper
	 * @return
	 */
	public static boolean isBetweenIncludeLowerNoUpper(int value, int lower, int upper) {
		return value >= lower && value < upper;
	}

	/**
	 * 指定成功率进行一次随机，结果表示是否随机成功
	 * 
	 * @param rate
	 * @param total
	 * @return
	 */
	public static boolean isHit(int rate, int total) {
		if (rate < 0 || total < 0) {
			throw new IllegalArgumentException();
		}

		return random(total) < rate;
	}

	/**
	 * 将字符串转换为整数<br>
	 * 不能转换的字符串转换为0,包括溢出的值
	 */
	public static int parseInt(String s) {
		return parseInt(s, 10);
	}

	public static int parseInt(String s, int i) {
		if (s == null)
			return 0;
		if (i < 2)
			return 0;
		if (i > 36)
			return 0;
		int j = 0;
		boolean flag = false;
		int k = 0;
		int l = s.length();
		if (l > 0) {
			int i1;
			if (s.charAt(0) == '-') {
				flag = true;
				i1 = -2147483648;
				k++;
			} else {
				i1 = -2147483647;
			}
			int j1 = i1 / i;
			if (k < l) {
				int k1 = Character.digit(s.charAt(k++), i);
				if (k1 < 0)
					return 0;
				j = -k1;
			}
			while (k < l) {
				int l1 = Character.digit(s.charAt(k++), i);
				if (l1 < 0)
					return 0;
				if (j < j1)
					return 0;
				j *= i;
				if (j < i1 + l1)
					return 0;
				j -= l1;
			}
		} else {
			return 0;
		}
		if (flag) {
			if (k > 1)
				return j;
			else
				return 0;
		} else {
			return -j;
		}
	}

	/**
	 * 取得不重复的随机的数
	 * 
	 * @param num 取得随机数的数量
	 * @param min 取得随机数的最小范围
	 * @param max 取得随机数的最大范围
	 * @return
	 */
	public static int[] randomArry(int num, int min, int max) {
		// 初始化返回数组，变成-1
		int[] resRand = new int[num];
		for (int i = 0; i < num; i++) {
			resRand[i] = -1;
		}

		// 取得随机数的数量 必须小于 随机数的最大范围
		if (num < max) {
			int count = 0;
			while (count < num) {
				int randNum = NumberUtil.random(min, max);
				boolean flag = true;

				for (int i = 0; i < num; i++) {
					if (randNum == resRand[i]) {
						flag = false;
						break;
					}
				}

				if (flag) {
					resRand[count] = randNum;
					count++;
				}
			}
		} else {
			resRand = new int[max];
			for (int i = 0; i < max; i++) {
				resRand[i] = i;
			}
		}

		return resRand;
	}

	/**
	 * 在多个概率中，取得其中的N个概率
	 * 
	 * @param roundList
	 * @return
	 */
	public static int randRound(List<Integer> roundList) {
		int resInt = 0;

		// 判断List的数量是否大于1
		if (roundList.size() > 1) {

			// 计算概率的基数
			int baseRound = 0;
			for (int round : roundList) {
				baseRound += round;
			}

			// 叠加
			List<Integer> totalList = new ArrayList<Integer>();
			totalList.add(roundList.get(0));
			for (int i = 1; i < roundList.size(); i++) {
				totalList.add(roundList.get(i));
				int temp = totalList.get(i) + totalList.get(i - 1);
				totalList.set(i, temp);
			}

			// 取得概率
			int randNum = NumberUtil.random(baseRound);
			for (int i = 0; i < totalList.size(); i++) {
				if (randNum > totalList.get(i)) {
					resInt = i + 1;
				} else {
					break;
				}
			}
		}

		if (resInt == 0) {
			resInt = 1;
		}

		return resInt;
	}

	public static float parseFloat(String input) {
		if (TextUtil.isBlank(input)) {
			return 0;
		}
		return Float.parseFloat(input);
	}
	
	public static double parseDouble(String input) {
		if (TextUtil.isBlank(input)) {
			return 0;
		}
		return Double.parseDouble(input);
	}

	/**
	 * 四舍五入，保留小数
	 * 
	 * @param randNum
	 * @param rand 保留小数 数量
	 * @return
	 */
	public static double randUp(double randNum, int rand) {
		BigDecimal bd = new BigDecimal(randNum);
		bd = bd.setScale(rand, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}

	public static float randUp(float randNum, int rand) {
		BigDecimal bd = new BigDecimal(randNum);
		bd = bd.setScale(rand, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}

	/**
	 * 十六进制转十进制颜色使用
	 * 
	 * @param htmlColor
	 * @return
	 */
	public static long parseColorRGBAFromHtml(String htmlColor) {
		int startIndex = htmlColor.startsWith("#") ? 1 : 0;
		long r = Long.valueOf(htmlColor.substring(startIndex, startIndex + 2), 16);
		long g = Long.valueOf(htmlColor.substring(startIndex + 2, startIndex + 4), 16);
		long b = Long.valueOf(htmlColor.substring(startIndex + 4, startIndex + 6), 16);
		long a = 255;
		long rgba = (r << 24) | (g << 16) | (b << 8) | a;
		return rgba;
	}

	/**
	 * 检测参数是否在指定范围内，检查不通过将抛出非法参数异常
	 * 
	 * @param param 待检测参数
	 * @param min 最小值
	 * @param max 最大值
	 */
	public static void checkRange(int param, int min, int max) {
		if (param < min || param > max) {
			throw new IllegalArgumentException("" + param);
		}
	}

	/**
	 * 获取随机 boolean
	 * 
	 * @return
	 */
	public static boolean randomBoolean() {
		return random.nextBoolean();
	}
}

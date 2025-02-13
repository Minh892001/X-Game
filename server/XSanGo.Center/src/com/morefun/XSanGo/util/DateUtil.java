/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author BruceSu
 * 
 */
public class DateUtil {
	public final static String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 返回当天的第一秒的Date对象
	 * 
	 * @return
	 */
	public static Calendar getFirstSecondOfToday() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}

	/**
	 * 给指定日期增加days天，并返回被更改的日期对象
	 * 
	 * @param basic
	 * @param days
	 * @return
	 */
	public static Calendar addDays(Calendar basic, int days) {
		basic.add(Calendar.DAY_OF_MONTH, days);
		return basic;
	}

	/**
	 * 给指定日期增加hours小时，并返回被更改的日期对象
	 * 
	 * @param basic
	 * @param hours
	 * @return
	 */
	public static Calendar addHours(Calendar basic, int hours) {
		basic.add(Calendar.HOUR_OF_DAY, hours);
		return basic;
	}

	/**
	 * 判断两个日期是否同一天
	 * 
	 * @param lastUpdate
	 * @param currentTimeMillis
	 * @return
	 */
	public static boolean isSameDay(long one, long two) {
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(one);
		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(two);

		return isSameDay(c1, c2);
	}

	/**
	 * 判断两个日期是否同一天
	 * 
	 * @param one
	 * @param two
	 * @return
	 */
	public static boolean isSameDay(Calendar one, Calendar two) {
		return ((one.get(Calendar.YEAR) == two.get(Calendar.YEAR)) && (one
				.get(Calendar.DAY_OF_YEAR) == two.get(Calendar.DAY_OF_YEAR)));
	}

	public static String toString(long time) {
		Date d = new Date();
		d.setTime(time);
		SimpleDateFormat nowDate = new SimpleDateFormat(DEFAULT_PATTERN);
		return nowDate.format(d);
	}

	public static String toString(long time, String patten) {
		Date d = new Date();
		d.setTime(time);
		SimpleDateFormat nowDate = new SimpleDateFormat(patten);
		return nowDate.format(d);
	}

	public static Date parseDate(String time) {
		SimpleDateFormat nowDate = new SimpleDateFormat(DEFAULT_PATTERN);
		try {
			return nowDate.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date parseDate(String patten, String time) {
		SimpleDateFormat nowDate = new SimpleDateFormat(patten);
		try {
			return nowDate.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取当前时间至指定时的点间隔毫秒数 如果当前钟点大于指定钟点数，则结果是当前时间至第二天指定时的间隔毫秒数
	 * 
	 * @param taskHour
	 * @param taskMiniute
	 * @return
	 */
	public static long betweenTaskHourMillis(int taskHour, int taskMiniute) {
		if (taskHour < 0) {
			taskHour = 0;
		}
		if (taskHour > 23) {
			taskHour = 23;
		}
		if (taskMiniute < 0) {
			taskMiniute = 0;
		}
		if (taskMiniute > 59) {
			taskMiniute = 59;
		}

		Calendar c = Calendar.getInstance();
		int nowHour = c.get(Calendar.HOUR_OF_DAY);
		if (nowHour > taskHour
				|| (nowHour == taskHour && c.get(Calendar.MINUTE) >= taskMiniute)) {
			c.add(Calendar.DAY_OF_MONTH, 1);
		}
		c.set(Calendar.HOUR_OF_DAY, taskHour);
		c.set(Calendar.MINUTE, taskMiniute);
		c.set(Calendar.SECOND, 0);
		return c.getTimeInMillis() - System.currentTimeMillis();
	}

	/**
	 * 获取当前时间至指定钟点数0分0秒时的间隔毫秒数 如果当前钟点大于指定钟点数，则结果是当前时间至第二天指定钟点数0分0秒时的间隔毫秒数
	 * 
	 * @param taskHour
	 *            任务钟点数（5点etc.）
	 * @return
	 */
	public static long betweenTaskHourMillis(int taskHour) {
		return betweenTaskHourMillis(taskHour, 0);
	}

	public static boolean isSameMonth(Calendar one, Calendar two) {
		return ((one.get(Calendar.YEAR) == two.get(Calendar.YEAR)) && (one
				.get(Calendar.MONTH) == two.get(Calendar.MONTH)));
	}

	/**
	 * 指定时间是否在两个时间点之间
	 * 
	 * @param checkPoint
	 * @param begin
	 * @param end
	 * @return
	 */
	public static boolean isBetween(Date checkPoint, Date begin, Date end) {
		return checkPoint.getTime() > begin.getTime()
				&& checkPoint.getTime() < end.getTime();
	}

}

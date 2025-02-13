/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author BruceSu
 * 
 */
public class DateUtil {
	public final static String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public final static String HHMM_PATTERN = "HH:mm";

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
	 * 增加月份
	 * 
	 * @param basic
	 * @param num
	 * @return
	 */
	public static Calendar addMonth(Calendar basic, int num) {
		basic.add(Calendar.MONTH, num);
		return basic;
	}

	/**
	 * 两日前相差的天数 忽略时间
	 * 
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static int diffDate(Calendar c1, Calendar c2) {
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);
		
		c2.set(Calendar.HOUR_OF_DAY, 0);
		c2.set(Calendar.MINUTE, 0);
		c2.set(Calendar.SECOND, 0);
		c2.set(Calendar.MILLISECOND, 0);
		
		int days = 0;
		while (c2.before(c1)) {
			days++;
			c2.add(Calendar.DAY_OF_YEAR, 1);
		}
		return days;
	}

	/**
	 * 两日前相差的天数  忽略时间
	 * 
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static int diffDate(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);
		return diffDate(c1, c2);
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
	 * @param oneDate
	 * @param twoDate
	 * @return
	 */
	public static boolean isSameDay(Date oneDate, Date twoDate) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(oneDate);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(twoDate);

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

	/*
	 * 判断 日期是否和指定天数相同
	 */
	public static boolean isSameDay(Calendar one, int date) {
		return one.get(Calendar.DATE) == date;
	}

	/**
	 * 返回当月的天数
	 * 
	 * @param one
	 * @return
	 */
	public static int getMonyhDay(Calendar one) {
		return one.get(Calendar.DATE);
	}

	/**
	 * 设置当前月份的天数
	 * 
	 * @param date
	 * @return
	 */
	public static Date setMonyhDay(int date) {
		return DateUtil.setMonyhDay(date, 0);
	}

	/**
	 * 设置指定月份的天数
	 * 
	 * @param date
	 *            设置日期
	 * @param addMmonth
	 *            增加和减少的月份
	 * @return
	 */
	public static Date setMonyhDay(int date, int addMmonth) {
		Calendar cal = DateUtil.getFirstSecondOfToday();
		cal.set(Calendar.DATE, date);
		cal.add(Calendar.MONTH, addMmonth);
		return cal.getTime();
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
			LogManager.error(e);
		}
		return null;
	}

	public static Date parseDate(String patten, String time) {
		SimpleDateFormat nowDate = new SimpleDateFormat(patten);
		try {
			return nowDate.parse(time);
		} catch (ParseException e) {
			LogManager.error(e);
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

	public static boolean isSameMonth(Date date1, Date date2) {
		Calendar one = Calendar.getInstance();
		one.setTime(date1);

		Calendar two = Calendar.getInstance();
		two.setTime(date2);
		return ((one.get(Calendar.YEAR) == two.get(Calendar.YEAR)) && (one
				.get(Calendar.MONTH) == two.get(Calendar.MONTH)));
	}

	/**
	 * 当前是否在指定时间范围内
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	public static boolean isBetween(Date begin, Date end) {
		return isBetween(Calendar.getInstance().getTime(), begin, end);
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

	/**
	 * 比较两个日期相差的毫秒数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long compareTime(Date date1, Date date2) {
		return date1.getTime() - date2.getTime();
	}

	/**
	 * 比较两个日期相差的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareDate(Date date1, Date date2) {
		return (int) (compareTime(date1, date2) / (60 * 60 * 24 * 1000));
	}

	/**
	 * 日期和时间拼接
	 * 
	 * @param date
	 * @param timeStr
	 * @return
	 */
	public static Date joinTime(Date date, String timeStr) {
		String dateStr = DateUtil.toString(date.getTime(), "yyyy-MM-dd");
		return DateUtil.parseDate(dateStr + " " + timeStr);
	}

	/**
	 * 日期和时间拼接,默认当天日期
	 * 
	 * @param timeStr
	 * @return
	 */
	public static Date joinTime(String timeStr) {
		return DateUtil.joinTime(new Date(), timeStr);
	}

	/**
	 * Date 转 Calendar
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar convertCal(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		return c;
	}

	/**
	 * 改变当前日期的秒数
	 * 
	 * @param modValue
	 *            正负代表增减
	 * @return
	 */
	public static Date addSecond(int modValue) {
		return DateUtil.addSecond(new Date(), modValue);
	}

	/**
	 * 改变 指定日期的秒数
	 * 
	 * @param date
	 * @param modValue
	 *            正负代表增减
	 * @return
	 */
	public static Date addSecond(Date date, int modValue) {
		Calendar c = DateUtil.convertCal(date);
		c.add(Calendar.SECOND, modValue);

		return c.getTime();
	}
	
	/**
	 * 改变指定日期的分钟数
	 * @param date
	 * @param modValue
	 * 			正负代表增减
	 * @return
	 */
	public static Date addMinutes(Date date, int modValue){
		Calendar c = DateUtil.convertCal(date);
		c.add(Calendar.MINUTE, modValue);
		return c.getTime();
	}
	
	/**
	 * 给指定日期增加days天，并返回被更改的日期对象
	 * 
	 * @param basic
	 * @param days
	 * @return
	 */
	public static Date addDays(Date date, int days){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, days);
		return c.getTime();
	}

	/**
	 * 从from开始到现在，是否会经过检查点时间
	 * 
	 * @param checkPoint
	 *            检查时间点，该对象的日期部分将被忽略
	 * @param patten
	 *            时间格式
	 * @param from
	 *            开始时间点
	 * @return
	 */
	public static boolean isPass(String checkPoint, String patten, Date from) {
		Date time = parseDate(patten, checkPoint);
		return isPass(time, from);
	}

	public static void main(String[] args) {
		System.out.println(diffDate(new Date(), parseDate("2015-08-18 00:00:00")));
	}

	/**
	 * 从from开始到现在，是否会经过检查点时间
	 * 
	 * @param checkPoint
	 *            检查时间点，该对象的日期部分将被忽略
	 * @param from
	 * @return
	 */
	public static boolean isPass(Date checkTime, Date from) {
		Calendar now = Calendar.getInstance();
		long totalInterval = now.getTimeInMillis() - from.getTime();
		if (totalInterval < 0) {
			return false;
		}

		// 超过一天了必然什么点都会过一遍
		if (totalInterval >= TimeUnit.DAYS.toMillis(1)) {
			return true;
		}

		// 间隔时间不超过一天时候，需要检查两次，分别为同天或者隔天
		Calendar begin = Calendar.getInstance();
		begin.setTime(from);
		Calendar check = (Calendar) begin.clone();
		check.set(Calendar.HOUR_OF_DAY, checkTime.getHours());
		check.set(Calendar.MINUTE, checkTime.getMinutes());
		check.set(Calendar.SECOND, checkTime.getSeconds());

		if (isBetween(check.getTime(), begin.getTime(), now.getTime())) {
			return true;
		}
		addDays(check, 1);
		if (isBetween(check.getTime(), begin.getTime(), now.getTime())) {
			return true;
		}

		return false;
	}

	/**
	 * 对比 时间点，是否过期
	 * 
	 * @param currDate
	 *            上一次发生的时间
	 * @param standardDate
	 *            当天检查时间
	 * @return
	 */
	public static boolean checkTime(Date currDate, Date standardDate) {
		if (DateUtil.compareDate(new Date(), currDate) == 0) {
			if (System.currentTimeMillis() > standardDate.getTime()
					&& currDate.getTime() < standardDate.getTime()) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	/**
	 * 检查日期的时分秒是否在指定时分秒范围内
	 * 
	 * @param checkTime
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static boolean checkTimeRange(Date checkTime, Date beginTime,
			Date endTime) {
		String patten = "HH:mm:ss";
		checkTime = parseDate(patten, toString(checkTime.getTime(), patten));
		long checkMillis = checkTime.getTime();
		long beginMillis = beginTime.getTime();
		long endMillis = endTime.getTime();
		if (beginMillis <= endMillis) {
			if (checkMillis >= beginMillis && checkMillis < endMillis) {
				return true;
			} else {
				return false;
			}
		} else {
			// 结束时间小于开始时间需要特殊处理
			if (checkMillis >= endMillis && checkMillis < beginMillis) {
				return false;
			} else {
				return true;
			}
		}
	}
	
	/**
	 * 计算big-small的天数
	 * @param big
	 * @param small
	 * @return
	 */
	public static int diffDay(Date big,Date small){
		Calendar bigCal = Calendar.getInstance();
		bigCal.setTime(big);
		
		Calendar smallCal = Calendar.getInstance();
		bigCal.setTime(small);
		
		return bigCal.get(Calendar.DATE) - smallCal.get(Calendar.DATE);
	}
	
	/**
	 * 日期格式化成String
	 * @param datetime
	 * @param patten
	 * @return
	 */
	public static String format(Date datetime, String patten){
		SimpleDateFormat format = new SimpleDateFormat(patten);
		return format.format(datetime);
	}
	
	public static String format(Date datetime){
		SimpleDateFormat format = new SimpleDateFormat(DEFAULT_PATTERN);
		return format.format(datetime);
	}
}

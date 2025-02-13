/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.util;

/**
 * 每日定时task
 * 
 * @author BruceSu
 * 
 */
public abstract class DailyTask extends DelayedTask {
	/**
	 * 根据每天执行任务的时间点创建任务
	 * 
	 * @param hour
	 * @param miniute
	 */
	public DailyTask(int hour, int miniute) {
		super(DateUtil.betweenTaskHourMillis(hour, miniute),
				24 * 60 * 60 * 1000);// 创建间隔时间为1天，延迟等待时间为到指定时间点的task
	}

}

/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.util;
/**
 * 
 */


import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延时任务，时间统一以毫秒为单位
 * 
 * @author BruceSu
 * 
 */
public abstract class DelayedTask implements Runnable, Delayed {

	/** 任务执行的时间 */
	protected long trigger;

	/** 重复执行时间间隔（毫秒） */
	protected long interval;

	/**
	 * 创建可重复使用的任务
	 * 
	 * @param delayed
	 * @param interval
	 */
	public DelayedTask(long delayed, long interval) {
		trigger = System.currentTimeMillis() + delayed;
		this.interval = interval;
	}

	/**
	 * 创建一次性使用的任务，不可重复
	 * 
	 * @param delayed
	 */
	public DelayedTask(long delayed) {
		this(delayed, 0);
	}

	/**
	 * 对象重用，根据自身设置生成一个新的执行时间
	 * 
	 * @throws TaskUnreusableException
	 *             该对象创建时被设置为不可重复执行时抛出
	 */
	public void reuse() throws TaskUnreusableException {
		if (!this.isRepeat()) {
			throw new TaskUnreusableException();
		}
		trigger += this.interval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Delayed o) {
		long comp = o.getDelay(TimeUnit.MILLISECONDS);
		long me = this.getDelay(TimeUnit.MILLISECONDS);
		if (me < comp)
			return -1;
		if (me > comp)
			return 1;
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.Delayed#getDelay(java.util.concurrent.TimeUnit)
	 */
	@Override
	public long getDelay(TimeUnit unit) {
		return trigger - System.currentTimeMillis();
	}

	public boolean isRepeat() {
		return this.interval > 0;
	}
}

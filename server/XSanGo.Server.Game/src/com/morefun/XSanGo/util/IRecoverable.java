/**
 * 
 */
package com.morefun.XSanGo.util;

import java.util.Date;

/**
 * 时间恢复接口
 * 
 * @author sulingyun
 *
 */
public interface IRecoverable {
	/**
	 * 获取下次恢复时间点
	 * 
	 * @return
	 */
	Date getNextRecTime();

	/**
	 * 设置下次恢复时间点
	 * 
	 * @param time
	 */
	void setTime(Date time);

	/**
	 * 获取当前值
	 * 
	 * @return
	 */
	int getValue();

	/**
	 * 获取最大值
	 * 
	 * @return
	 */
	int getLimit();

	/**
	 * 改变当前值
	 * 
	 * @param change
	 */
	void changeValue(int change);

	/**
	 * 获取恢复时间间隔，单位：毫秒
	 * 
	 * @return
	 */
	long getInterval();
}

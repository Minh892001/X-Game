/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.monitor;

import com.morefun.XSanGo.util.TextUtil;

/**
 * @author BruceSu
 * 
 */
public class ProcessStat {
	private String name;
	/** 执行次数 */
	private int processTime;
	/** 总执行时间 */
	private long totalCostTime;
	private int maxCostTime;

	public ProcessStat(String name) {
		this.name = name;
	}

	public void increase(int time) {
		this.processTime++;
		this.totalCostTime += time;

		if (this.maxCostTime < time) {
			this.maxCostTime = time;
		}
	}

	public String getName() {
		return name;
	}

	public int getProcessTime() {
		return processTime;
	}

	public long getTotalCostTime() {
		return totalCostTime;
	}

	public int getMaxCostTime() {
		return maxCostTime;
	}

	public int getAvgCostTime() {
		if (this.processTime == 0) {
			return 0;
		}
		return (int) (this.totalCostTime / this.processTime);
	}

	@Override
	public String toString() {
		return TextUtil.format(
				"[name={0},processTime={1},avgTime={2},maxTime={3}]",
				this.getName(), this.getProcessTime(), this.getAvgCostTime(),
				this.getMaxCostTime());
	}
}

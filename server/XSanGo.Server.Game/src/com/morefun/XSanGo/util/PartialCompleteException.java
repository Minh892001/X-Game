/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.util;

/**
 * 只完成指定任务的一部分时抛出的异常
 * 
 * @author BruceSu
 * 
 */
public class PartialCompleteException extends Exception {
	private static final long serialVersionUID = -1668473927803043479L;
	
	private int total;
	private int completed;

	public PartialCompleteException(int total, int completed) {
		this.total = total;
		this.completed = completed;
	}

	public int getTotal() {
		return total;
	}

	public int getCompleted() {
		return completed;
	}

}

/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.util;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executor;

/**
 * 延时任务执行线程
 * 
 * @author BruceSu
 * 
 */
public class DelayedTaskThread extends Thread {
	private boolean work = true;
	/** 任务队列 */
	private DelayQueue<DelayedTask> taskQueue = new DelayQueue<DelayedTask>();
	private Executor executor;

	public DelayedTaskThread() {

	}

	public DelayedTaskThread(Executor executor) {
		this.executor = executor;
	}

	/**
	 * 添加延迟任务接口
	 * 
	 * @param message
	 */
	public void putTask(DelayedTask rt) {
		this.taskQueue.put(rt);
	}

	public void removeTask(DelayedTask dt) {
		this.taskQueue.remove(dt);
	}

	/**
	 * 从任务队列里取出最近的任务并执行
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		while (this.work) {
			DelayedTask task;
			try {
				task = taskQueue.take();
				if (!this.work) {
					break;
				}
				
				if (task.isCanceled()) {
					continue;
				}

				if (this.executor == null) {
					task.run();
				} else {
					this.executor.execute(task);
				}
				if (task.isRepeat()) {
					task.reuse();
					this.putTask(task);
				}
			} catch (InterruptedException e) {
				LogManager.error(e);
			} catch (TaskUnreusableException e) {
				LogManager.error(e);
			} catch (Exception e) {
				LogManager.error(e);
			}
		}
	}

	public static void main(String[] args) {
		new DelayedTaskThread().start();
	}

	public boolean isWork() {
		return work;
	}

	public void setWork(boolean work) {
		this.work = work;
	}
}

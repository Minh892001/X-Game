/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LoginDatabase {

	private static LoginDatabase instance = null;

	protected final static Log logger = LogFactory.getLog(LoginDatabase.class);
	private static AtomicLong queueSize = new AtomicLong();

	/**
	 * @return
	 */
	public synchronized static LoginDatabase instance() {
		if (instance == null) {
			instance = new LoginDatabase();
		}
		return instance;
	}

	private ApplicationContext ac;

	public static final int DB_THREAD_NUM = 10;

	protected static ExecutorService executor = Executors
			.newFixedThreadPool(DB_THREAD_NUM);

	public static void setExecutor(ExecutorService executor) {
		LoginDatabase.executor = executor;
	}

	public static void execute(final Runnable runable) {
		queueSize.incrementAndGet();
		final Throwable trace = new Throwable();
		executor.execute(new Runnable() {

			@Override
			public void run() {
				try {
					if (runable != null) {
						runable.run();
					}
				} catch (Exception e) {
					logger.error(e);
					logger.error(trace);
				} finally {
					queueSize.decrementAndGet();
					// if (logicCallback != null) {
					// LogicThread.execute(logicCallback);
					// }
				}
			}
		});
	}

	public ApplicationContext getAc() {
		return ac;
	}

	/**
	 * 是否所有数据库线程都执行完毕，只有在shutdown方法调用后才有可能返回true
	 * 
	 * @return
	 */
	public static boolean isTerminated() {
		return executor.isTerminated();
	}

	/**
	 * 
	 */
	public void init() {
		// 初始化数据库
		try {
			this.ac = new ClassPathXmlApplicationContext(
					new String[] { "/applicationContext.xml" });
		} catch (Exception e) {
			logger.error(e);
		}

	}

	/**
	 * 停止所有数据库线程，在此之前会执行完所有待执行任务但不再接受新的任务
	 * 
	 */
	public static void shutdown() {
		executor.shutdown();
	}

	/**
	 * 队列长度
	 * 
	 * @return
	 */
	public static long getQueueSize() {
		return queueSize.get();
	}

}

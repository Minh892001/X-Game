/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.morefun.XSanGo.monitor.XsgMonitorManager;
import com.morefun.XSanGo.net.IceEntry;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.DelayedTaskThread;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

public class LogicThread {

	private final static Log logger = LogFactory.getLog(LogicThread.class);

	/** 主逻辑线程 */
	private static ThreadPoolExecutor logicExecutor = new ThreadPoolExecutor(30, 30, 60, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());
	/** 执行延迟任务的辅助线程 */
	private static DelayedTaskThread timer = new DelayedTaskThread();

	private static boolean dynamicOverload;
	private static boolean staticOverload;

	/** 主逻辑线程编号 */
	protected static long logicThreadId;

	/**
	 * 逻辑初始化
	 */
	public static void init() {
		// 初始化主线程标记
		logicExecutor.execute(new Runnable() {
			@Override
			public void run() {
				logicThreadId = Thread.currentThread().getId();
				Thread.currentThread().setName("LogicThread");
				logger.info("Main Thread ID: " + logicThreadId);
			}
		});

		// 初始化辅助线程
		timer.start();

		// 初始化服务器列表
		loadGameServers();

		// 启动检测服务器状态线程
		monitorServerStatus();

		// 启动性能检测
		startMonitor();
	}

	private static void startMonitor() {
		logger.warn("start monitor...");
		final int interval = 2 * 60 * 1000;
		scheduleTask(new DelayedTask(interval, interval) {
			@Override
			public void run() {
				XsgMonitorManager.getInstance().showStatInfo();
			}
		});
	}

	private static void monitorServerStatus() {
		logger.warn("start up server monitor...");
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						TimeUnit.SECONDS.sleep(5);
						monitorGameServers();
					} catch (Exception e) {
						LogManager.error(e);
					}
				}
			}
		}.start();
		// scheduleTask(new DelayedTask(5 * 1000, 5 * 1000) {
		// @Override
		// public void run() {
		// monitorGameServers();
		// }
		// });
	}

	/** 初始化服务器列表 */
	private static void loadGameServers() {
		int count = CrossArenaManager.getInstance().serverList.size();
		final CountDownLatch finished = new CountDownLatch(count);
		for (Integer id : CrossArenaManager.getInstance().serverList) {
			try {
				// 设置回调
				IceEntry.setupCrossArenaCallbackPrx(id, finished);
			} catch (Exception e) {
				logger.warn(TextUtil.format("setupCallback error serverId: {0}.", id), e);
				finished.countDown();
			}
		}
		try {
			finished.await();
		} catch (InterruptedException e) {
			logger.warn("Finished monitor CountDownLatch is interrupted.", e);
		}
		logger.warn("loader server finished...");
	}

	/** 监控服务器状态 */
	private static void monitorGameServers() {
		for (Integer id : CrossArenaManager.getInstance().serverList) {
			try {
				// logger.warn(TextUtil.format("start monitor {0}", id));
				IceEntry.monitorStatus(id);
			} catch (Exception e) {
				logger.warn(TextUtil.format("monitorStatus error serverId: {0}.", id), e);
			}
			// logger.warn(TextUtil.format("end monitor {0}", id));
		}
		// LogManager.warn("getActiveCount：" + logicExecutor.getActiveCount());
		// 打印队列积压
		int size = logicExecutor.getQueue().size();
		if (size >= 100) {
			LogManager.warn("logicExecutor queue size：" + size);
		}
	}

	public static void scheduleTask(DelayedTask task) {
		timer.putTask(task);
	}

	/**
	 * 停止辅助线程，主逻辑线程工作
	 */
	public static void shutdown() {
		timer.setWork(false);
		logicExecutor.shutdownNow();
	}

	/**
	 * 使用主逻辑线程执行
	 * 
	 * @param runable
	 */
	public static void execute(Runnable runnable) {
		if (runnable == null || logicExecutor.isShutdown()) {
			return;
		}

		if (Thread.currentThread().getId() == logicThreadId) {// 调用线程就是主线程则直接执行
			runnable.run();
		} else {
			logicExecutor.execute(runnable);
		}
	}

	/**
	 * 设置动态过载状态
	 * 
	 * @param b
	 */
	public static void setDynamicOverload(boolean b) {
		dynamicOverload = b;
	}

	/**
	 * 设置静态过载状态
	 * 
	 * @param b
	 */
	public static void setStaticOverload(boolean b) {
		staticOverload = b;
	}

	/**
	 * 获取服务器是否过载状态，内部结合动态和静态两个状态标识，只要有一个显示过载则返回true
	 * 
	 * @return
	 */
	public static boolean isOverload() {
		// TODO ... 是否过载
		return staticOverload || dynamicOverload;
	}

	public static int getLogicQueueSize() {
		return logicExecutor.getQueue().size();
	}

	public static boolean isTerminated() {
		return logicExecutor.isTerminated();
	}
}

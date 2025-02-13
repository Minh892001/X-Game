package com.morefun.XSanGo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

/**
 * @author guofeng.qin
 */
public class ThreadPool {
	private final static Log logger = LogFactory.getLog(ThreadPool.class);

	private int DB_THREAD_NUM = 1;

	protected ExecutorService executor;

	private AtomicLong queueSize = new AtomicLong();
	
	public ThreadPool(int num) {
		DB_THREAD_NUM = num;
		executor = Executors.newFixedThreadPool(DB_THREAD_NUM);
	}

	public void execute(Runnable runable) {
		execute(runable, null);
	}

	public void execute(final Runnable dbTask, final Runnable logicCallback) {
		queueSize.incrementAndGet();
		final Throwable trace = new Throwable();
		String dbTaskName = "null-name";
		if (dbTask != null) {
			dbTaskName = dbTask.getClass().getName();
		}
		executor.execute(new DBRunnable(dbTaskName) {

			@Override
			public void run() {
				try {
					if (dbTask != null) {
						dbTask.run();
					}
				} catch (Exception e) {
					LogManager.error(e);
					LogManager.error(trace);
				} finally {
					queueSize.decrementAndGet();
					if (logicCallback != null) {
						LogicThread.execute(logicCallback);
					}
				}
			}
		});
	}

	/**
	 * 停止所有数据库线程，在此之前会执行完所有待执行任务但不再接受新的任务
	 * 
	 */
	public void shutdown() {
		executor.shutdown();
	}

	/**
	 * 是否所有数据库线程都执行完毕，只有在shutdown方法调用后才有可能返回true
	 * 
	 * @return
	 */
	public boolean isTerminated() {
		return executor.isTerminated();
	}

	public boolean isShutdown() {
		return executor.isShutdown();
	}

	/**
	 * 队列长度
	 * 
	 * @return
	 */
	public long getQueueSize() {
		return queueSize.get();
	}

	/** 打印执行队列内容 */
	public void printQueue() {
		if (executor != null) {
			ThreadPoolExecutor poolExe = (ThreadPoolExecutor) executor;
			BlockingQueue<Runnable> queue = poolExe.getQueue();
			if (queue != null) {
				Map<String, Integer> statistics = new HashMap<String, Integer>();
				for (Runnable runnable : queue) {
					String name = "";
					if (runnable instanceof DBRunnable) {
						name = ((DBRunnable) runnable).getParentName();
					} else {
						name = TextUtil.format("noparent:{0}", runnable.getClass().getName());
					}
					int count = 0;
					if (statistics.containsKey(name)) {
						count = statistics.get(name);
					}
					count++;
					statistics.put(name, count);
				}
				if (statistics.size() > 0) {
					logger.error(">>>>>>>>>>>>>>>>> DBThread <<<<<<<<<<<<<<<<<<<<<");
					for (Map.Entry<String, Integer> entry : statistics.entrySet()) {
						logger.error(TextUtil.format("DBThreadQueue: {0}:{1}", entry.getKey(), entry.getValue()));
					}
					logger.error(">>>>>>>>>>>>>>>>> DBThread <<<<<<<<<<<<<<<<<<<<<");
				}
			}
		}
	}
	
	public static abstract class DBRunnable implements Runnable {
		private String parentName;
		public DBRunnable(String parentName) {
			this.parentName = parentName;
		}
		public String getParentName() {
			return parentName;
		}
	}

}

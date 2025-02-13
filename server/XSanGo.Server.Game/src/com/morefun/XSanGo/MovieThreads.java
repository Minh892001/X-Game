package com.morefun.XSanGo;

/**
 * 战报生成线程
 * 
 * @author xiongming.li
 *
 */
public class MovieThreads {

	public static final int DB_THREAD_NUM = 10;

	private static ThreadPool threadPool = new ThreadPool(DB_THREAD_NUM);

	public static void execute(Runnable runable) {
		threadPool.execute(runable, null);
	}

	public static void execute(final Runnable dbTask, final Runnable logicCallback) {
		threadPool.execute(dbTask, logicCallback);
	}

	/**
	 * 停止所有数据库线程，在此之前会执行完所有待执行任务但不再接受新的任务
	 * 
	 */
	public static void shutdown() {
		threadPool.shutdown();
	}

	/**
	 * 是否所有数据库线程都执行完毕，只有在shutdown方法调用后才有可能返回true
	 * 
	 * @return
	 */
	public static boolean isTerminated() {
		return threadPool.isTerminated();
	}

	public static boolean isShutdown() {
		return threadPool.isShutdown();
	}

	/**
	 * 队列长度
	 * 
	 * @return
	 */
	public static long getQueueSize() {
		return threadPool.getQueueSize();
	}

	/** 打印执行队列内容 */
	public static void printQueue() {
		threadPool.printQueue();
	}
}

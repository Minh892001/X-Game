/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.MFBI;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.morefun.XSanGo.util.LogManager;
import com.morefun.bi.sdk.Logger;

/**
 * BI数据接口，发送数据中心的线程
 * 
 * @author 吕明涛
 * 
 */
public class DelayedBILogThread extends Thread {

	private static final Log log = LogFactory.getLog(DelayedBILogThread.class);
	
	/** 任务队列 */
	private LinkedBlockingQueue<BIMessage> msgQueue = new LinkedBlockingQueue<BIMessage>();
	

	/**
	 * 初始化和开启BI数据发送
	 */
	public void initMFBI() {
		if (XsgMFBIManager.getInstance().isStart) {
			try {
				log.info("============initMFBI start============");
				Logger.getInstance().setEnable(true); // 设置sdk是否有效，必要时可以关掉
				Logger.getInstance().init(XsgMFBIManager.getInstance().gameId); // appid
				Logger.getInstance().setPutInterval(1000); // 发送日志的最大间隔时间，单位毫秒
				Logger.getInstance().setPutBatchSize(100); // 队列最大长度，一旦超出就立即发送
				log.info("============initMFBI end==============");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 设置sdk是否有效
	 * @param enable
	 */
	public void setEnable(boolean enable) {
		log.info("MFBI setEnable : " + enable);
		Logger.getInstance().setEnable(enable); 
	}

	/**
	 * 关闭BI数据发送
	 * @throws InterruptedException 
	 */
	public void shutdown() {
		log.info("MFBI fini");
		Logger.getInstance().fini();
	}

	/**
	 * @param message
	 */
	public void addMess(BIMessage msg) {
		this.msgQueue.add(msg);
	}

	/**
	 * 从任务队列里取出最近的任务并执行
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		initMFBI();

		while (XsgMFBIManager.getInstance().isStart) {
			try {
				BIMessage BI_Log = msgQueue.take();
				if (BI_Log != null) {
					Logger.getInstance().log(BI_Log.getUserInfo(), BI_Log.getRoleInfo(),
							BI_Log.getType(), BI_Log.getTimestamps(), BI_Log.getParams());
				}
			} catch (Exception e) {
				LogManager.error(e);
			}
		}
	}
}

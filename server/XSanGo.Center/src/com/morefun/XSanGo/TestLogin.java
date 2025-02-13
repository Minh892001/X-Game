/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo;

import java.io.FileNotFoundException;
import java.io.IOException;

import net.sf.ehcache.CacheManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.morefun.XSanGo.http.HttpLancher;
import com.morefun.XSanGo.net.IceEntry;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 
 */
public class TestLogin {

	protected final static Log logger = LogFactory.getLog(TestLogin.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			logger.debug("Center Server start!");
			// 初始化数据库
			LoginDatabase.instance().init();
			logger.debug("DB on line!");

			// 初始化网络
			IceEntry.instance().init(args);
			logger.debug("Ice on line!");

			// 初始化服务
			CenterServer.instance().init();
			logger.debug("CenterServer on line!");

			// 开始服务
			CenterServer.instance().start();
			logger.debug("login Server on line!");

			HttpLancher.startup();

			ChargeNotifyer.getInstance().start();
		} catch (Exception e) {
			e.printStackTrace();
			return;

		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				// 通过系统关闭
				saveExit();
			}
		});

		IceEntry.instance().getCommunicator().waitForShutdown();

		saveExit();

	}

	/**
	 * 
	 */
	private static void saveExit() {
		HttpLancher.stop();
		try {
			CenterServer.instance().shutdown();
		} catch (FileNotFoundException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}

		XsgAccountManager.getInstance().shutdown();
		LoginDatabase.shutdown();
		try {
			while (!LoginDatabase.isTerminated()) {
				logger.info(TextUtil.format(
						"Waiting for Database completed,{0} tasks remain.",
						LoginDatabase.getQueueSize()));
				Thread.sleep(2000);
			}
		} catch (InterruptedException e) {
			logger.error(e);
		}

		logger.info("Closing cache");
		CacheManager.getInstance().shutdown();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			logger.error(e);
		}

		logger.info("Good bye!");
	}
}

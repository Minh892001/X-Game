package com.morefun.XSanGo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.morefun.XSanGo.net.IceEntry;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

public class CrossArenaMain {
	private final static Log logger = LogFactory.getLog(CrossArenaMain.class);

	private static ApplicationContext ac;
	private static String springConfig = "applicationContextMain.xml";
	private static String appConfig = "ice.config";
	private static String iceConfig = appConfig;

	private static String parseArgValue(String arg) {
		return arg.substring(arg.lastIndexOf("=") + 1, arg.length());
	}

	private static void parseConfigPath(String[] args) {
		logger.warn("解析配置文件...");
		for (String arg : args) {
			// spring config
			if (arg.startsWith("--Spring.Config=")) {
				springConfig = parseArgValue(arg);
			}
			// app properties
			if (arg.startsWith("--Ice.Config=")) {
				iceConfig = parseArgValue(arg);
			}
		}
	}

	private static void logConfigsPath() {
		logger.warn("==========  ==========");
		logger.warn(TextUtil.format("SpringApplicationConfig:{0}", springConfig));
		logger.warn(TextUtil.format("AppProperties:{0}", appConfig));
		logger.warn(TextUtil.format("IceConfig:{0}", iceConfig));
		logger.warn("==========  ==========");
	}

	/** 系统关闭的回调 */
	private static void onShutdown() {
		logger.warn("Server shutdown...");
		LogicThread.shutdown();
		try {
			// 所有缓存再保存一遍
			CrossArenaManager.getInstance().saveAllCache();
			DBThreads.shutdown();
			while (!DBThreads.isTerminated()) {
				logger.info(TextUtil.format("Waiting for Database completed,{0} tasks remain.",
						DBThreads.getQueueSize()));
				Thread.sleep(2000);
			}
		} catch (InterruptedException e) {
			LogManager.error(e);
		}
		logger.info("Cross arena Over!");
	}

	public static void main(String[] args) {
		parseConfigPath(args);
		ac = new ClassPathXmlApplicationContext(springConfig);
		// appConfig = ac.getBean("AppConfigFile", String.class);
		logConfigsPath();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				logger.info("server shutdown...");
				// 通过系统关闭
				onShutdown();
			}
		});
		logger.info("初始化网络...");
		IceEntry.init(args);

		logger.info("初始化服务...");
		CrossArenaManager.getInstance().init();

		logger.info("初始化逻辑线程...");
		LogicThread.init();

		logger.info("初始化排行榜...");
		CrossArenaManager.getInstance().initRank();

		startupEnd();

		IceEntry.getCommunicator().waitForShutdown();

	}

	/**
	 * start finished
	 * */
	private static void startupEnd() {
		logger.warn("#     #  #####                 #####         ");
		logger.warn(" #   #  #     #   ##   #    # #     #  ####  ");
		logger.warn("  # #   #        #  #  ##   # #       #    # ");
		logger.warn("   #     #####  #    # # #  # #  #### #    # ");
		logger.warn("  # #         # ###### #  # # #     # #    # ");
		logger.warn(" #   #  #     # #    # #   ## #     # #    # ");
		logger.warn("#     #  #####  #    # #    #  #####   ####  ");
	}

	public static ApplicationContext getAC() {
		return ac;
	}
}

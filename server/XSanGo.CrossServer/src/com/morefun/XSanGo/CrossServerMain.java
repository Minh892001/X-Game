package com.morefun.XSanGo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.morefun.XSanGo.net.IceEntry;
import com.morefun.XSanGo.util.TextUtil;

/**
 * @author guofeng.qin
 */
public class CrossServerMain {
	private final static Log logger = LogFactory.getLog(CrossServerMain.class);

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
		logger.warn(TextUtil
				.format("SpringApplicationConfig:{0}", springConfig));
		logger.warn(TextUtil.format("AppProperties:{0}", appConfig));
		logger.warn(TextUtil.format("IceConfig:{0}", iceConfig));
		logger.warn("==========  ==========");
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

	/** 系统关闭的回调 */
	private static void onShutdown() {
		logger.warn("Server shutdown...");
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
		// 初始化网络
		logger.info("初始化网络...");
		IceEntry.init(args);
		// 初始化服务
		logger.info("初始化服务...");
		CrossServerManager.getInstance().init();
		// 初始化逻辑线程
		logger.info("初始化逻辑线程...");
		LogicThread.init();
		// 加载机器人
		CrossServerManager.getInstance().loadRobot();
		
		startupEnd();

		IceEntry.getCommunicator().waitForShutdown();
	}

	public static ApplicationContext getAC() {
		return ac;
	}
}

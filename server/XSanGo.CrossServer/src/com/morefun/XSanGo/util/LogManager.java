/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 日志管理器
 * 
 * @author BruceSu
 * 
 */
public class LogManager {
	private final static Log logger = LogFactory.getLog(LogManager.class);

	public static void error(Throwable e) {
		logger.error(e, e);
	}

	public static void warn(Object msg) {
		logger.warn(msg);
	}
	
	public static void info(Object msg) {
		logger.info(msg);
	}
}

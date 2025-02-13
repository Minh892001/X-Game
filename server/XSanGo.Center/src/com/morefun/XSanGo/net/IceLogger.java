/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.net;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import Ice.Logger;

public class IceLogger implements Logger {

	protected final static Log logger = LogFactory.getLog(IceLogger.class);
	
	
	@Override
	public void warning(String message) {
		logger.warn(message);				
	}
	
	@Override
	public void trace(String category, String message) {
		logger.info(category+":"+message)		;
	}
	
	@Override
	public void print(String message) {
		logger.debug(message)		;
		
	}
	
	@Override
	public void error(String message) {
		logger.error(message)		;
				
	}
	
	@Override
	public Logger cloneWithPrefix(String prefix) {
		return null;
	}

}

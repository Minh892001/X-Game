package com.morefun.XSanGo.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author guofeng.qin
 */
public class XsgCacheManager {
	private final static Log logger = LogFactory.getLog(XsgCacheManager.class);

	private static final class InnerHolder {
		private static final XsgCacheManager instance = new XsgCacheManager();
	}

	private XsgCacheManager() {
	}

	public static XsgCacheManager getInstance() {
		return InnerHolder.instance;
	}
}

/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.cache;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

/**
 * 缓存事件监听基类
 * 
 * @author BruceSu
 * 
 */
class CacheListener implements CacheEventListener {

	@Override
	public void dispose() {
	}

	@Override
	public void notifyElementEvicted(Ehcache arg0, Element arg1) {
		// System.out.println("缓存满，被移除KEY = " + arg1.getObjectKey());
	}

	@Override
	public void notifyElementExpired(Ehcache arg0, Element arg1) {
		// System.out.println("缓存过期，被移除KEY = " + arg1.getObjectKey());
	}

	@Override
	public void notifyElementPut(Ehcache arg0, Element arg1)
			throws CacheException {
	}

	@Override
	public void notifyElementRemoved(Ehcache arg0, Element arg1)
			throws CacheException {
		// System.out.println("缓存被移除KEY = " + arg1.getObjectKey());
	}

	@Override
	public void notifyElementUpdated(Ehcache arg0, Element arg1)
			throws CacheException {

	}

	@Override
	public void notifyRemoveAll(Ehcache arg0) {
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}

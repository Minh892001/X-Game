/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.cache;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 角色缓存监听器
 * 
 * @author BruceSu
 * 
 */
class RoleCacheListener extends CacheListener {
	@Override
	public void notifyElementEvicted(Ehcache arg0, Element arg1) {
		super.notifyElementEvicted(arg0, arg1);
		this.onElementRemoved(arg0, arg1);
	}

	@Override
	public void notifyElementExpired(Ehcache arg0, Element arg1) {
		super.notifyElementExpired(arg0, arg1);
		this.onElementRemoved(arg0, arg1);
	}

	private void onElementRemoved(Ehcache cache, Element element) {
		IRole role = (IRole) element.getObjectValue();
		if (role.isOnline()) {
			LogManager.warn(TextUtil.format(
					"[{0},{1}] is online,then readd to the cache.",
					role.getRoleId(), role.getName()));
			cache.put(element);
		} else {
			role.saveAsyn();
			role.destory();
		}
	}
}

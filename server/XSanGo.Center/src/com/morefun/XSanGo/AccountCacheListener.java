/**
 * 
 */
package com.morefun.XSanGo;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.morefun.XSanGo.db.Account;

/**
 * 帐号缓存监听对象
 * 
 * @author sulingyun
 *
 */
public class AccountCacheListener implements CacheEventListener {
	protected final static Log logger = LogFactory
			.getLog(AccountCacheListener.class);

	@Override
	public void dispose() {

	}

	@Override
	public void notifyElementEvicted(Ehcache arg0, Element arg1) {
		this.onElementRemoved(arg1);
	}

	@Override
	public void notifyElementExpired(Ehcache arg0, Element arg1) {
		this.onElementRemoved(arg1);
	}

	@Override
	public void notifyElementPut(Ehcache arg0, Element arg1)
			throws CacheException {
	}

	@Override
	public void notifyElementRemoved(Ehcache arg0, Element arg1)
			throws CacheException {
		this.onElementRemoved(arg1);
	}

	@Override
	public void notifyElementUpdated(Ehcache arg0, Element arg1)
			throws CacheException {
	}

	@Override
	public void notifyRemoveAll(Ehcache arg0) {
		for (Object key : arg0.getKeysWithExpiryCheck()) {
			this.onElementRemoved(arg0.get(key));
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	private void onElementRemoved(Element element) {
		Account account = (Account) element.getObjectValue();
		// logger.warn(TextUtil.format("[{0}] is saving...",
		// account.getAccount()));
		XsgAccountManager.getInstance().updateAccount(account);
	}
}

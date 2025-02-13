/**
 * 
 */
package com.morefun.XSanGo;

import java.util.Date;

import com.morefun.XSanGo.db.Account;
import com.morefun.XSanGo.db.AccountDAO;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 帐号管理类
 * 
 * @author sulingyun
 *
 */
public class XsgAccountManager {
	private static XsgAccountManager instance = new XsgAccountManager();

	public static XsgAccountManager getInstance() {
		return instance;
	}

	private Cache loginCache;
	private Cache accountCache;
	private AccountDAO accountDao;

	private XsgAccountManager() {
		loginCache = CacheManager.getInstance().getCache("login_key");
		accountDao = AccountDAO.getFromApplicationContext(LoginDatabase
				.instance().getAc());
		accountCache = CacheManager.getInstance().getCache("account");
		accountCache.getCacheEventNotificationService().registerListener(
				new AccountCacheListener());
	}

	public Account createAccount(String accountName, int channel, String mac) {
		Account account = new Account(accountDao.getNextId(), accountName, "",
				channel, mac, new Date());
		if (!LoginDatabase.instance().getAc()
				.getBean("NeedActive", Boolean.class)) {
			account.setActive(1);
		}
		return account;
	}

	/**
	 * 登录成功，缓存相关数据
	 * 
	 * @param db
	 * @param sessionId
	 *            会话标识
	 * @param mac
	 * @param ip
	 */
	public void loginSuccess(Account db, String sessionId, String mac,
			String ip, String version, int channel) {
		loginCache.put(new Element(sessionId, new SessionData(db.getAccount(),
				mac, ip, version, channel)));
		accountCache.put(new Element(db.getAccount(), db));
	}

	/**
	 * 先在缓存中查找，没有则请求数据库，所以可能有IO消耗
	 * 
	 * @param accountName
	 * @return
	 */
	public Account findAccount(String accountName) {
		Element element = this.accountCache.get(accountName);
		if (element != null) {
			return (Account) element.getObjectValue();
		}

		Account account = this.accountDao.findByName(accountName);
		if (account != null) {
			this.accountCache.put(new Element(accountName, account));
		}
		return account;
	}

	/**
	 * 根据登录的会话标识获取对应帐号，没有则返回NULL
	 * 
	 * @param sessionId
	 * @return
	 */
	public Account findAccountBySessionId(String sessionId) {
		SessionData sessionData = this.findSessionData(sessionId);
		if (sessionData == null) {
			return null;
		}
		Element element = accountCache.get(sessionData.getAccount());
		return ((element != null && !element.isExpired()) ? (Account) element
				.getObjectValue() : null);
	}

	/**
	 * 获取登录会话数据
	 * 
	 * @param sessionId
	 * @return
	 */
	public SessionData findSessionData(String sessionId) {
		Element element = this.loginCache.get(sessionId);
		return ((element != null && !element.isExpired()) ? (SessionData) element
				.getObjectValue() : null);
	}

	/**
	 * 将数据更新到数据库中
	 * 
	 * @param account
	 */
	public void updateAccount(final Account account) {
		LoginDatabase.execute(new Runnable() {
			@Override
			public void run() {
				accountDao.attachDirty(account);
			}
		});

	}

	public void shutdown() {
		for (Object key : this.accountCache.getKeys()) {
			Element element = this.accountCache.get(key);
			if (element != null) {
				this.updateAccount((Account) element.getObjectValue());
			}
		}
	}
}

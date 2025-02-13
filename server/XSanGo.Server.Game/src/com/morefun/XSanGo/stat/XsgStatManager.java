/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.stat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import com.XSanGo.Protocol.CurrencyType;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.cache.XsgCacheManager;
import com.morefun.XSanGo.db.stat.StatEcnomy;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

public class XsgStatManager {

	public static final String Ecnomy_Cache_Name = "ecnomy_stat";
	private static XsgStatManager instance;

	public static XsgStatManager getInstance() {
		if (instance == null) {
			instance = new XsgStatManager();
		}
		return instance;
	}

	private Map<CurrencyType, StatEcnomy> statMap;
	private Cache statCache;

	protected XsgStatManager() {
		this.statCache = XsgCacheManager.getInstance().getCache(
				Ecnomy_Cache_Name);
		this.statMap = new HashMap<CurrencyType, StatEcnomy>();
		for (Object key : this.statCache.getKeysWithExpiryCheck()) {
			CurrencyType type = CurrencyType.valueOf(key.toString());
			Element element = this.statCache.get(key);
			if (element == null) {
				LogManager
						.warn(TextUtil.format("Stat cache {0} is null.", key));
			} else {
				StatEcnomy se = (StatEcnomy) element.getObjectValue();
				this.statMap.put(type, se);
				System.out.println(se.toString());
			}
		}

		this.statCache.removeAll();
	}

	public static void main(String[] args) {
		Cache statCache = XsgCacheManager.getInstance().getCache(
				Ecnomy_Cache_Name);
		statCache.removeAll();
		statCache.put(new Element(CurrencyType.AuctionMoney.toString(),
				new StatEcnomy(new Date(), 11, CurrencyType.AuctionMoney
						.ordinal(), 323, 522, 243521)));

		for (Object key : statCache.getKeysWithExpiryCheck()) {
			// CurrencyType type = CurrencyType.valueOf(key.toString());
			Element element = statCache.get(key);
			if (element == null) {
				LogManager
						.warn(TextUtil.format("Stat cache {0} is null.", key));
			} else {
				StatEcnomy se = (StatEcnomy) element.getObjectValue();
				System.out.println(key + se.toString());
			}
		}
		XsgCacheManager.getInstance().shutdown();
	}

	public IStatControler createStatControler(IRole role) {
		return new StatControler(role);
	}

	public void onBindYuanbaoChange(int change) {
		this.handleValueChange(CurrencyType.BindYuanbao, change);
	}

	/**
	 * 处理数值变化，并更新缓存
	 * 
	 * @param se
	 * @param change
	 */
	public void handleValueChange(CurrencyType type, long change) {
		StatEcnomy se = this.getStatEcnomy(type);
		if (change > 0) {
			se.setProduce(se.getProduce() + change);
		} else {
			se.setConsume(se.getConsume() - change);
		}
	}

	/**
	 * 获取指定货币类型的统计数据
	 * 
	 * @param currency
	 * @return
	 */
	public StatEcnomy getStatEcnomy(CurrencyType currency) {
		if (!this.statMap.containsKey(currency)) {
			this.statMap.put(currency,
					new StatEcnomy(new Date(), ServerLancher.getServerId(),
							currency.ordinal(), 0, 0, 0));
		}

		return this.statMap.get(currency);
	}

	public void onUnbindYuanbaoChange(int change) {
		this.handleValueChange(CurrencyType.UnbindYuanbao, change);
	}

	public void onJinbiChange(long change) {
		this.handleValueChange(CurrencyType.Jinbi, change);
	}

	/**
	 * 公会奖章变化处理
	 * 
	 * @param change
	 */
	public void onFactionTokenChange(int change) {
		this.handleValueChange(CurrencyType.FactionToken, change);
	}

	/**
	 * 清理数据，重置统计
	 * 
	 * @return
	 */
	public void clearData() {
		this.statMap.clear();
	}

	/**
	 * 写入缓存
	 */
	public void save2Cache() {
		for (CurrencyType currency : this.statMap.keySet()) {
			this.statCache.put(new Element(currency.toString(), this.statMap
					.get(currency)));
		}
	}
}

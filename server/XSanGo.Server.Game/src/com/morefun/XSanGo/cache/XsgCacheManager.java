/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.cache;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.SearchAttribute;
import net.sf.ehcache.config.Searchable;
import net.sf.ehcache.search.Result;

import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.db.EntityMeta;
import com.morefun.XSanGo.db.HibernateExtension;
import com.morefun.XSanGo.util.FileUtil;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

/**
 * @author Su LingYun
 * 
 */
public class XsgCacheManager {
	private static XsgCacheManager instance;

	public static XsgCacheManager getInstance() {
		if (instance == null) {
			instance = new XsgCacheManager();
		}
		return instance;
	}

	private CacheManager cacheManager;

	private XsgCacheManager() {
		// 初始化cache管理器
		this.cacheManager = CacheManager.getInstance();

		this.registerEventListener();
		try {
			this.initPersistentData();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * 初始化持久化数据
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws CacheException
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private void initPersistentData() throws IllegalArgumentException, IllegalStateException, CacheException,
			IOException, ClassNotFoundException, IllegalAccessException, InvocationTargetException {
		File persistentPath = new File(getPersistentPath());
		if (!persistentPath.exists()) {
			return;
		}
		for (File subPath : persistentPath.listFiles()) {
			if (subPath.isFile()) {
				continue;
			}

			String cacheName = subPath.getName();
			Cache cache = this.getCache(cacheName);
			for (File f : subPath.listFiles()) {
				// .xsg文件
				String key = f.getName().substring(0, f.getName().length() - 4);
				Object obj = FileUtil.readObjectFromFile(f);
				this.persistentDataCompatible(obj);
				cache.put(new Element(key, obj));
			}
		}
	}

	/**
	 * 持久化数据兼容性处理，旧版本没有的集合或者MAP类型字段初始化
	 * 
	 * @param obj
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public void persistentDataCompatible(Object obj) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		EntityMeta reflaction = HibernateExtension.getEntityMeta(obj.getClass());
		Map<String, Method> setMethodMap = reflaction.getSetMethodMap();
		for (Method m : reflaction.getOneToManyChildren()) {
			Object children = m.invoke(obj);
			if (children == null) {
				Class<?> returnType = m.getReturnType();
				String setMethodName = m.getName().replaceFirst("get", "set");
				Method setMethod = setMethodMap.get(setMethodName);
				if (setMethod == null) {
					continue;
				}
				if (returnType.isAssignableFrom(ArrayList.class)) {
					setMethod.invoke(obj, new ArrayList());
				} else if (returnType.isAssignableFrom(HashSet.class)) {
					setMethod.invoke(obj, new HashSet());
				} else if (returnType.isAssignableFrom(HashMap.class)) {
					setMethod.invoke(obj, new HashMap());
				}
			} else {
				if (children instanceof Map) {
					children = ((Map) children).values();
				}

				Collection childCollection = (Collection) children;
				for (Object item : childCollection) {
					// 递归调用
					persistentDataCompatible(item);
				}
			}

		}
	}

	/**
	 * 获取缓存路径
	 * 
	 * @return
	 */
	public String getDiskStorePath() {
		return this.cacheManager.getConfiguration().getDiskStoreConfiguration().getPath();
	}

	public String getPersistentPath() {
		return this.getDiskStorePath() + "/" + "persistent";
	}

	/**
	 * 注册缓存事件
	 */
	private void registerEventListener() {
		Cache roleCache = this.getCache(Const.Role.CACHE_NAME_ROLE);
		roleCache.getCacheEventNotificationService().registerListener(new RoleCacheListener());
	}

	public CacheConfiguration createWorldBossConfig(String name) {
		CacheConfiguration cacheConfig = new CacheConfiguration(name, 10000).eternal(true);
		Searchable searchable = new Searchable();
		cacheConfig.addSearchable(searchable);

		searchable.addSearchAttribute(new SearchAttribute().name("ranking").expression("value.getRanking()"));
		searchable.addSearchAttribute(new SearchAttribute().name("bossId").expression("value.getBossId()"));
		searchable.addSearchAttribute(new SearchAttribute().name("totalDamage").expression("value.getTotalDamage()"));
		searchable.addSearchAttribute(new SearchAttribute().name("lastHitTime").expression("value.getLastHitTime()"));

		return cacheConfig;

		// this.cacheManagerConfig.addCache(cacheConfig);
	}

	public Cache getCache(String name) {
		return cacheManager.getCache(name);
	}

	public void addCache(CacheConfiguration config) {
		cacheManager.addCache(new Cache(config));
	}

	/**
	 * 解析缓存查询结果，获取具体的缓存值
	 * 
	 * @param <T>
	 * @param queryList
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> parseCacheValue(List<Result> queryList, Class<T> type) {
		List<T> resultList = new ArrayList<T>();
		for (Result cache : queryList) {
			resultList.add((T) cache.getValue());
		}

		return resultList;
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		Configuration managerConfig = new Configuration();
		managerConfig.setName("testManager");

		CacheConfiguration cacheConfig = new CacheConfiguration("testCache", 100);
		cacheConfig.setTimeToIdleSeconds(10);
		cacheConfig.setTimeToLiveSeconds(20);
		managerConfig.addCache(cacheConfig);
		Searchable searchable = new Searchable();
		cacheConfig.addSearchable(searchable);

		CacheManager cacheManager = new CacheManager(managerConfig);

		Cache testCache = cacheManager.getCache("testCache");
		testCache.getCacheEventNotificationService().registerListener(new CacheListener() {
		});

		for (int i = 0; i < 20; i++) {
			testCache.put(new Element(i, new Object()));
		}

		System.out.println(testCache.createQuery().includeValues().execute().hasValues());

		Thread.sleep(5000);
		for (int i = 0; i < 20; i++) {
			testCache.get(i);
		}
	}

	/**
	 * 首先递归删除错误数据存储目录，检查是否有需要持久化的缓存数据，有则通过自定义的文件形式存储，最后关闭ehcache
	 */
	public void shutdown() {
		String persistentPath = this.getPersistentPath();
		FileUtil.delete(new File(persistentPath));
		// 配置了持久化功能的缓存自定义序列化到磁盘
		for (String name : this.cacheManager.getCacheNames()) {
			Cache cache = this.getCache(name);
			boolean diskPersistent = cache.getCacheConfiguration().isDiskPersistent();
			if (!diskPersistent || cache.getSize() == 0) {
				continue;
			}

			for (Object key : cache.getKeysWithExpiryCheck()) {
				String fileName = TextUtil.format("{0}/{1}/{2}.xsg", persistentPath, name, key.toString());
				FileUtil.saveFile(fileName, TextUtil.objectToBytes((Serializable) cache.get(key).getObjectValue()));
			}
		}
		this.cacheManager.shutdown();
	}

	/**
	 * 输出缓存监控信息
	 */
	public void showMonitorInfo() {
		for (String name : this.cacheManager.getCacheNames()) {
			Cache cache = this.getCache(name);
			int current = cache.getSize();
			long max = cache.getCacheConfiguration().getMaxEntriesLocalHeap();

			if (current * 100 / max > 30) {
				LogManager.warn(TextUtil.format("Cache name:{0},size:{1}/{2}.", name, current, max));
			}
		}
	}

	/**
	 * 计算并在控制台输出各缓存实际内存占用，该方法功能效率低，谨慎使用
	 */
	public void calculateMemorySizeUse() {
		Map<String, Long> map = new HashMap<String, Long>();
		for (String name : this.cacheManager.getCacheNames()) {
			long size = this.getCache(name).calculateInMemorySize();
			map.put(name, size);
		}

		long total = 0;
		for (String name : map.keySet()) {
			long size = map.get(name);
			System.out.println(TextUtil.format("{0}:\t{1}.", name, size));
			total += size;
		}
		System.out.println(TextUtil.format("total size:\t{0}.", total));
	}
}
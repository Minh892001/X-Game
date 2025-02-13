package com.morefun.XSanGo.auction;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Direction;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.expression.Criteria;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.XSanGo.Protocol.ItemType;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.achieve.XsgAchieveManager.AchieveTemplate;
import com.morefun.XSanGo.cache.XsgCacheManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.db.game.AuctionHouseDao;
import com.morefun.XSanGo.db.game.AuctionHouseItem;
import com.morefun.XSanGo.db.game.AuctionHouseRecord;
import com.morefun.XSanGo.db.game.Mail;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleItem;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.mail.InstanceAttachObject;
import com.morefun.XSanGo.mail.MailRewardT;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 拍卖行全局管理类
 * 
 * @author qinguofeng
 * @date Mar 27, 2015
 */
public class XsgAuctionHouseManager {
	private static final Log logger = LogFactory.getLog(XsgAuctionHouseManager.class);

	// Ehcache 缓存相关索引关键字配置
	public static final String CACHE_NAME_AUCTION_HOUSE = "auctionHouse";
	public static final String CACHE_AUCTION_HOUSE_RECORDS = "auctionHouseRecords";
	public static final String CACHE_NAME_AUCTION_HOUSE_ATTR_ENDTIME = "endTime";
	public static final String CACHE_NAME_AUCTION_HOUSE_ATTR_SELLER = "sellerId";
	public static final String CACHE_NAME_AUCTION_HOUSE_ATTR_AUCTION_ID = "auctionId";
	public static final String CACHE_NAME_AUCTIOH_HOUSE_ATTR_TYPE = "type";
	public static final String CACHE_NAME_AUCTIOH_HOUSE_ATTR_KEYWORD = "keyword";
	public static final String CACHE_NAME_AUCTIOH_HOUSE_ATTR_QUALITY = "quality";
	public static final String CACHE_NAME_AUCTIOH_HOUSE_ATTR_ROLEID = "roleId";
	public static final String CACHE_NAME_AUCTIOH_HOUSE_ATTR_UPDATETIME = "updateTime";
	public static final String CACHE_NAME_AUCTION_HOUSE_ATTR_BIDDER = "bidder";

	private static XsgAuctionHouseManager instance = new XsgAuctionHouseManager();

	public static XsgAuctionHouseManager getInstance() {
		return instance;
	}

	private AuctionHouseDao mAuctionHouseDao;

	private AuctionBaseConfigT auctionBaseConfig;

	private Ehcache auctionCache;
	private Ehcache auctionRecordsCache;

	private AuctionHouseDelayedTask auctionHouseDelayedTask = null;

	/**
	 * 刷新消耗配置
	 */
	private Map<Integer, Integer> refreshCoins = new HashMap<Integer, Integer>();

	/**
	 * 固定刷新商品
	 */
	private Map<Integer, AuctionShopRewardT> fixedShops = new HashMap<Integer, AuctionShopRewardT>();

	/**
	 * 随机刷新商品
	 */
	private Map<Integer, AuctionShopRewardT> randomShops = new HashMap<Integer, AuctionShopRewardT>();

	private XsgAuctionHouseManager() {
		XsgCacheManager cacheManager = XsgCacheManager.getInstance();
		auctionCache = cacheManager.getCache(CACHE_NAME_AUCTION_HOUSE);
		auctionRecordsCache = cacheManager.getCache(CACHE_AUCTION_HOUSE_RECORDS);
		if (auctionCache == null || auctionRecordsCache == null) {
			logger.error("拍卖行cache初始化错误.");
			return;
		}
		mAuctionHouseDao = AuctionHouseDao.getFromApplicationContext(ServerLancher.getAc());
		if (mAuctionHouseDao == null) {
			logger.error("AuctionHouseDao init error.");
			return;
		}
		List<AuctionBaseConfigT> cfgList = ExcelParser.parse(AuctionBaseConfigT.class);
		if (cfgList == null || cfgList.size() <= 0) {
			logger.error("拍卖行配置错误");
			return;
		}

		auctionBaseConfig = cfgList.get(0);

		List<AuctionRefreshConfigT> refreshConfigTs = ExcelParser.parse(AuctionRefreshConfigT.class);
		for (AuctionRefreshConfigT ar : refreshConfigTs) {
			refreshCoins.put(ar.times, ar.AuctionCoin);
		}
		List<AuctionShopRewardT> shops = ExcelParser.parse("拍卖行固定刷新", AuctionShopRewardT.class);
		for (AuctionShopRewardT s : shops) {
			fixedShops.put(s.id, s);
		}
		shops = ExcelParser.parse("拍卖行随机刷新", AuctionShopRewardT.class);
		for (AuctionShopRewardT s : shops) {
			randomShops.put(s.id, s);
		}
		initCacheData();
		startNextAuction();
	}

	/** 初始化缓存数据, 加载数据库内容到缓存中 */
	private void initCacheData() {
		// 加载数据库内容
		List<AuctionHouseItem> auctionList = mAuctionHouseDao.getAllAuctionItems();
		for (AuctionHouseItem item : auctionList) {
			XsgAuctionHouseItem xsgItem = new XsgAuctionHouseItem(item);
			// 拍卖项目加入缓存
			auctionCache.put(new Element(xsgItem.getId(), xsgItem));
			for (AuctionHouseRecord record : item.getRecords()) {
				XsgAuctionHouseRecord xsgRecord = new XsgAuctionHouseRecord(xsgItem, record);
				// 竞价记录加入缓存
				auctionRecordsCache.put(new Element(xsgRecord.getId(), xsgRecord));
			}
		}
	}

	/**
	 * 拼接查询条件
	 * 
	 * @param c1
	 *            条件1, 可以为空
	 * @param c2
	 *            条件2
	 * */
	private Criteria appendCriteria(Criteria c1, Criteria c2) {
		if (c1 == null) {
			return c2;
		}
		return c1.and(c2);
	}

	/**
	 * 构造查询条件,所有的参数如果传小于零的值或者控制则表示没有该条件约束
	 * 
	 * @param sellId
	 *            拍卖人
	 * @param typeInt
	 *            类型
	 * @param key
	 *            名字关键字匹配
	 * @param qualityInt
	 *            品质
	 * */
	private Criteria createCriteriaBy(String sellId, int typeInt, String key, int qualityInt) {
		Criteria criteria = null;
		if (!TextUtil.isBlank(sellId)) {
			Attribute<String> sellerId = auctionCache.getSearchAttribute(CACHE_NAME_AUCTION_HOUSE_ATTR_SELLER);
			criteria = appendCriteria(criteria, sellerId.eq(sellId));
		}
		if (typeInt > 0) {
			Attribute<Integer> type = auctionCache.getSearchAttribute(CACHE_NAME_AUCTIOH_HOUSE_ATTR_TYPE);
			criteria = appendCriteria(criteria, type.eq(typeInt));
		}
		if (!TextUtil.isBlank(key)) {
			Attribute<String> keyword = auctionCache.getSearchAttribute(CACHE_NAME_AUCTIOH_HOUSE_ATTR_KEYWORD);
			criteria = appendCriteria(criteria, keyword.ilike(TextUtil.format("*{0}*", key)));
		}
		if (qualityInt > 0) {
			Attribute<Integer> quality = auctionCache.getSearchAttribute(CACHE_NAME_AUCTIOH_HOUSE_ATTR_QUALITY);
			criteria = appendCriteria(criteria, quality.eq(qualityInt));
		}
		return criteria;
	}

	/**
	 * 根据查询条件获取拍卖行项目
	 * 
	 * @param direction
	 *            排序的方向;0,按照上架时间正续;1,倒序
	 * */
	public ItemsAndTotal<XsgAuctionHouseItem> getAuctionItems(int startIndex, int count, int typeInt, String key,
			int qualityInt, int direction) {
		ItemsAndTotal<XsgAuctionHouseItem> itemsAndTotal = new ItemsAndTotal<XsgAuctionHouseItem>();
		Attribute<Long> endTime = auctionCache.getSearchAttribute(CACHE_NAME_AUCTION_HOUSE_ATTR_ENDTIME);
		// 构造查询条件
		Criteria criteria = createCriteriaBy(null, typeInt, key, qualityInt);
		// 查询结果根据结束时间倒序, 即新上架的排在前面
		Query query = auctionCache.createQuery().includeValues()
				.addOrderBy(endTime, direction == 0 ? Direction.DESCENDING : Direction.ASCENDING);
		if (criteria != null) {
			query.addCriteria(criteria);
		}
		List<Result> resList = query.execute().all();
		if (resList != null && resList.size() > 0) {
			itemsAndTotal.totalCount = resList.size();
			itemsAndTotal.items = getSublistOf(resList, startIndex, count, XsgAuctionHouseItem.class);
		}
		return itemsAndTotal;
	}

	/**
	 * 获取某个用户对某个拍卖品的竞价记录
	 * 
	 * @param roleIdStr
	 *            用户ID
	 * @param auctionIdStr
	 *            拍卖ID
	 * */
	public XsgAuctionHouseRecord getAuctionRecord(String roleIdStr, String auctionIdStr) {
		Attribute<String> roleId = auctionRecordsCache.getSearchAttribute(CACHE_NAME_AUCTIOH_HOUSE_ATTR_ROLEID);
		Attribute<String> auctionId = auctionRecordsCache.getSearchAttribute(CACHE_NAME_AUCTION_HOUSE_ATTR_AUCTION_ID);
		// 用户ID 和 拍卖 ID 可以唯一确认一条叫价记录, 所有设置maxresult 为1
		Query query = auctionRecordsCache.createQuery().includeValues()
				.addCriteria(roleId.eq(roleIdStr).and(auctionId.eq(auctionIdStr))).maxResults(1);
		List<Result> resList = query.execute().all();
		if (resList != null && resList.size() > 0) {
			List<XsgAuctionHouseRecord> res = XsgCacheManager.parseCacheValue(resList, XsgAuctionHouseRecord.class);
			if (res != null && res.size() > 0) {
				return res.get(0);
			}
		}
		return null;
	}

	/**
	 * 获取用户竞拍的物品总数,即某个用户出价最高的拍卖品的个数
	 * */
	public int getRoleAuctionRecordsCount(String roleIdStr) {
		Attribute<String> bidderId = auctionCache.getSearchAttribute(CACHE_NAME_AUCTION_HOUSE_ATTR_BIDDER);
		Query query = auctionCache.createQuery().includeValues().addCriteria(bidderId.eq(roleIdStr));
		List<Result> resList = query.execute().all();
		return resList == null ? 0 : resList.size();
	}

	/**
	 * 根据查询条件获取用户的竞价记录
	 * 
	 * @param roleIdStr
	 *            用户ID
	 * @param startIndex
	 *            开始索引
	 * @param count
	 *            返回的总数
	 * @param typeInt
	 *            类型
	 * @param key
	 *            关键字匹配
	 * @param qualityInt
	 *            品质
	 * @param direction
	 *            排序的方向;0,按照上架时间正续;1,倒序
	 * */
	public ItemsAndTotal<XsgAuctionHouseRecord> getAuctionRecords(String roleIdStr, int startIndex, int count,
			int typeInt, String key, int qualityInt, int direction) {
		ItemsAndTotal<XsgAuctionHouseRecord> itemsAndTotal = new ItemsAndTotal<XsgAuctionHouseRecord>();
		Attribute<String> roleId = auctionRecordsCache.getSearchAttribute(CACHE_NAME_AUCTIOH_HOUSE_ATTR_ROLEID);
		Attribute<Long> endTime = auctionRecordsCache.getSearchAttribute(CACHE_NAME_AUCTION_HOUSE_ATTR_ENDTIME);
		// 构造查询条件
		Criteria criteria = createCriteriaBy(null, typeInt, key, qualityInt);
		criteria = appendCriteria(criteria, roleId.eq(roleIdStr));
		// 按照竞拍时间倒序, 最后竞拍的排在前面
		Query query = auctionRecordsCache.createQuery().includeValues()
				.addOrderBy(endTime, (direction == 0 ? Direction.DESCENDING : Direction.ASCENDING));
		if (criteria != null) {
			query.addCriteria(criteria);
		}
		List<Result> resList = query.execute().all();
		if (resList != null && resList.size() > 0) {
			itemsAndTotal.totalCount = resList.size();
			itemsAndTotal.items = getSublistOf(resList, startIndex, count, XsgAuctionHouseRecord.class);
		}
		return itemsAndTotal;
	}

	/**
	 * 增加一个竞拍记录,通过hibernate的级联保存操作持久化到数据库
	 * 
	 * @param item
	 *            竞拍项目
	 * @param record
	 *            竞拍记录
	 * */
	public void addAuctionRecord(XsgAuctionHouseItem item, XsgAuctionHouseRecord record) {
		// 设置关联关系
		item.addRecord(record.getRecordDB());
		// 放入缓存
		auctionRecordsCache.put(new Element(record.getId(), record));
		// 持久化到数据库
		item.saveToDBAsync();
	}

	/**
	 * 根据拍卖的ID,获取拍卖项目
	 * */
	public XsgAuctionHouseItem getAuctionItem(String id) {
		Element element = auctionCache.get(id);
		if (element != null) {
			return (XsgAuctionHouseItem) element.getObjectValue();
		}
		return null;
	}

	public AuctionBaseConfigT getBaseConfigT() {
		return auctionBaseConfig;
	}

	public int getTotalAuctionCount() {
		return auctionCache.getKeysWithExpiryCheck().size();
	}

	/**
	 * 保存拍卖项目, 会有数据库操作
	 * */
	public void saveAuctionHouseItem(AuctionHouseItem item) {
		mAuctionHouseDao.save(item);
	}

	public void addAuctionItem(XsgAuctionHouseItem item) {
		auctionCache.put(new Element(item.getId(), item));
		item.saveToDBAsync();
		startNextAuction();
	}

	public AuctionHouseController createAuctionHouseController(IRole r, Role db) {
		return new AuctionHouseController(r, db);
	}

	/**
	 * 根据查询条件获取某个用户拍卖的物品
	 * 
	 * @param roleId
	 *            用户ID
	 * @param startIndex
	 *            开始索引
	 * @param count
	 *            返回的总数
	 * @param typeInt
	 *            类型
	 * @param key
	 *            关键字匹配
	 * @param qualityInt
	 *            品质
	 * @param direction
	 *            排序的方向;0,按照上架时间正续;1,倒序
	 * */
	public ItemsAndTotal<XsgAuctionHouseItem> getRoleAuctionItems(String roleId, int startIndex, int count,
			int typeInt, String key, int qualityInt, int direction) {
		ItemsAndTotal<XsgAuctionHouseItem> itemsAndTotal = new ItemsAndTotal<XsgAuctionHouseItem>();
		Attribute<Long> endTime = auctionCache.getSearchAttribute(CACHE_NAME_AUCTION_HOUSE_ATTR_ENDTIME);
		// 构造查询条件
		Criteria criteria = createCriteriaBy(roleId, typeInt, key, qualityInt);
		// 查询结果根据结束时间倒序, 最后拍卖的排在前面
		Query query = auctionCache.createQuery().includeValues()
				.addOrderBy(endTime, (direction == 0 ? Direction.DESCENDING : Direction.ASCENDING));
		if (criteria != null) {
			query.addCriteria(criteria);
		}
		List<Result> resList = query.execute().all();
		if (resList != null && resList.size() > 0) {
			itemsAndTotal.totalCount = resList.size();
			itemsAndTotal.items = getSublistOf(resList, startIndex, count, XsgAuctionHouseItem.class);
		}
		return itemsAndTotal;
	}

	private <T> List<T> getSublistOf(List<Result> list, int startIndex, int count, Class<T> clazz) {
		if (list != null && startIndex < list.size()) {
			int endIndex = Math.min(startIndex + count, list.size());
			List<Result> subList = list.subList(startIndex, endIndex);
			List<T> res = XsgCacheManager.parseCacheValue(subList, clazz);
			return res;
		}
		return null;
	}

	/** 获取某个用户的拍卖的总数 */
	public int getRoleAuctionItemCount(String roleId) {
		Attribute<String> sellerId = auctionCache.getSearchAttribute(CACHE_NAME_AUCTION_HOUSE_ATTR_SELLER);
		Query query = auctionCache.createQuery().includeValues().addCriteria(sellerId.eq(roleId));
		List<Result> resList = query.execute().all();
		return resList == null ? 0 : resList.size();
	}

	/**
	 * 获取结合中的ID的拍卖项目
	 * 
	 * @param ids
	 *            id的集合
	 * */
	public List<XsgAuctionHouseItem> getAuctionItems(List<String> ids) {
		Attribute<String> auctionId = auctionCache.getSearchAttribute(CACHE_NAME_AUCTION_HOUSE_ATTR_AUCTION_ID);
		Query query = auctionCache.createQuery().includeValues().addCriteria(auctionId.in(ids));
		List<Result> resList = query.execute().all();
		if (resList != null) {
			List<XsgAuctionHouseItem> items = XsgCacheManager.parseCacheValue(resList, XsgAuctionHouseItem.class);
			return items;
		}
		return null;
	}

	/**
	 * 获取即将结束的下一个拍卖品
	 * */
	private XsgAuctionHouseItem getNextAuction() {
		Attribute<Long> endTime = auctionCache.getSearchAttribute(CACHE_NAME_AUCTION_HOUSE_ATTR_ENDTIME);
		// 根据结束时间排序, 获取最先结束的一个
		Query query = auctionCache.createQuery().includeValues().addOrderBy(endTime, Direction.ASCENDING).maxResults(1);
		List<Result> resList = query.execute().all();
		if (resList != null && resList.size() > 0) {
			List<XsgAuctionHouseItem> res = XsgCacheManager.parseCacheValue(resList, XsgAuctionHouseItem.class);
			return res.get(0);
		}
		return null;
	}

	/**
	 * 开始下一个拍卖, 如果当前的拍卖还没结束, 则什么都不做, 否则选取即将结束的一个开始计时任务
	 * */
	public void startNextAuction() {
		XsgAuctionHouseItem item = getNextAuction();
		if (item == null || auctionHouseDelayedTask != null) {
			return;
		}
		// 构造延时任务
		auctionHouseDelayedTask = new AuctionHouseDelayedTask(item.getId(), item.getEndTime()
				- System.currentTimeMillis());
		LogicThread.scheduleTask(auctionHouseDelayedTask);
	}

	/** 停止一个拍卖 */
	public void stopAuction(XsgAuctionHouseItem item) {
		// 结束的项目是正在等待结束的项目, 则做一些清理工作, 停止计时器
		String auctionId = item.getId();
		if (auctionHouseDelayedTask != null && auctionId.equals(auctionHouseDelayedTask.getAuctionId())) {
			// 结束正在等待的计时器
			closeAuctionWithoutSettle(item);
			// 启动下一个计时器
			startNextAuction();
			return;
		}
		// 结束的项目不是正在等待结束的项目, 直接结束
		closeAndRemoveAuction(item);
	}

	/** 返回缴税之后的收益 */
	public long getMoneyAfterRate(long price) {
		AuctionBaseConfigT config = getBaseConfigT();
		return (price * (100 - config.handingCharge)) / 100;
	}

	/** 结束并结算 */
	public void closeAuctionWithSettle(String auctionId) {
		// 拍卖时间到, 进行结算
		XsgAuctionHouseItem item = getAuctionItem(auctionId);
		// 项目正在进行中
		if (item != null) {
			String templateId = item.getTemplateId();
			int num = item.getNum();
			String bidderId = item.getBidderId();
			String itemName = item.getName();
			String sellerId = item.getSellerId();
			if (!TextUtil.isBlank(bidderId)) {
				// 发送拍卖所得拍卖币给拍卖者, 并扣除佣金
				long price = getMoneyAfterRate(item.getCurrentPrice());
				// 发送拍卖币邮件
				mailSellSuccess(sellerId, item.getCurrentPrice(), price, itemName, item.getBidderName());
				// 发送日志
				XsgAuctionHosueLog.logSellSuccess(auctionId, sellerId, bidderId, price, templateId, num);
				// 记录税收日志
				XsgAuctionHosueLog.logRevenue(auctionId, sellerId, bidderId, num, templateId, item.getCurrentPrice()
						- price);
				// 发送物品邮件给获得者
				mailNormalBuySuccess(bidderId, itemName, templateId, num, item.getRoleItem());
				// 发送日志
				XsgAuctionHosueLog.logNormalBuySuccess(auctionId, bidderId, sellerId, templateId, num, price);
				final String buy = bidderId;
				final String sell = sellerId;
				// 加再不在线玩家
				XsgRoleManager.getInstance().loadRoleByIdAsync(buy, new Runnable() {
					@Override
					public void run() {
						IRole temp = XsgRoleManager.getInstance().findRoleById(buy);
						if (null != temp) {
							// 买的人
							temp.getAchieveControler().updateAchieveProgress(AchieveTemplate.AuctionSuccessNums.name(),
									1 + "");
						}
					}
				}, new Runnable() {
					@Override
					public void run() {// 失败什么都不做
					}
				});
				XsgRoleManager.getInstance().loadRoleByIdAsync(sell, new Runnable() {
					@Override
					public void run() {
						IRole temp = XsgRoleManager.getInstance().findRoleById(sell);
						if (null != temp) {
							temp.getAchieveControler()
									.updateAchieveProgress(AchieveTemplate.AuctionNums.name(), 1 + "");
						}
					}
				}, new Runnable() {
					@Override
					public void run() {// 失败什么都不做
					}
				});
			} else {
				// 发送退回邮件
				mailNoOneBuy(sellerId, itemName, templateId, num, item.getRoleItem());
				// 发送日志
				XsgAuctionHosueLog.logNoOneBuy(auctionId, sellerId, templateId, num, item.getBasePrice());
				final String sell = sellerId;
				// 加再不在线玩家
				XsgRoleManager.getInstance().loadRoleByIdAsync(sell, new Runnable() {
					@Override
					public void run() {
						IRole temp = XsgRoleManager.getInstance().findRoleById(sell);
						if (null != temp) {
							temp.getAchieveControler()
									.updateAchieveProgress(AchieveTemplate.AuctionNums.name(), 1 + "");
						}
					}
				}, new Runnable() {
					@Override
					public void run() {// 失败什么都不做
					}
				});
			}
			closeAndRemoveAuction(item);
		} else {
			logger.error("auction close normally." + auctionId);
		}
		auctionHouseDelayedTask = null;
	}

	/** 结束不结算 */
	private void closeAuctionWithoutSettle(XsgAuctionHouseItem item) {
		if (auctionHouseDelayedTask != null && item.getId().equals(auctionHouseDelayedTask.getAuctionId())) {
			auctionHouseDelayedTask.cancel();
			closeAndRemoveAuction(item);
			auctionHouseDelayedTask = null;
		}
	}

	/**
	 * 结束所有的拍卖品, 用于合服
	 * 
	 * 拍卖币退回竞拍者，物品退回给拍卖者
	 * */
	public void finishAllAuction() {
		// 停止定时任务
		if (auctionHouseDelayedTask != null) {
			auctionHouseDelayedTask.cancel();
			auctionHouseDelayedTask = null;
		}
		List<String> keys = auctionCache.getKeysWithExpiryCheck();
		if (keys != null) {
			for (String key : keys) {
				XsgAuctionHouseItem item = getAuctionItem(key);
				if (item != null) {
					String bidderId = item.getBidderId();
					// 拍卖币退给竞拍者
					if (!TextUtil.isNotBlank(bidderId)) {
						// 退回拍卖币
						// 构造参数替换映射
						Map<String, Integer> rewardMap = new HashMap<String, Integer>();
						rewardMap.put(Const.PropertyName.AUCTION_COIN, (int) item.getCurrentPrice());
						Map<String, String> replaceMap = new HashMap<String, String>();
						replaceMap.put("$c", item.getName());
						// 发送邮件
						XsgMailManager.getInstance().sendTemplate(bidderId, MailTemplate.SendBackAcutionIcon,
								rewardMap, replaceMap);
						// 发送日志
						XsgAuctionHosueLog.logBuyFailure(item.getId(), bidderId, "system",
								(int) item.getCurrentPrice(), item.getNum(), item.getTemplateId());
					}
					// 物品退回给拍卖者
					// 发送退回邮件
					mailSendAuctionItem(item.getSellerId(), item.getName(), item.getTemplateId(), item.getNum(),
							MailTemplate.SendBackAuctionItem, item.getRoleItem());
					// 发送日志
					XsgAuctionHosueLog.logNoOneBuy(item.getId(), item.getSellerId(), item.getTemplateId(),
							item.getNum(), item.getBasePrice());

					// 结束拍卖品
					closeAndRemoveAuction(item);
					logger.warn(TextUtil.format("finish auction {0}:{1}", item.getSellerId(), item.getName()));
				}
			}
		}
		logger.warn("auction all stopped ...");
	}

	/** 关闭一个拍卖, 移除相关的所有东西 */
	private void closeAndRemoveAuction(final XsgAuctionHouseItem item) {
		auctionCache.remove(item.getId());
		// 移除叫价记录
		List<String> keys = auctionRecordsCache.getKeysWithExpiryCheck();
		if (keys != null) {
			for (String key : keys) {
				XsgAuctionHouseRecord record = (XsgAuctionHouseRecord) auctionRecordsCache.get(key).getObjectValue();
				if (record.getAuctionId().equals(item.getId())) {
					auctionRecordsCache.remove(key);
				}
			}
		}
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				// 删除上架记录
				mAuctionHouseDao.delete(item.getDBItem());
			}
		});
	}

	/** 一口价买下 */
	public static void mailFixedBuySuccess(String acceptId, String itemName, String templateId, int num, RoleItem item) {
		mailSendAuctionItem(acceptId, itemName, templateId, num, MailTemplate.AuctionBuyByFixedPrice, item);
	}

	/** 竞拍成功 */
	public static void mailNormalBuySuccess(String acceptId, String itemName, String templateId, int num, RoleItem item) {
		mailSendAuctionItem(acceptId, itemName, templateId, num, MailTemplate.AuctionBuySuccess, item);
	}

	/** 竞拍失败, 退回拍卖币 */
	public static void mailBuyFailure(String acceptId, long money, String itemName, String bidderName, boolean selfBuy) {
		mailSendMoney(acceptId, money, itemName, bidderName, (selfBuy ? MailTemplate.AuctionBuySelf
				: MailTemplate.AuctionBuyFail));
	}

	/** 竞拍成功, 获得拍卖币 */
	public static void mailSellSuccess(String acceptId, long origPrice, long money, String itemName, String bidderName) {
		// mailSendMoney(acceptId, money, itemName, bidderName,
		// MailTemplate.AuctionSaleSuccess);
		mailSendMoneyWithPrice(acceptId, origPrice, money, itemName, bidderName, MailTemplate.AuctionSaleSuccess);
	}

	/** 物品下架 */
	public static void mailCancelAuction(String acceptId, String itemName, String templateId, int num, RoleItem item) {
		mailSendAuctionItem(acceptId, itemName, templateId, num, MailTemplate.AuctionItemDown, item);
	}

	/** 拍卖结束, 无人叫价 */
	public static void mailNoOneBuy(String acceptId, String itemName, String templateId, int num, RoleItem item) {
		mailSendAuctionItem(acceptId, itemName, templateId, num, MailTemplate.AuctionTimeout, item);
	}

	private static String serializeAttachments(Map<String, Integer> map) {
		return XsgMailManager.getInstance().serializeMailAttach(map);
	}

	private static String serializeAttachments(RoleItem roleItem) {
		InstanceAttachObject[] objs = new InstanceAttachObject[] { new InstanceAttachObject(roleItem) };
		return XsgMailManager.getInstance().serializeMailAttach(objs);
	}

	/**
	 * 发送邮件
	 * */
	private static void mailSendAuctionItem(String acceptId, String itemName, String templateId, int num,
			MailTemplate mailTemplateId, RoleItem item) {
		MailRewardT rewardT = XsgMailManager.getInstance().getMailRewardTList().get(mailTemplateId.value());
		if (rewardT != null) {
			String attachment = null;
			ItemType type = ItemType.DefaultItemType;
			// 如果实例不为空, 则发送实例附件
			if (item != null) {
				type = XsgItemManager.getInstance().getItemType(item.getTemplateCode());
			}
			// 普通物品发模版附件邮件, 非普通的发实例附件邮件
			if (type.equals(ItemType.DefaultItemType)) {
				Map<String, Integer> rewardMap = new HashMap<String, Integer>();
				rewardMap.put(templateId, num);
				attachment = serializeAttachments(rewardMap);
			} else {
				attachment = serializeAttachments(item);
			}
			// 替换模版内容
			String mailContent = rewardT.body.replace("$c", itemName);
			// 构造邮件
			Mail mail = new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "", rewardT.sendName,
					acceptId, rewardT.title, mailContent, attachment, new Date());
			XsgMailManager.getInstance().sendMail(mail);
		}
	}

	/** 发送普通模版邮件 */
	private static void mailSendMoney(String acceptId, long money, String itemName, String bidderName,
			MailTemplate mailTemplateId) {
		Map<String, Integer> rewardMap = new HashMap<String, Integer>();
		Map<String, String> replaceMap = new HashMap<String, String>();
		// 构造参数替换映射
		rewardMap.put(Const.PropertyName.AUCTION_COIN, (int) money);
		replaceMap.put("$c", itemName);
		replaceMap.put("$d", bidderName);
		// 发送邮件
		XsgMailManager.getInstance().sendTemplate(acceptId, mailTemplateId, rewardMap, replaceMap);
	}

	/** 发送普通模版邮件, 附带税前价格 */
	private static void mailSendMoneyWithPrice(String acceptId, long origPrice, long money, String itemName,
			String bidderName, MailTemplate mailTemplateId) {
		Map<String, Integer> rewardMap = new HashMap<String, Integer>();
		Map<String, String> replaceMap = new HashMap<String, String>();
		// 构造参数替换映射
		rewardMap.put(Const.PropertyName.AUCTION_COIN, (int) money);
		replaceMap.put("$c", itemName);
		replaceMap.put("$d", bidderName);
		replaceMap.put("$q", origPrice + "");
		// 发送邮件
		XsgMailManager.getInstance().sendTemplate(acceptId, mailTemplateId, rewardMap, replaceMap);
	}

	/**
	 * 保持拍卖状态,更新持续时间等
	 * 
	 * 服务器停止的时候调用
	 * */
	public void saveAuctionHouseStatus() {
		// update pause time
		if (auctionCache != null) {
			List<String> keys = auctionCache.getKeysWithExpiryCheck();
			if (keys != null) {
				logger.info("Auction House item size: " + keys.size());
				for (String key : keys) {
					XsgAuctionHouseItem item = (XsgAuctionHouseItem) auctionCache.get(key).getObjectValue();
					item.setPauseTime(Calendar.getInstance().getTime());
					item.saveToDBAsync();
				}
			} else {
				logger.info("Auction House has no item.");
			}
		}
	}

	public static class ItemsAndTotal<T> {
		public List<T> items = null;
		public int totalCount = 0;
	}

	/**
	 * 获取固定刷新商品配置
	 * 
	 * @return
	 */
	public Map<Integer, AuctionShopRewardT> getFixedShops() {
		return fixedShops;
	}

	/**
	 * 获取随机刷新商品配置
	 * 
	 * @return
	 */
	public Map<Integer, AuctionShopRewardT> getRandomShops() {
		return randomShops;
	}

	/**
	 * 获取商品刷新配置
	 * 
	 * @return
	 */
	public Map<Integer, Integer> getRefreshConfigs() {
		return refreshCoins;
	}
}

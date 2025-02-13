package com.morefun.XSanGo.haoqingbao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.cache.XsgCacheManager;
import com.morefun.XSanGo.chat.ChatAdT;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.db.game.HaoqingbaoDao;
import com.morefun.XSanGo.db.game.HaoqingbaoRedPacket;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleHaoqingbao;
import com.morefun.XSanGo.db.game.RoleHaoqingbaoItem;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Direction;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.expression.Criteria;

/**
 * 豪情宝
 * 
 * @author guofeng.qin
 */
public class XsgHaoqingbaoManager {

	private static final Logger log = LoggerFactory
			.getLogger(XsgHaoqingbaoManager.class);
	
	// 1，工会；2，好友；3，全服
	public static final int RedPacketType_Faction = 1;
	public static final int RedPacketType_Friend = 2;
	public static final int RedPacketType_All = 3;

	private static XsgHaoqingbaoManager _instance = new XsgHaoqingbaoManager();

	/** 豪情宝发送红包 */
	public static final String Cache_Haoqingbao_Item = "haoqingbaoItem";
	/** 豪情宝发送的红包具体项目 */
	public static final String Cache_Haoqingbao_RedPacket = "haoqingbaoRedPacket";

	/** 红包ItemId查询KEY */
	public static final String Cache_Attr_Item_Id = "itemId";
	/** 抢红包接收者ID查询KEY */
	public static final String Cache_Attr_Recver_Id = "recverId";
	/** 抢红包开始时间 */
	public static final String Cache_Haoqingbao_StartTime = "startTime";
	/** 是否结束的标记 */
	public static final String Cache_Attr_Is_Finished = "finished";

	private Map<Integer, Integer> MsgType = new HashMap<Integer, Integer>();

	// 玩家排行榜
	public static final int TotalRankSize = 1000; // 排前1000名
	private List<RankItem> sendRankList = new ArrayList<RankItem>(); // 发红包榜单
	private List<RankItem> recvRankList = new ArrayList<RankItem>(); // 抢红包榜单

	private Ehcache itemCache;
	private Ehcache redPacketCache;

	private HaoqingbaoCfgT haoqingbaoCfgT;
	private HaoqingbaoDao mHaoqingbaoDao;

	/** 红包倒计时延迟任务 */
	private RedPacketDelayTask redPacketDelayTask;

	/** 红会红包配置 */
	private Map<Integer, HaoqingbaoFactionT> factionT = new HashMap<Integer, HaoqingbaoFactionT>();
	/** 全服红包配置 */
	private HaoqingbaoAllT allT;
	/** 好友红包配置 */
	private Map<Integer, HaoqingbaoFriendT> friendT = new HashMap<Integer, HaoqingbaoFriendT>();
	/** 索要红包显示条数 */
	private Map<String, Integer> redPacketClaimNum = new HashMap<String, Integer>();
	
	private XsgHaoqingbaoManager() {
		XsgCacheManager manager = XsgCacheManager.getInstance();
		itemCache = manager.getCache(Cache_Haoqingbao_Item);
		redPacketCache = manager.getCache(Cache_Haoqingbao_RedPacket);
		if (itemCache == null || redPacketCache == null) {
			log.error("豪情宝Cache初始化错误");
			return;
		}
		mHaoqingbaoDao = HaoqingbaoDao.getFromApplicationContext(ServerLancher
				.getAc());
		if (mHaoqingbaoDao == null) {
			log.error("HaoqingbaoDao init error.");
			return;
		}

		loadHaoqingbaoScript();
		// 初始化缓存
		initCache();
		// 初始化排行榜
		initRankList();
		// 启动延时任务
		startNextRedPacketItem();

		MsgType.put(RedPacketType_Faction, 6); // 工会
		MsgType.put(RedPacketType_Friend, 7); // 好友
		MsgType.put(RedPacketType_All, 8); // 全服
	}

	private void initCache() {
		List<RoleHaoqingbaoItem> items = mHaoqingbaoDao.getAllHaoqingbaoItems();
		if (items != null && items.size() > 0) {
			itemCache.removeAll();
			for (RoleHaoqingbaoItem item : items) {
				itemCache.put(new Element(item.getId(), item));
			}
		}
		List<HaoqingbaoRedPacket> packets = mHaoqingbaoDao
				.getAllHaoqingbaoRedPackets();
		if (packets != null && packets.size() > 0) {
			redPacketCache.removeAll();
			for (HaoqingbaoRedPacket packet : packets) {
				redPacketCache.put(new Element(packet.getId(), packet));
			}
		}
	}
	
	/** 获取红包索要次数 */
	public int getRedPacketClaimNum(String id) {
		if (redPacketClaimNum.containsKey(id)) {
			return redPacketClaimNum.get(id);
		}
		return 0;
	}
	
	/** 更新红包索要次数 */
	public void addRedPacketClaimNum(String id, int num) {
		Integer oldNum = redPacketClaimNum.get(id);
		if (oldNum == null) {
			oldNum = 0;
		}
		oldNum += num;
		redPacketClaimNum.put(id, oldNum);
	}

	/** 删除无用的记录 */
	public void removeOldItems() {
		int dayCount = haoqingbaoCfgT.recordSaveTime;
		long delDayTime = DateUtil.addDays(Calendar.getInstance().getTime(),
				-dayCount).getTime();
		List<String> keys = itemCache.getKeysWithExpiryCheck();
		if (keys != null) {
			for (String key : keys) {
				RoleHaoqingbaoItem item = (RoleHaoqingbaoItem) itemCache.get(
						key).getObjectValue();
				if (item.getFinished() == 1
						&& (item.getStartTime().getTime() < delDayTime)) { // 已经结束并且过期
					removeItemWithId(item);
				}
			}
		}
	}

	public void saveAll() {
		final List<String> keys = itemCache.getKeysWithExpiryCheck();
		final List<String> redPacketKeys = redPacketCache
				.getKeysWithExpiryCheck();
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				if (keys != null) {
					for (String key : keys) {
						RoleHaoqingbaoItem item = (RoleHaoqingbaoItem) itemCache
								.get(key).getObjectValue();
						mHaoqingbaoDao.save(item);
					}
				}
				if (redPacketKeys != null) {
					for (String key : redPacketKeys) {
						HaoqingbaoRedPacket record = (HaoqingbaoRedPacket) redPacketCache
								.get(key).getObjectValue();
						mHaoqingbaoDao.save(record);
					}
				}
			}
		});
	}

	private void removeItemWithId(final RoleHaoqingbaoItem item) {
		itemCache.remove(item.getId());
		final List<HaoqingbaoRedPacket> records = new ArrayList<HaoqingbaoRedPacket>();
		List<String> keys = redPacketCache.getKeysWithExpiryCheck();
		if (keys != null) {
			for (String key : keys) {
				HaoqingbaoRedPacket record = (HaoqingbaoRedPacket) redPacketCache
						.get(key).getObjectValue();
				if (record.getItemId().equals(item.getId())) {
					records.add(record);
					redPacketCache.remove(key);
				}
			}
		}
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				mHaoqingbaoDao.delete(item);
				for (HaoqingbaoRedPacket packet : records) {
					mHaoqingbaoDao.delete(packet);
				}
			}
		});
		log.warn(TextUtil.format("remove HaoqingbaoItem {0}", item.getId()));
	}

	/**
	 * 初始化排行榜
	 * */
	private void initRankList() {
		// 发红包排行
		List<RoleHaoqingbao> rhList = mHaoqingbaoDao.getSendRedPacketRank(0,
				TotalRankSize);
		if (rhList != null && rhList.size() > 0) {
			sendRankList.clear();
			int index = 1;
			for (RoleHaoqingbao rh : rhList) {
				RankItem ri = new RankItem(rh.getRoleId(),
						rh.getTotalSendSum(), index++);
				sendRankList.add(ri);
			}
		}
		// 抢红包排行
		rhList = mHaoqingbaoDao.getSendRedPacketRank(1, TotalRankSize);
		if (rhList != null && rhList.size() > 0) {
			recvRankList.clear();
			int index = 1;
			for (RoleHaoqingbao rh : rhList) {
				RankItem ri = new RankItem(rh.getRoleId(),
						rh.getTotalRecvNum(), index++);
				recvRankList.add(ri);
			}
		}
	}

	/**
	 * 红包是否存在
	 * */
	public boolean isRedPacketItemExist(String id) {
		return itemCache.get(id) != null;
	}
	
	public void loadHaoqingbaoScript() {
		List<HaoqingbaoCfgT> list = ExcelParser.parse(HaoqingbaoCfgT.class);
		if (list != null && list.size() > 0) {
			haoqingbaoCfgT = list.get(0);
		}
		if (haoqingbaoCfgT == null) {
			log.error("豪情宝配置参数错误");
		}
		List<HaoqingbaoAllT> allList = ExcelParser.parse(HaoqingbaoAllT.class);
		if (allList != null && allList.size() > 0) {
			allT = allList.get(0);
		}
		List<HaoqingbaoFactionT> factionList = ExcelParser
				.parse(HaoqingbaoFactionT.class);
		if (factionList != null && factionList.size() > 0) {
			for (HaoqingbaoFactionT t : factionList) {
				factionT.put(t.range, t);
			}
		}
		List<HaoqingbaoFriendT> friendList = ExcelParser
				.parse(HaoqingbaoFriendT.class);
		if (friendList != null && friendList.size() > 0) {
			for (HaoqingbaoFriendT t : friendList) {
				friendT.put(t.range, t);
			}
		}
	}

	public HaoqingbaoCfgT getHaoqingbaoCfgT() {
		return haoqingbaoCfgT;
	}

	public HaoqingbaoAllT getHaoqingbaoAllT() {
		return allT;
	}

	public HaoqingbaoFactionT getHaoqingbaoFactionT(int range) {
		return factionT.get(range);
	}

	public HaoqingbaoFriendT getHaoqingbaoFriendT(int range) {
		return friendT.get(range);
	}

	public boolean updateHaoqingbaoItem(RoleHaoqingbaoItem item) {
		if (item == null) {
			return false;
		}
		// 缓存
		itemCache.put(new Element(item.getId(), item));
		addHaoqingbaoItem2DB(item, null);
		return true;
	}

	public boolean updateHaoqingbaoItem(RoleHaoqingbaoItem item,
			List<HaoqingbaoRedPacket> packets) {
		if (item == null || packets == null || packets.size() <= 0) {
			return false;
		}
		// 缓存
		itemCache.put(new Element(item.getId(), item));
		for (HaoqingbaoRedPacket packet : packets) {
			redPacketCache.put(new Element(packet.getId(), packet));
		}
		// 更新DB
		addHaoqingbaoItem2DB(item, packets);
		return true;
	}

	public boolean addHaoqingbaoItem(RoleHaoqingbaoItem item,
			List<HaoqingbaoRedPacket> packets) {
		if (item == null || packets == null || packets.size() <= 0) {
			return false;
		}
		// 先加入缓存
		itemCache.put(new Element(item.getId(), item));
		for (HaoqingbaoRedPacket packet : packets) {
			redPacketCache.put(new Element(packet.getId(), packet));
		}
		// 加入DB
		addHaoqingbaoItem2DB(item, packets);

		return true;
	}

	private void addHaoqingbaoItem2DB(final RoleHaoqingbaoItem finalItem,
			final List<HaoqingbaoRedPacket> finalPackets) {
		// 加入DB
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				mHaoqingbaoDao.save(finalItem);
				if (finalPackets != null) {
					for (HaoqingbaoRedPacket packet : finalPackets) {
						mHaoqingbaoDao.save(packet);
					}
				}
			}
		});
	}

	public void removeHaoqingbaoItem(String itemId) {
		itemCache.remove(itemId);
		// 移除记录
		List<String> keys = redPacketCache.getKeysWithExpiryCheck();
		if (keys != null) {
			for (String key : keys) {
				HaoqingbaoRedPacket packet = (HaoqingbaoRedPacket) redPacketCache
						.get(key).getObjectValue();
				if (packet.getItemId().equals(itemId)) {
					redPacketCache.remove(key);
				}
			}
		}
	}

	/**
	 * 根据ID获取RoleHaoqingbaoItem
	 * */
	public RoleHaoqingbaoItem getRoleHaoqingbaoItem(String id) {
		Element ele = itemCache.get(id);
		if (ele != null) {
			return (RoleHaoqingbaoItem) ele.getObjectValue();
		}
		return null;
	}

	/**
	 * 获取下一个结束的红包
	 * */
	public RoleHaoqingbaoItem getNextFinishedItem() {
		Attribute<Date> startTime = itemCache
				.getSearchAttribute(Cache_Haoqingbao_StartTime);
		Attribute<Integer> finished = itemCache
				.getSearchAttribute(Cache_Attr_Is_Finished);
		// 根据开始时间，获取最早的一个
		Query query = itemCache.createQuery().includeValues()
				.addCriteria(finished.eq(0))
				.addOrderBy(startTime, Direction.ASCENDING).maxResults(1);
		List<Result> resList = query.execute().all();
		if (resList != null && resList.size() > 0) {
			List<RoleHaoqingbaoItem> result = XsgCacheManager.parseCacheValue(
					resList, RoleHaoqingbaoItem.class);
			return result.get(0);
		}
		return null;
	}

	/**
	 * 开始下一个红包倒计时
	 * */
	public void startNextRedPacketItem() {
		// 倒计时任务已经开始
		if (redPacketDelayTask != null) {
			return;
		}
		RoleHaoqingbaoItem item = getNextFinishedItem();
		if (item == null) { // 没有红包任务
			return;
		}
		HaoqingbaoCfgT cfg = getHaoqingbaoCfgT();
		long timeLimit = cfg.timeLimit * 60 * 1000L;
		long startTime = item.getStartTime().getTime();
		long current = System.currentTimeMillis();
		long delayed = (timeLimit - (current - startTime));
		redPacketDelayTask = new RedPacketDelayTask(item.getId(), delayed);
		// 启动延时任务
		LogicThread.scheduleTask(redPacketDelayTask);
	}

	private void stopDelayTask() {
		// 清除延迟任务以便于开始下一轮任务
		redPacketDelayTask = null;
		// 开启下一次的延迟任务
		startNextRedPacketItem();
	}

	/**
	 * 设置运气王
	 * */
	public void setupLuckyStar(final RoleHaoqingbaoItem item) {
		// 计算运气王
		final List<HaoqingbaoRedPacket> records = getHaoqingbaoRedPackets(
				item.getId(), true, -1);
		// 可能运气王有多个
		final Map<String, HaoqingbaoRedPacket> luckyStarIds = new HashMap<String, HaoqingbaoRedPacket>();
		Map<String, HaoqingbaoRedPacket> oldLuckStarIds = new HashMap<String, HaoqingbaoRedPacket>();
		int maxNum = 0;
		if (records != null && records.size() > 0) {
			for (HaoqingbaoRedPacket record : records) {
				if (record.getLuckyStar() == 1) { // 老的运气王
					oldLuckStarIds.put(record.getReceiverId(), record);
				}
				if (maxNum <= record.getNum()) {
					if (maxNum == record.getNum()) {
						luckyStarIds.put(record.getReceiverId(), record);
					} else {
						maxNum = record.getNum();
						luckyStarIds.clear();
						luckyStarIds.put(record.getReceiverId(), record);
					}
				}
			}
		}
		final Map<String, HaoqingbaoRedPacket> clearLuckyStarList = new HashMap<String, HaoqingbaoRedPacket>();
		for (Map.Entry<String, HaoqingbaoRedPacket> entry : oldLuckStarIds
				.entrySet()) {
			if (luckyStarIds.containsKey(entry.getKey())) { // 已经存在的不用重复设置
				luckyStarIds.remove(entry.getKey());
			} else { // 不存在的要清除运气王状态
				clearLuckyStarList.put(entry.getKey(), entry.getValue());
			}
		}
		final List<String> loadIdList = new ArrayList<String>();
		loadIdList.addAll(luckyStarIds.keySet());
		loadIdList.addAll(clearLuckyStarList.keySet());
		if (luckyStarIds.size() > 0) {
			XsgRoleManager.getInstance().loadRoleAsync(loadIdList,
					new Runnable() {
						@Override
						public void run() {
							XsgRoleManager manager = XsgRoleManager
									.getInstance();
							// 设置运气王
							for (HaoqingbaoRedPacket packet : luckyStarIds
									.values()) {
								IRole target = manager.findRoleById(packet
										.getReceiverId());
								if (target != null) {
									packet.setLuckyStar(1);
									target.getHaoqingbaoController()
											.updateLuckyStar(item.getId(), 1);
								}
							}
							// 清除老的运气王状态
							for (HaoqingbaoRedPacket packet : clearLuckyStarList
									.values()) {
								IRole target = manager.findRoleById(packet
										.getReceiverId());
								if (target != null) {
									packet.setLuckyStar(0);
									target.getHaoqingbaoController()
											.updateLuckyStar(item.getId(), 0);
								}
							}
							updateHaoqingbaoItem(item, records);
						}
					});
		}
	}

	/**
	 * 结束抢红包
	 * */
	public void finishRedPacketItem(final String itemId) {
		final RoleHaoqingbaoItem item = getRoleHaoqingbaoItem(itemId);
		if (item != null && item.getFinished() == 0) {
			List<HaoqingbaoRedPacket> records = getHaoqingbaoRedPackets(itemId, true, -1);
			// 统计一共发出去多少钱
			int total = 0;
			// 运气王用户
			final List<String> luckyStarUserIds = new ArrayList<String>();
			if (records != null) {
				for (HaoqingbaoRedPacket record : records) {
					if (!TextUtil.isBlank(record.getReceiverId())) {
						total += record.getNum();
						if (record.getLuckyStar() == 1) { // 设置运气王
							luckyStarUserIds.add(record.getReceiverId());
						}
					}
				}
			}
			List<String> loadUsers = new ArrayList<String>();
			loadUsers.add(item.getRoleId());
			if (luckyStarUserIds.size() > 0) {
				loadUsers.addAll(luckyStarUserIds);
			}
			final int lastNum = item.getTotalNum() - total;
			XsgRoleManager.getInstance().loadRoleAsync(loadUsers, new Runnable() {
				@Override
				public void run() {
					XsgRoleManager manager = XsgRoleManager.getInstance();
					// 返还玩家没用掉的金额
					IRole sender = manager.findRoleById(item.getRoleId());
					if (lastNum > 0 && sender != null) {
						// 退回余额
						sender.getHaoqingbaoController().acceptCharge(lastNum,
								Messages.getString("HaoqingbaoController.giveBack"));
						sender.getHaoqingbaoController().onGiveBack(item.getId(), lastNum);
						// 发送消息
						sender.getHaoqingbaoController().sendGiveBackMsg(item, lastNum);
					}
					// 设置结束标记
					item.setFinished(1);
					updateHaoqingbaoItem(item);
					stopDelayTask();
					if (luckyStarUserIds.size() > 0) {
						for (String fluckyStarUserId : luckyStarUserIds) {
							IRole luckyStarRole = manager.findRoleById(fluckyStarUserId);
							if (luckyStarRole != null) {
								// 出发豪情宝事件，用于成就统计
								luckyStarRole.getHaoqingbaoController().imluckyStar();
							}
						}
					}
				}
			});
		} else {
			log.error("Haoqingbao get a Finished task...");
			// 设置结束标记
			item.setFinished(1);
			updateHaoqingbaoItem(item);
			stopDelayTask();
		}
	}

	/**
	 * 获取某个红包某个ID的领取记录
	 * */
	public HaoqingbaoRedPacket getHaoqingbaoRedPacket(String itemId,
			String recverId) {
		Attribute<String> attrItemId = redPacketCache
				.getSearchAttribute(Cache_Attr_Item_Id);
		Criteria criteria = attrItemId.eq(itemId);
		Query query = redPacketCache.createQuery().includeValues()
				.addCriteria(criteria);
		List<Result> resultList = query.execute().all();
		if (resultList != null && resultList.size() > 0) {
			List<HaoqingbaoRedPacket> packets = XsgCacheManager
					.parseCacheValue(resultList, HaoqingbaoRedPacket.class);
			if (packets != null && packets.size() > 0) {
				for (HaoqingbaoRedPacket packet : packets) {
					if (recverId.equals(packet.getReceiverId())) {
						return packet;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 获取红包
	 * 
	 * @param limit
	 *            -1表示没有限制
	 * */
	public List<HaoqingbaoRedPacket> getHaoqingbaoRedPackets(String itemId,
			boolean hasReceiver, int limit) {
		Attribute<String> attrItemId = redPacketCache
				.getSearchAttribute(Cache_Attr_Item_Id);
		Criteria criteria = attrItemId.eq(itemId);
		// Attribute<String> attrRecverId = redPacketCache
		// .getSearchAttribute(Cache_Attr_Recver_Id);
		// if (hasReceiver) {
		// // appendCriteria(criteria, attrRecverId.ne(""));
		// } else {
		// appendCriteria(criteria, attrRecverId.eq(""));
		// }
		Query query = redPacketCache.createQuery().includeValues()
				.addCriteria(criteria);
		// if (limit > 0) {
		// query.maxResults(limit);
		// }
		List<Result> resultList = query.execute().all();
		if (resultList != null && resultList.size() > 0) {
			List<HaoqingbaoRedPacket> packets = XsgCacheManager
					.parseCacheValue(resultList, HaoqingbaoRedPacket.class);
			List<HaoqingbaoRedPacket> result = new ArrayList<HaoqingbaoRedPacket>();
			if (packets != null && packets.size() > 0) {
				for (HaoqingbaoRedPacket packet : packets) {
					if (!hasReceiver
							&& TextUtil.isBlank(packet.getReceiverId())) {
						result.add(packet);
					}
					if (hasReceiver
							&& !TextUtil.isBlank(packet.getReceiverId())) {
						result.add(packet);
					}
				}
			}
			return result;
		}
		return null;
	}

	public HaoqingbaoController createHaoqingbaoController(IRole r, Role db) {
		return new HaoqingbaoController(r, db);
	}

	public static XsgHaoqingbaoManager getInstance() {
		return _instance;
	}

	public String parseRedPacketMessage(IRole role, String packetId,
			int yuanbaoNum, int type) {
		XsgChatManager manager = XsgChatManager.getInstance();
		List<ChatAdT> adTList = manager
				.getAdContentMap(XsgChatManager.AdContentType.BigRedPacket);
		if (adTList != null && adTList.size() > 0) {
			ChatAdT adt = adTList.get(NumberUtil.random(adTList.size()));
			if (adt != null && adt.onOff == 1) {
				String content = manager.replaceRoleContent(adt.content, role);
				Map<String, String> replaceMap = new HashMap<String, String>();
				// type(1,2,3)|roleId|roleName
				String replaceStr = type + "|" + role.getRoleId() + "|"
						+ role.getName();
				replaceMap.put("~red_packet_link~", replaceStr);
				replaceMap.put("~red_packet_gold_num~", yuanbaoNum + "");
				return role.getChatControler().parseAdConent(content,
						replaceMap);
			}
		}
		return null;
	}

	public String parseRedPacketChatMessage(IRole role, String packetId,
			int yuanbaoNum, int type, String color, String msg) {
		XsgChatManager manager = XsgChatManager.getInstance();
		List<ChatAdT> adTList = manager
				.getAdContentMap(XsgChatManager.AdContentType.ChatRedPacket);
		if (adTList != null && adTList.size() > 0) {
			ChatAdT adt = adTList.get(NumberUtil.random(adTList.size()));
			if (adt != null && adt.onOff == 1) {
				String content = manager.replaceRoleContent(adt.content, role);
				Map<String, String> replaceMap = new HashMap<String, String>();
				replaceMap.put("~self_color~", color);
				replaceMap.put("~rp_msg~", msg);
				// pid|role_id|role_name|role_vip|head_img|role_level
				String replaceStr = packetId + "|" + role.getRoleId() + "|"
						+ role.getName() + "|" + role.getVipLevel() + "|"
						+ role.getHeadImage() + "|" + role.getLevel() + "|"
						+ type;
				replaceMap.put("~red_packet~", replaceStr);
				return role.getChatControler().parseAdConent(content,
						replaceMap);
			}
		}
		return null;
	}
	
	public String parseClaimRedPacketMessage(IRole role) {
		XsgChatManager manager = XsgChatManager.getInstance();
		List<ChatAdT> adTList = manager.getAdContentMap(XsgChatManager.AdContentType.ClaimRedPacket);
		if (adTList != null && adTList.size() > 0) {
			ChatAdT adt = adTList.get(NumberUtil.random(adTList.size()));
			if (adt != null && adt.onOff == 1) {
				String content = manager.replaceRoleContent(adt.content, role);
				return content;
			}
		}
		return null;
	}

	public String parseGiveBackMsg(IRole role, RoleHaoqingbaoItem item,
			int lastNum) {
		HaoqingbaoCfgT cfg = getHaoqingbaoCfgT();
		if (cfg != null) {
			XsgChatManager manager = XsgChatManager.getInstance();
			String content = manager.replaceRoleContent(cfg.giveBackMsg, role);
			Map<String, String> replaceMap = new HashMap<String, String>();
			replaceMap.put("~date~", DateUtil.format(item.getStartTime()));
			String type = Messages
					.getString("HaoqingbaoController.allRedPacket");
			if (item.getType() == 1) {
				type = Messages
						.getString("HaoqingbaoController.factionRedPacket");
			}
			if (item.getType() == 2) {
				type = Messages
						.getString("HaoqingbaoController.friendRedPacket");
			}
			replaceMap.put("~red_packet_type~", type);
			replaceMap.put("~red_packet_num~", lastNum + "");
			return role.getChatControler().parseAdConent(content, replaceMap);
		}
		return null;
	}

	public RankItem getRankItem(List<RankItem> list, String id) {
		int index = 0;
		for (RankItem item : list) {
			index++;
			if (item.id.equals(id)) {
				item.index = index;
				return item;
			}
		}
		return null;
	}

	public void sortRankList(List<RankItem> list) {
		int index = 0;
		for (RankItem item : list) {
			index++;
			item.index = index;
		}
	}

	public RankItem getSendRankItem(String id) {
		return getRankItem(sendRankList, id);
	}

	public void updateRankList(List<RankItem> list, String id, long score) {
		RankItem item = getRankItem(list, id);
		boolean remove = true; // 是否已经在榜上
		if (item == null) { // 没有上榜
			item = new RankItem(id, 0, 0);
			remove = false;
		}
		if (score == item.score) { // 分数相同不需要更新直接返回
			return;
		}
		if (remove) { // 已经在榜上，移除之后重新排名
			list.remove(item);
		}
		item.score = score; // 更新分数
		if (list.size() < TotalRankSize) {
			list.add(item);
		} else {
			RankItem lastItem = list.get(TotalRankSize - 1);
			if (lastItem.score < score) {
				list.remove(lastItem); // 移除最后一名
				list.add(item);
			}
		}
		// 排序
		Collections.sort(list, new Comparator<RankItem>() {
			@Override
			public int compare(RankItem o1, RankItem o2) {
				return Long.valueOf(o2.score).compareTo(o1.score);
			}
		});
		sortRankList(list);
	}

	public void updateSendRankList(String id, long score) {
		updateRankList(sendRankList, id, score);
	}

	public RankItem getRecvRankItem(String id) {
		return getRankItem(recvRankList, id);
	}

	public void updateRecvRankList(String id, long score) {
		updateRankList(recvRankList, id, score);
	}

	public List<RankItem> headSendRank(int size) {
		return sendRankList.subList(0, Math.min(size, sendRankList.size()));
	}

	public List<RankItem> headRecvRank(int size) {
		return recvRankList.subList(0, Math.min(size, recvRankList.size()));
	}

	public static class RankItem {
		public RankItem(String id, long score, int index) {
			this.id = id;
			this.score = score;
			this.index = index;
		}

		public String id;
		public long score;
		public int index;
	}
	
	public int getChatMsgType(int type) {
		return MsgType.get(type);
	}
}

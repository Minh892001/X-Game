package com.morefun.XSanGo.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import com.XSanGo.Protocol.Property;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.db.game.LotteryDao;
import com.morefun.XSanGo.db.game.LotteryScoreInfo;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.LogManager;

/**
 * 大富翁活动
 */
public class XsgLotteryManage {

	private static XsgLotteryManage instance = new XsgLotteryManage();

	public static XsgLotteryManage getInstance() {
		return instance;
	}

	/**
	 * 创建大富温的控制模块
	 * 
	 * @param roleRt
	 * @param roleDB
	 * @return
	 */
	public LotteryControler createLotteryControler(IRole roleRt, Role roleDB) {
		return new LotteryControler(roleRt, roleDB);
	}

	// 起点
	public static final int TYPE_START = 1;
	// 神秘商店
	public static final int TYPE_SHOP = 2;
	// 普通格子
	public static final int TYPE_NORMAL_GRID = 3;

	// 稀有道具
	public static final int SPECIAL_ITEM = 1;
	// 普通道具
	public static final int NORMAL_ITEM = 2;

	// 最大点数
	public static final int MAX_THROW_POINT = 6;

	// 最大格子数
	public static int GRID_COUNT;

	// 起点
	public static final int START_GRID = 1;

	// 金币
	public static final int COST_TYPE_MONEY = 1;

	// 元宝
	public static final int COST_TYPE_YUANBAO = 2;

	// 神秘商店总权重
	private int mysticalShopTPro;
	// 格子配置道具总权重
	private int itemTPro;
	// 格子特殊道具总权重
	private int specialItemTPro;
	// 排行限制
	public static int rankLimit;

	// 摇骰子最大花费
	public static int MAX_THROW_COST;
	// 免费次数
	public static int FREE_TIMES;
	// 巡回奖励最大圈数限制
	public static int CYCLE_AWARD_NUM_LIMIT;
	/**
	 * 角色 大富温 数据库数据集
	 */
	private Map<String, LotteryScoreInfo> rolesLotteryDBinfo = new HashMap<String, LotteryScoreInfo>();

	/** 大富温积分排行榜 */
	private List<LotteryScoreInfo> lotteryScoreRankList = new ArrayList<LotteryScoreInfo>();
	/**
	 * 排行奖励配置
	 */
	private List<LotteryRankAwardT> rankAwardsCfg = new ArrayList<LotteryRankAwardT>();

	/**
	 * 巡回奖励配置
	 */
	private Map<Integer, LotteryTourAwardT> toursAwardsCfg = new HashMap<Integer, LotteryTourAwardT>();

	/**
	 * 摇骰元宝配置
	 */
	private Map<Integer, LotteryTimeCostT> timeCostCfg = new HashMap<Integer, LotteryTimeCostT>();

	/**
	 * 棋盘道具配置
	 */
	private Map<Integer, LotteryGridItemT> lotteryGridItemCfg = new HashMap<Integer, LotteryGridItemT>();

	/**
	 * 棋盘道具权重配置<权重结尾值,vo>
	 */
	private TreeMap<Integer, LotteryGridItemT> lotteryGridItemProCfg = new TreeMap<Integer, LotteryGridItemT>();

	/**
	 * 棋盘特殊道具权重配置<权重结尾值,vo>
	 */
	private TreeMap<Integer, LotteryGridItemT> lotteryGridSpecialItemProCfg = new TreeMap<Integer, LotteryGridItemT>();

	/**
	 * 神秘商店配置
	 */
	private Map<Integer, LotteryMysticalShopT> lotteryMysticalShopCfg = new HashMap<Integer, LotteryMysticalShopT>();

	/**
	 * 神秘商店权重配置<权重结尾值,vo>
	 */
	private TreeMap<Integer, LotteryMysticalShopT> lotteryMysticalShopProCfg = new TreeMap<Integer, LotteryMysticalShopT>();

	/**
	 * 棋盘方格配置
	 */
	private TreeMap<Integer, LotteryGridT> lotteryGridCfg = new TreeMap<Integer, LotteryGridT>();

	/**
	 * 棋盘方格类型配置<类型,<id,vo>>
	 */
	private Map<Integer, TreeMap<Integer, LotteryGridT>> type4GridCfg = new HashMap<Integer, TreeMap<Integer, LotteryGridT>>();

	private LotteryCommParaT lotteryCommParaT;
	/**
	 * 神秘商店预览
	 */
	private Property[] shopPreview;

	private XsgLotteryManage() {
		loadrankAwards();
		loadtoursAwardsCfg();
		loadTimeCostCfg();
		loadLotteryGridCfg();
		loadLotteryGridItemCfg();
		loadLotteryMysticalShopCfg();
		loadCommPara();

		initLotteryInfos();
	}

	/**
	 * 加载大富温
	 */
	private void initLotteryInfos() {
		LotteryDao lotteryDao = LotteryDao.getFromApplicationContext(ServerLancher.getAc());
		List<LotteryScoreInfo> lotteryDaoResultList = (LinkedList<LotteryScoreInfo>) lotteryDao.findAll();
		boolean isNull = false;
		if (lotteryDaoResultList == null || lotteryDaoResultList.size() == 0) {
			isNull = true;
		}
		// 活动不存在 清数据 不再执行
		ActivityT t = XsgActivityManage.getInstance().getActivityT(XsgActivityManage.LOTTERY_ACTIVEID);
		if (t == null) {
			if (isNull) {
				return;
			}
			clearLotteryData(lotteryDaoResultList);
			return;
		}
		boolean isBetween = DateUtil.isBetween(new Date(), DateUtil.parseDate(t.startTime),
				DateUtil.parseDate(t.endTime));
		// 不在活动时间内 且还有数据的 清档
		if (!isBetween) {
			if (!isNull)
				clearLotteryData(lotteryDaoResultList);
		} else {
			// 在活动时间内 且有数据 正常处理
			if (!isNull) {
				for (LotteryScoreInfo dbVo : lotteryDaoResultList) {
					rolesLotteryDBinfo.put(dbVo.getRoleId(), dbVo);
					if (dbVo.getScore() > 0 && lotteryScoreRankList.size() < rankLimit) {
						lotteryScoreRankList.add(dbVo);
					}
				}
			}
		}
		// 发送邮件奖励 清除数据
		sendAward2Player();
		endToClearData();
	}

	private void sendAward2Player() {
		Date endTime = DateUtil.parseDate(lotteryCommParaT.endTime);
		// 清数据剩余时间
		long interval = endTime.getTime() - System.currentTimeMillis();
		LogicThread.scheduleTask(new DelayedTask(interval, 0) {
			@Override
			public void run() {
				int rank = 1;
				for (LotteryScoreInfo info : lotteryScoreRankList) {
					try {
						Map<String, Integer> itemMap = getAward4Rank(rank);
						if (itemMap == null || itemMap.size() == 0)
							continue;
						if (info.getSendMail() == 1) {
							LogManager.warn(rank + "名大富翁重复发奖:" + info.getRoleId());
							continue;
						}
						Map<String, String> replaceMap = new HashMap<String, String>();
						replaceMap.put("$l", String.valueOf(info.getScore()));
						replaceMap.put("$m", String.valueOf(rank));

						// 发送邮件
						XsgMailManager.getInstance().sendTemplate(info.getRoleId(), MailTemplate.LotteryActivity,
								itemMap, replaceMap);
						info.setSendMail(1);
						save2DbAsync(info);
					} catch (Exception e) {
						LogManager.error(e);
					} finally {
						rank += 1;
					}
				}
			}
		});
	}

	/**
	 * 活动结束清理数据
	 */
	private void endToClearData() {
		ActivityT t = XsgActivityManage.getInstance().getActivityT(XsgActivityManage.LOTTERY_ACTIVEID);
		Date endTime = DateUtil.parseDate(t.endTime);
		// 清数据剩余时间
		long interval = endTime.getTime() - System.currentTimeMillis();
		LogicThread.scheduleTask(new DelayedTask(interval, 0) {
			@Override
			public void run() {
				clearLotteryData(rolesLotteryDBinfo.values());
			}
		});
	}

	/**
	 * 是否在活动可操作时间内
	 * 
	 * @return
	 */
	public boolean isCanThrowAndBuy() {
		if (lotteryCommParaT == null)
			return false;
		boolean isBetween = DateUtil.isBetween(new Date(), DateUtil.parseDate(lotteryCommParaT.startTime),
				DateUtil.parseDate(lotteryCommParaT.endTime));
		if (!isBetween) {
			return false;
		}
		return true;
	}

	/**
	 * 获得名次
	 * 
	 * @return
	 */
	public int getRankNum(LotteryScoreInfo roleLottery) {
		List<LotteryScoreInfo> ranks = XsgLotteryManage.getInstance().getLotteryScoreRankList();
		if (ranks == null || ranks.size() == 0)
			return 0;
		if (roleLottery.getScore() < ranks.get(ranks.size() - 1).getScore()) {
			return 0;
		}
		int index = 1;
		for (LotteryScoreInfo info : ranks) {
			if (info.getRoleId().equals(roleLottery.getRoleId())) {
				return index;
			}
			index += 1;
		}
		return 0;
	}

	/**
	 * 清空数据库记录
	 * 
	 * @param scoreRankList
	 *            积分排名列表
	 */
	public void clearLotteryData(final Collection<LotteryScoreInfo> list) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				LotteryDao lotteryDao = LotteryDao.getFromApplicationContext(ServerLancher.getAc());
				lotteryDao.clear(list);
				LogicThread.execute(new Runnable() {
					@Override
					public void run() {
						rolesLotteryDBinfo.clear();
						lotteryScoreRankList.clear();
					}
				});
			}
		});
	}

	/**
	 * 加载常量配置
	 */
	private void loadCommPara() {
		List<LotteryCommParaT> list = ExcelParser.parse(LotteryCommParaT.class);
		lotteryCommParaT = list.get(0);
	}

	/**
	 * 加载排行奖励配置
	 */
	private void loadrankAwards() {
		List<LotteryRankAwardT> list = ExcelParser.parse(LotteryRankAwardT.class);
		rankAwardsCfg.clear();
		for (LotteryRankAwardT t : list) {
			String items[] = t.items.split(",");
			for (int i = 0; i < items.length; i++) {
				String detail[] = items[i].split(":");
				t.itemMap.put(detail[0], Integer.valueOf(detail[1]));
			}
			rankAwardsCfg.add(t);
		}
		rankLimit = list.get(list.size() - 1).stopRank;
	}

	/**
	 * 加载巡回奖励配置
	 */
	private void loadtoursAwardsCfg() {
		List<LotteryTourAwardT> list = ExcelParser.parse(LotteryTourAwardT.class);
		toursAwardsCfg.clear();
		for (LotteryTourAwardT t : list) {
			String detail[] = t.item.split(":");
			t.itemMap.put(detail[0], Integer.valueOf(detail[1]));
			toursAwardsCfg.put(t.num, t);
			CYCLE_AWARD_NUM_LIMIT = CYCLE_AWARD_NUM_LIMIT + 1;
		}
	}

	/**
	 * 摇骰元宝奖励配置
	 */
	private void loadTimeCostCfg() {
		List<LotteryTimeCostT> list = ExcelParser.parse(LotteryTimeCostT.class);
		timeCostCfg.clear();
		for (LotteryTimeCostT t : list) {
			timeCostCfg.put(t.numRoll, t);
			if (t.cost == 0) {
				FREE_TIMES += 1;
			}
			if (t.coinType == XsgLotteryManage.COST_TYPE_YUANBAO) {
				if (MAX_THROW_COST < t.cost) {
					MAX_THROW_COST = t.cost;
				}
			}
		}
	}

	/**
	 * 棋盘方格配置
	 */
	private void loadLotteryGridCfg() {
		List<LotteryGridT> list = ExcelParser.parse(LotteryGridT.class);
		lotteryGridCfg.clear();
		type4GridCfg.clear();
		int maxId = 0;
		for (LotteryGridT t : list) {
			lotteryGridCfg.put(t.id, t);
			TreeMap<Integer, LotteryGridT> map = type4GridCfg.get(t.pointType);
			if (map == null) {
				map = new TreeMap<Integer, LotteryGridT>();
				type4GridCfg.put(t.pointType, map);
			}
			map.put(t.id, t);
			if (maxId < t.id)
				maxId = t.id;
		}
		GRID_COUNT = maxId;
	}

	/**
	 * 棋盘道具列表
	 */
	private void loadLotteryGridItemCfg() {
		lotteryGridItemCfg.clear();
		List<LotteryGridItemT> list = ExcelParser.parse(LotteryGridItemT.class);
		int startRange = 1;
		int startSpecialRange = 1;
		for (LotteryGridItemT t : list) {
			if (startRange == 1) {
				t.startRange = 1;
				t.endRange = t.pro;
			} else {
				t.startRange = startRange;
				t.endRange = t.pro + t.startRange - 1;
			}
			startRange = t.endRange + 1;
			itemTPro += t.pro;
			lotteryGridItemCfg.put(t.id, t);
			lotteryGridItemProCfg.put(t.endRange, t);
			if (t.lotteryType == SPECIAL_ITEM) {
				int endRange = 0;
				specialItemTPro += t.pro;
				if (startSpecialRange == 1) {
					t.startRange = 1;
					endRange = t.pro;
				} else {
					t.startRange = startSpecialRange;
					endRange = t.pro + startSpecialRange - 1;
				}
				startSpecialRange = endRange + 1;
				lotteryGridSpecialItemProCfg.put(endRange, t);
			}
		}
	}

	/**
	 * 神秘商店列表
	 */
	private void loadLotteryMysticalShopCfg() {
		lotteryMysticalShopCfg.clear();
		List<LotteryMysticalShopT> list = ExcelParser.parse(LotteryMysticalShopT.class);

		List<Property> previewList = new ArrayList<Property>();
		int startRange = 1;
		for (LotteryMysticalShopT t : list) {
			if (startRange == 1) {
				t.startRange = 1;
				t.endRange = t.pro;
			} else {
				t.startRange = startRange;
				t.endRange = t.pro + t.startRange - 1;
			}
			startRange = t.endRange + 1;
			mysticalShopTPro += t.pro;
			lotteryMysticalShopCfg.put(t.id, t);
			lotteryMysticalShopProCfg.put(t.endRange, t);

			if (t.preview == 1) {
				previewList.add(new Property(t.item, t.num));
			}
		}

		this.shopPreview = previewList.toArray(new Property[0]);
	}

	/**
	 * 调整积分排行榜排名
	 */
	public void adjustLotteryScoreRank() {
		// 按照总分数和创建时间排行
		Collections.sort(lotteryScoreRankList, new Comparator<LotteryScoreInfo>() {
			@Override
			public int compare(LotteryScoreInfo t1, LotteryScoreInfo t2) {
				if (t1.getScore() > t2.getScore()) {
					return -1;
				} else if (t1.getScore() < t2.getScore()) {
					return 1;
				}
				if (t1.getUpdateTime().before(t2.getUpdateTime())) {
					return -1;
				} else if (t1.getUpdateTime().after(t2.getUpdateTime())) {
					return 1;
				}
				return 0;
			}
		});
		if (lotteryScoreRankList.size() > rankLimit) {
			lotteryScoreRankList = (List<LotteryScoreInfo>) lotteryScoreRankList.subList(0, rankLimit);
		}
	}

	/**
	 * 异步保存到数据库
	 * 
	 * @param lotteryScoreInfo
	 *            积分排名对象
	 */
	public void save2DbAsync(final LotteryScoreInfo lotteryScoreInfo) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				LotteryDao lotteryDao = LotteryDao.getFromApplicationContext(ServerLancher.getAc());
				lotteryDao.save(lotteryScoreInfo);
			}
		});
	}

	/**
	 * 初始化格子需要的道具(不可重复)
	 */
	private List<Integer> loadRandomGridsItems() {
		List<Integer> list = new ArrayList<Integer>();
		Map<Integer, LotteryGridT> normalGridMapcfg = getType4GridCfg().get(XsgLotteryManage.TYPE_NORMAL_GRID);
		LotteryCommParaT lotteryCommParaT = getLotteryCommParaT();
		// 稀有道具随机数量
		int needSpecialItemNum = lotteryCommParaT.lotteryNum;
		// 普通道具随机数量
		int needCommItemNum = normalGridMapcfg.size() - needSpecialItemNum;

		for (int i = 0; i < needSpecialItemNum; i++) {
			list = getUnionIndex(list, SPECIAL_ITEM);
		}

		for (int i = 0; i < needCommItemNum; i++) {
			list = getUnionIndex(list, NORMAL_ITEM);
		}
		Collections.shuffle(list);
		return list;
	}

	/**
	 * 初始化神秘商店需要的道具(不可重复)
	 */
	private List<Integer> loadMShopItems() {
		List<Integer> list = new ArrayList<Integer>();
		LotteryCommParaT lotteryCommParaT = getLotteryCommParaT();
		// 道具随机数量
		int needItemNum = lotteryCommParaT.matrialNum;
		for (int i = 0; i < needItemNum; i++) {
			list = randomMysticalShopItems(list);
		}
		return list;
	}

	/**
	 * 棋盘初始化或者重置
	 * 
	 * @param dbInfo
	 */
	public void initRoleGridItems(LotteryScoreInfo dbInfo) {
		List<Integer> indexs = loadRandomGridsItems();
		StringBuffer buf = new StringBuffer();
		int num = 0;
		for (Integer index : indexs) {
			if (num == 0)
				buf.append(index).append(",");
			else
				buf.append(index).append(",");
			num += 1;
		}
		dbInfo.setGridsInfo(buf.toString());
	}

	/**
	 * 神秘商店初始化或者重置
	 * 
	 * @param dbInfo
	 */
	public void initRoleMShopItems(LotteryScoreInfo dbInfo) {
		List<Integer> indexs = loadMShopItems();
		StringBuffer buf = new StringBuffer();
		for (Integer index : indexs) {
			buf.append(index).append("_").append(0).append(",");
		}
		dbInfo.setShopInfo(buf.toString());
		dbInfo.setShopOpenTime(new Date());
	}

	/**
	 * 随机神秘商店需要的道具(不可重复)
	 */
	private List<Integer> randomMysticalShopItems(List<Integer> list) {
		int index = getRandomMysticalShopItemIndex();
		if (index == 0) {
			return list;
		}
		if (list.contains(index)) {
			list = randomMysticalShopItems(list);
		} else {
			list.add(index);
		}
		return list;
	}

	/**
	 * 获得不重复的 随机道具索引
	 * 
	 * @param list
	 * @param num
	 * @return
	 */
	private List<Integer> getUnionIndex(List<Integer> list, int type) {
		int index = 0;
		if (type == SPECIAL_ITEM) {
			index = getRandomSpecialItemIndex();
		} else {
			index = getRandomItemIndex();
		}
		if (index == 0) {
			return list;
		}
		if (list.contains(index)) {
			list = getUnionIndex(list, type);
		} else {
			list.add(index);
		}
		return list;
	}

	/**
	 * 随机出一个道具
	 * 
	 * @param
	 * @return
	 */
	public Integer getRandomItemIndex() {
		int proCount = itemTPro;
		Random random = new Random();
		int r = random.nextInt(proCount) + 1;
		int pro = lotteryGridItemProCfg.ceilingKey(r);
		return lotteryGridItemProCfg.get(pro).id;
	}

	/**
	 * 随机出一个特殊道具
	 * 
	 * @param type
	 * @return
	 */
	public Integer getRandomSpecialItemIndex() {
		int proCount = specialItemTPro;
		Random random = new Random();
		int r = random.nextInt(proCount) + 1;
		int pro = lotteryGridSpecialItemProCfg.ceilingKey(r);
		return lotteryGridSpecialItemProCfg.get(pro).id;
	}

	/**
	 * 摇骰 获得新点位
	 * 
	 * @param dbInfo
	 * @return 点数,格子位子
	 */
	public int[] getThrowPoint(LotteryScoreInfo dbInfo) {
		int curGrid = dbInfo.getGridId();
		List<Integer> gridIds = new ArrayList<Integer>();
		// 最大不会转圈 直接取前面6格
		if (curGrid <= GRID_COUNT - MAX_THROW_POINT) {
			for (int i = curGrid + 1; i <= curGrid + MAX_THROW_POINT; i++) {
				gridIds.add(i);
			}
		} else {
			// 可能转圈
			int left = GRID_COUNT - curGrid;
			for (int i = curGrid + 1; i <= curGrid + left; i++)
				gridIds.add(i);
			int leftNext = MAX_THROW_POINT - left;
			for (int i = 1; i <= leftNext; i++)
				gridIds.add(i);
		}
		if (gridIds.size() != MAX_THROW_POINT) {
			return null;
		}
		int detail[] = new int[2];
		// 根据这6个格子的 权重进行 随机
		int aimGridId = getNextGrid(gridIds, dbInfo);
		if (aimGridId > curGrid) {
			detail[0] = aimGridId - curGrid;
		} else {
			detail[0] = GRID_COUNT - curGrid + aimGridId;
		}
		detail[1] = aimGridId;
		return detail;
	}

	/**
	 * 获得投掷后的 格子ID
	 * 
	 * @param gridIds
	 * @return
	 */
	private int getNextGrid(List<Integer> gridIds, LotteryScoreInfo dbInfo) {
		int proCount = 0;
		int minPro = 0;// 权重值最小的
		int minProCount = 0;
		TreeMap<Integer, Integer> map = new TreeMap<Integer, Integer>();
		for (Integer gridId : gridIds) {
			LotteryGridT t = lotteryGridCfg.get(gridId);
			if (t.pointType != TYPE_NORMAL_GRID) {
				proCount += t.pro;
			} else {
				// 普通格子的权重要从道具中取
				int itemPro = getProFromItem(gridId, dbInfo).pro;
				proCount += itemPro;
			}
			map.put(proCount, gridId);
			if (minPro == 0) {
				minPro = t.pro;
				minProCount = proCount;
			} else {
				if (minPro > t.pro) {
					minPro = t.pro;
					minProCount = proCount;
				}
			}

		}
		Random random = new Random();
		int r = random.nextInt(proCount) + 1;
		int pro = map.ceilingKey(r);
		// 任性值达标 取权重最小的
		if (dbInfo.getSpecialScore() >= lotteryCommParaT.luckyNum) {
			pro = minProCount;
			dbInfo.setSpecialScore(0);
		}
		return map.get(pro);
	}

	/**
	 * 根据格子ID 从对应道具中获取道具配置
	 * 
	 * @param grid
	 * @return
	 */
	public LotteryGridItemT getProFromItem(int grid, LotteryScoreInfo dbInfo) {
		LotteryGridT t1 = lotteryGridCfg.get(grid);
		if (t1.pointType != TYPE_NORMAL_GRID)
			return null;
		String itemIndex[] = dbInfo.getGridsInfo().split(",");
		TreeMap<Integer, LotteryGridT> map = type4GridCfg.get(TYPE_NORMAL_GRID);
		int index = 0;
		for (LotteryGridT t : map.values()) {
			if (t.id == grid)
				break;
			index += 1;
		}
		int id = Integer.valueOf(itemIndex[index]);
		return lotteryGridItemCfg.get(id);
	}

	/**
	 * 根据当前排名获得奖励
	 * 
	 * @param rank
	 * @return
	 */
	public Map<String, Integer> getAward4Rank(int rank) {
		for (LotteryRankAwardT t : rankAwardsCfg) {
			if (rank >= t.startRank && rank <= t.stopRank) {
				return t.itemMap;
			}
		}
		return null;
	}

	/**
	 * 随机出一个神秘商店道具
	 * 
	 * @param type
	 * @return
	 */
	public Integer getRandomMysticalShopItemIndex() {
		Random random = new Random();
		int r = random.nextInt(mysticalShopTPro) + 1;
		int pro = lotteryMysticalShopProCfg.ceilingKey(r);
		return lotteryMysticalShopProCfg.get(pro).id;
	}

	public int getItemTPro() {
		return itemTPro;
	}

	public Map<String, LotteryScoreInfo> getRolesLotteryDBinfo() {
		return rolesLotteryDBinfo;
	}

	public List<LotteryRankAwardT> getRankAwardsCfg() {
		return rankAwardsCfg;
	}

	public Map<Integer, LotteryTourAwardT> getToursAwardsCfg() {
		return toursAwardsCfg;
	}

	public Map<Integer, LotteryTimeCostT> getTimeCostCfg() {
		return timeCostCfg;
	}

	public Map<Integer, LotteryGridItemT> getLotteryGridItemCfg() {
		return lotteryGridItemCfg;
	}

	public Map<Integer, LotteryMysticalShopT> getLotteryMysticalShopCfg() {
		return lotteryMysticalShopCfg;
	}

	public TreeMap<Integer, LotteryGridT> getLotteryGridCfg() {
		return lotteryGridCfg;
	}

	public LotteryCommParaT getLotteryCommParaT() {
		return lotteryCommParaT;
	}

	public List<LotteryScoreInfo> getLotteryScoreRankList() {
		return lotteryScoreRankList;
	}

	public int getRankLimit() {
		return rankLimit;
	}

	public Map<Integer, TreeMap<Integer, LotteryGridT>> getType4GridCfg() {
		return type4GridCfg;
	}

	public TreeMap<Integer, LotteryGridItemT> getLotteryGridItemProCfg() {
		return lotteryGridItemProCfg;
	}

	public TreeMap<Integer, LotteryGridItemT> getLotteryGridSpecialItemProCfg() {
		return lotteryGridSpecialItemProCfg;
	}

	public int getSpecialItemTPro() {
		return specialItemTPro;
	}

	/**
	 * 获取投掷骰子的最低等级
	 * 
	 * @return
	 */
	public int getMinLevel() {
		return this.lotteryCommParaT.minLevel;
	}

	/**
	 * 获取神秘商店预览
	 * 
	 * @return
	 */
	public Property[] getShopPreview() {
		return this.shopPreview;
	}
}

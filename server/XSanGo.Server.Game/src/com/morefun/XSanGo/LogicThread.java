/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.morefun.XSanGo.ArenaRank.CrossArenaManager;
import com.morefun.XSanGo.ArenaRank.XsgArenaRankManager;
import com.morefun.XSanGo.AttackCastle.XsgAttackCastleManager;
import com.morefun.XSanGo.MFBI.XsgMFBIManager;
import com.morefun.XSanGo.achieve.XsgAchieveManager;
import com.morefun.XSanGo.activity.XsgActivityManage;
import com.morefun.XSanGo.activity.XsgAnnounceManager;
import com.morefun.XSanGo.activity.XsgLotteryManage;
import com.morefun.XSanGo.activity.XsgShareManage;
import com.morefun.XSanGo.auction.XsgAuctionHouseManager;
import com.morefun.XSanGo.buyJinbi.XsgBuyJInbiManager;
import com.morefun.XSanGo.cache.XsgCacheManager;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.collect.XsgCollectHeroSoulManager;
import com.morefun.XSanGo.colorfulEgg.XsgColorfullEggManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.GroovyManager;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.copy.XsgCopyManager;
import com.morefun.XSanGo.crossServer.XsgCrossServerManager;
import com.morefun.XSanGo.crossServer.XsgTournamentBetManager;
import com.morefun.XSanGo.crossServer.XsgTournamentManager;
import com.morefun.XSanGo.db.game.Faction;
import com.morefun.XSanGo.db.game.FactionDAO;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleDAO;
import com.morefun.XSanGo.dreamland.XsgDreamlandManager;
import com.morefun.XSanGo.equip.XsgEquipManager;
import com.morefun.XSanGo.faction.XsgFactionManager;
import com.morefun.XSanGo.faction.factionBattle.XsgFactionBattleManager;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager;
import com.morefun.XSanGo.formation.XsgFormationManager;
import com.morefun.XSanGo.formation.datacollector.XsgFormationDataCollecterManager;
import com.morefun.XSanGo.friendsRecall.XsgFriendsRecallManager;
import com.morefun.XSanGo.haoqingbao.XsgHaoqingbaoManager;
import com.morefun.XSanGo.hero.XsgHeroManager;
import com.morefun.XSanGo.hero.market.HeroMarketManager;
import com.morefun.XSanGo.heroAdmire.XsgHeroAdmireManager;
import com.morefun.XSanGo.heroAwaken.XsgHeroAwakenManager;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.itemChip.XsgItemChipManager;
import com.morefun.XSanGo.ladder.XsgLadderManager;
import com.morefun.XSanGo.luckybag.XsgLuckyBagManager;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.makewine.XsgMakeWineManager;
import com.morefun.XSanGo.onlineAward.XsgOnlineAwardManager;
import com.morefun.XSanGo.partner.XsgPartnerManager;
import com.morefun.XSanGo.pushmsg.XsgPushMsgManager;
import com.morefun.XSanGo.rankList.XsgRankListManager;
import com.morefun.XSanGo.reward.XsgRewardManager;
import com.morefun.XSanGo.robot.XsgRobotManager;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.shop.XsgShopManage;
import com.morefun.XSanGo.sign.XsgSignManager;
import com.morefun.XSanGo.smithyExchange.XsgSmithyManager;
import com.morefun.XSanGo.sns.XsgSnsManager;
import com.morefun.XSanGo.stat.XsgStatManager;
import com.morefun.XSanGo.superCharge.XsgSuperChargeManager;
import com.morefun.XSanGo.task.XsgTaskManager;
import com.morefun.XSanGo.temp.XsgFourthTimeTestManger;
import com.morefun.XSanGo.temp.XsgTemporaryRDActivityManager;
import com.morefun.XSanGo.timeBattle.XsgTimeBattleManage;
import com.morefun.XSanGo.trader.XsgTraderManager;
import com.morefun.XSanGo.treasure.XsgTreasureManage;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.DelayedTaskThread;
import com.morefun.XSanGo.util.TextUtil;
import com.morefun.XSanGo.vip.XsgVipManager;
import com.morefun.XSanGo.worldboss.WorldBossManager;

import net.sf.ehcache.Cache;

public class LogicThread {

	private final static Log logger = LogFactory.getLog(LogicThread.class);

	/** 主逻辑线程 */
	private static ThreadPoolExecutor logicExecutor = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());
	/** 执行延迟任务的辅助线程 */
	private static DelayedTaskThread timer = new DelayedTaskThread(logicExecutor);

	private static boolean dynamicOverload;
	private static boolean staticOverload;

	/** 主逻辑线程编号 */
	protected static long logicThreadId;

	/**
	 * 
	 */
	public static void init() {
		// 初始化主线程标记
		logicExecutor.execute(new Runnable() {

			@Override
			public void run() {
				Thread logicThread = Thread.currentThread();
				logicThread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
					@Override
					public void uncaughtException(Thread t, Throwable e) {
						logger.error("UncaughtException", e);
					}
				});
				logicThreadId = logicThread.getId();
				logger.info("Main Thread ID: " + logicThreadId);
			}
		});

		handleBackupData();
		GroovyManager.getInstance();
		XsgFourthTimeTestManger.getInstance();

		// 初始化辅助线程
		timer.start();
		XsgGameParamManager.getInstance();

		// 主键管理
		GlobalDataManager.getInstance();

		XsgStatManager.getInstance();
		XsgAnnounceManager.getInstance();

		// 初始化全服最高等级
		logger.info("初始化全服最高等级...");
		// XsgActivityManage.getInstance().initMaxLevelInServer();

		logger.info("初始化战力快照信息...");
		XsgFormationDataCollecterManager.getInstance();

		// 初始化角色
		logger.info("初始化角色配置...");
		XsgRoleManager.getInstance();

		logger.info("初始化掉落模块配置...");
		XsgRewardManager.getInstance();
		// 初始化物品
		logger.info("初始化道具配置...");
		XsgItemManager.getInstance();

		if (ServerLancher.getAc().getBean("CheckScriptOnStart", Boolean.class)) {
			XsgRewardManager.getInstance().checkScript();
		}

		// 初始化武将
		logger.info("初始化武将配置...");
		XsgHeroManager.getInstance();
		// 初始化军团
		logger.info("初始化军团配置...");
		XsgFormationManager.getInstance();
		// 副本
		logger.info("初始化关卡配置...");
		XsgCopyManager.getInstance();
		// 邮件
		logger.info("初始化邮件配置...");
		XsgMailManager.getInstance();
		// 装备
		logger.info("初始化装备配置...");
		XsgEquipManager.getInstance();

		// 抽卡
		logger.info("初始化抽卡配置...");
		HeroMarketManager.getInstance();
		// 神秘商人
		logger.info("初始化神秘商人配置...");
		XsgTraderManager.getInstance();

		logger.info("初始化VIP配置...");
		XsgVipManager.getInstance();

		// 点金手
		logger.info("初始化点金手配置...");
		XsgBuyJInbiManager.getInstance();
		// 在线礼包
		logger.info("初始化在线礼包配置...");
		XsgOnlineAwardManager.getInstance();
		// 碎片掠夺
		logger.info("初始化碎片掠夺配置...");
		XsgItemChipManager.getInstance();
		// 任务
		logger.info("初始化任务配置...");
		XsgTaskManager.getInstance();
		// 聊天
		logger.info("初始化聊天配置...");
		XsgChatManager.getInstance();

		// 活动
		logger.info("初始化活动配置...");
		XsgActivityManage.getInstance();
		logger.info("初始化临时运营活动配置...");
		XsgTemporaryRDActivityManager.getInstance();
		// 商城
		logger.info("初始化商城配置...");
		XsgShopManage.getInstance();
		// 时空战役
		logger.info("初始化时空战役配置...");
		XsgTimeBattleManage.getInstance();
		// 公会
		logger.info("初始化工会配置...");
		XsgFactionManager.getInstance();
		// 机器生成数据
		logger.info("初始化机器人配置...");
		XsgRobotManager.getInstance();
		// 竞技场 排行榜
		logger.info("初始化竞技场配置...");
		XsgArenaRankManager.getInstance();
		CrossArenaManager.getInstance();
		logger.info("初始化竞技场排名...");
		XsgArenaRankManager.getInstance().addRobot(XsgRobotManager.getInstance().arenaRobotFormationMap);
		// 排行榜数据
		logger.info("初始化排行榜数据...");
		XsgRankListManager.getInstance();
		// 群雄争霸
		logger.info("初始化群雄争霸...");
		XsgLadderManager.getInstance();
		// 群雄争霸
		logger.info("初始化名将仰慕...");
		XsgHeroAdmireManager.getInstance();

		// 推送消息
		logger.info("初始化推送消息...");
		XsgPushMsgManager.getInstance();

		// 名将召唤
		logger.info("初始化名将召唤配置...");
		XsgCollectHeroSoulManager.getInstance();

		// 排行榜
		logger.info("初始化排行榜数据...");
		XsgRankListManager.getInstance();

		// 群雄争霸系统
		logger.info("初始化群雄争霸系统...");
		XsgLadderManager.getInstance();

		// 北伐
		logger.info("初始化北伐配置...");
		XsgAttackCastleManager.getInstance();

		// 签到
		logger.info("初始化签到...");
		XsgSignManager.getInstance();

		logger.info("初始化拍卖行...");
		XsgAuctionHouseManager.getInstance();
		logger.info("初始化豪情宝...");
		XsgHaoqingbaoManager.getInstance();
		logger.info("初始化福袋...");
		XsgLuckyBagManager.getInstance();
		logger.info("初始化寻宝...");
		XsgTreasureManage.getInstance();
		logger.info("初始化世界Boss...");
		WorldBossManager.getInstance();
		logger.info("初始化老友召回...");
		XsgFriendsRecallManager.getInstance();

		// 武将觉醒系统
		logger.info("初始化武将觉醒...");
		XsgHeroAwakenManager.getInstance();

		
		logger.info("初始化公会战...");
		XsgFactionBattleManager.getInstance();
		
		logger.info("初始化南华幻境...");
		XsgDreamlandManager.getInstance();
		
		// 伙伴系统
		logger.info("成就...");
		XsgAchieveManager.getInstance();
		logger.info("初始化伙伴系统配置...");
		XsgPartnerManager.getInstance();

		logger.info("初始化铁匠铺兑换配置...");
		XsgSmithyManager.getInstance();
		//超级充值、抽奖
		logger.info("初始化超级充值配置...");
		XsgSuperChargeManager.getInstance();
		// 节日彩蛋
		logger.info("初始化彩蛋配置...");
		XsgColorfullEggManager.getInstance();

		// 社交
		logger.info("初始化社交配置...");
		XsgSnsManager.getInstance();

		// 预加载关卡占领者信息
		logger.info("预加载关卡占领者...");
		XsgCopyManager.getInstance().preloadChampions();

		logger.info("启动战报验证线程...");
		XsgFightMovieManager.getInstance().startValidateFightMovie();

		logger.info("初始化大富翁配置...");
		XsgLotteryManage.getInstance();
		
		logger.info("初始化分享活动配置...");
		XsgShareManage.getInstance();
		logger.info("初始化比武大会...");
		XsgCrossServerManager.getInstance();
		XsgTournamentManager.getInstance();
		XsgTournamentBetManager.getInstance();
		logger.info("初始化煮酒论英雄");
		XsgMakeWineManager.getInstance();

		logger.info("分析合成原料...");
		XsgItemChipManager.getInstance().analyzeCompoundMaterial();
		// 发送数据中心
		XsgMFBIManager.getInstance();

		// 初始化全局定时任务，这句放最后
		TimerManager.getInstance();
	}

	/**
	 * 处理上次停服未保存成功的备份数据
	 */
	private static void handleBackupData() {
		Cache cache = XsgCacheManager.getInstance().getCache(Const.ErrorData_Cache_Name);
		logger.info(TextUtil.format("查找到[{0}]个备份数据。", cache.getKeysWithExpiryCheck().size()));
		try {
			for (Object key : cache.getKeysWithExpiryCheck()) {
				Object value = cache.get(key).getObjectValue();
				if (value instanceof Role) {
					Role role = (Role) value;
					handleRoleBackup(role);
				}
			}
			cache.removeAll();
			// 处理公会
			Cache factionCache = XsgCacheManager.getInstance().getCache(Const.FACTION_ERROR_DATA_NAME);
			for (Object key : factionCache.getKeysWithExpiryCheck()) {
				Object value = factionCache.get(key).getObjectValue();
				if (value instanceof Faction) {
					FactionDAO.getFromApplicationContext(ServerLancher.getAc()).customMerge((Faction) value);
				}
			}
			factionCache.removeAll();
		} catch (Exception e) {
			logger.error("Data recover error!!!", e);
		}

	}

	/**
	 * @param role
	 * @throws Exception
	 */
	private static void handleRoleBackup(Role role) throws Exception {
		// 移除竞技场战报ID丢失bug引起的无法存储的坏数据
		XsgArenaRankManager.removeBrokenData(role);

		RoleDAO.getFromApplicationContext(ServerLancher.getAc()).customMerge(role);
		logger.info(TextUtil.format("[{0},{1},{2}]恢复成功。", role.getName(), role.getAccount(), role.getId()));
	}

	public static void scheduleTask(DelayedTask task) {
		timer.putTask(task);
	}

	/**
	 * 停止辅助线程，主逻辑线程工作
	 */
	public static void shutdown() {
		timer.setWork(false);
		logicExecutor.shutdownNow();
	}

	/**
	 * 使用主逻辑线程执行
	 * 
	 * @param runable
	 */
	public static void execute(Runnable runable) {
		if (runable == null || logicExecutor.isShutdown()) {
			return;
		}

		if (Thread.currentThread().getId() == logicThreadId) {// 调用线程就是主线程则直接执行
			runable.run();
		} else {
			logicExecutor.execute(runable);
		}
	}

	/**
	 * 设置动态过载状态
	 * 
	 * @param b
	 */
	public static void setDynamicOverload(boolean b) {
		dynamicOverload = b;
	}

	/**
	 * 设置静态过载状态
	 * 
	 * @param b
	 */
	public static void setStaticOverload(boolean b) {
		staticOverload = b;
	}

	/**
	 * 获取服务器是否过载状态，内部结合动态和静态两个状态标识，只要有一个显示过载则返回true
	 * 
	 * @return
	 */
	public static boolean isOverload() {
		return XsgRoleManager.getInstance().isOnlineOverload() || staticOverload || dynamicOverload;
	}

	public static int getLogicQueueSize() {
		return logicExecutor.getQueue().size();
	}

	public static boolean isTerminated() {
		return logicExecutor.isTerminated();
	}
	
	/** 打印执行队列内容 */
	public static void printQueue() {
		if (logicExecutor != null) {
			ThreadPoolExecutor poolExe = logicExecutor;
			BlockingQueue<Runnable> queue = poolExe.getQueue();
			if (queue != null) {
				Map<String, Integer> statistics = new HashMap<String, Integer>();
				for (Runnable runnable : queue) {
					String name = runnable.getClass().getName();
					int count = 0;
					if (statistics.containsKey(name)) {
						count = statistics.get(name);
					}
					count++;
					statistics.put(name, count);
				}
				if (statistics.size() > 0) {
					logger.error(">>>>>>>>>>>>>>>>> LogicThread <<<<<<<<<<<<<<<<<<<<<");
					for (Map.Entry<String, Integer> entry : statistics.entrySet()) {
						logger.error(TextUtil.format("LogicThreadQueue: {0}:{1}", entry.getKey(), entry.getValue()));
					}
					logger.error(">>>>>>>>>>>>>>>>> LogicThread <<<<<<<<<<<<<<<<<<<<<");
				}
			}
		}
	}
}

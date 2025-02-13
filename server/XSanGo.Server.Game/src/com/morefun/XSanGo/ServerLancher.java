/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.morefun.XSanGo.auction.XsgAuctionHouseManager;
import com.morefun.XSanGo.cache.XsgCacheManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.db.game.ServerOpenCloseLog;
import com.morefun.XSanGo.db.game.ServerPerformance;
import com.morefun.XSanGo.db.game.SimpleDAO;
import com.morefun.XSanGo.faction.IFaction;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager;
import com.morefun.XSanGo.haoqingbao.XsgHaoqingbaoManager;
import com.morefun.XSanGo.monitor.ProcessStat;
import com.morefun.XSanGo.monitor.XsgMonitorManager;
import com.morefun.XSanGo.net.GameSessionManagerI;
import com.morefun.XSanGo.net.IceEntry;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.stat.XsgStatManager;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

public class ServerLancher {

	private static String appProperties = "app.properties";
	private static ApplicationContext ac;
	private final static Log logger = LogFactory.getLog(ServerLancher.class);
	private static String[] argArray;
	private static int serverId;
	private static boolean debug;
	private static boolean canCreateNewRole;
	/** 服务器启动时间 */
	private static Date openTime;
	private static String iceConfigPath;
	private static boolean roleL2cache;

	public static void main(String[] args) {
		initIceConfigFilePath(args);
		argArray = args;
		logger.info("Game server startup.");
		String springConfigPath = "applicationContextMain.xml";
		for (String arg : args) {
			if (arg.startsWith("--Spring.Config=")) {
				springConfigPath = arg.substring(arg.lastIndexOf("=") + 1,
						arg.length());
			}
		}

		try {
			ac = new ClassPathXmlApplicationContext(
					new String[] { springConfigPath });
			appProperties = ac.getBean("AppConfigFile", String.class);
			debug = ac.getBean("DebugMode", Boolean.class);
			roleL2cache = ac.getBean("RoleL2cache", Boolean.class);
			canCreateNewRole = ac.getBean("CreateNewRole", Boolean.class);
			parseServerId(args);

			LogicThread.init();

			if (checkVindicate()) {
				vindicate();
				logger.info("Vindicate completed.");
				System.exit(0);
			}

			logger.info("Logic ready.");
			IceEntry.init(args);
		} catch (Exception e) {
			logger.error(e, e);
			return;
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				logger.info("Exit by OS.");
				// 通过系统关闭
				saveExit();
			}
		});

		openTime = Calendar.getInstance().getTime();
		logger.info("Game server is ready.");
		IceEntry.getCommunicator().waitForShutdown();

		// logger.info("Exit by IceGrid.");
		// saveExit();// 通过grid管理工具stop命令停止，实际为调用communicator的shutdown方法
	}

	private static void parseServerId(String[] args)
			throws FileNotFoundException, IOException {
		Properties sysConfig = new Properties();
		InputStream iStream = new FileInputStream(iceConfigPath);
		sysConfig.load(iStream);
		serverId = Integer
				.parseInt(sysConfig.getProperty("Ice.Admin.ServerId"));
	}

	/**
	 * 初始化ICE配置文件路径
	 * 
	 * @param args
	 */
	private static void initIceConfigFilePath(String[] args) {
		iceConfigPath = appProperties;
		for (String arg : args) {
			if (arg.startsWith("--Ice.Config=")) {
				iceConfigPath = arg.substring(arg.lastIndexOf("=") + 1,
						arg.length());
			}
		}
	}

	/**
	 * @return
	 */
	private static boolean checkVindicate() {
		for (String arg : argArray) {
			if (arg.equalsIgnoreCase("-vindicate")) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 程序优雅关闭，依次保存在线玩家，帮派，公共数据，统计，缓存玩家，辅助数据。 顺序不可轻易更改
	 */
	private static void saveExit() {
		logger.info("stopping fight movie validate...");
		XsgFightMovieManager.getInstance().stopValidateFightMovie();
		logger.info("Stopping Logic and Timer Threads...");
		LogicThread.shutdown();
		// 先关主线程再群发消息，这是因为先发消息则如果主线程同时接收到一个创建连接请求，
		// 则会抛出ConcurrentModificationException，导致整个关闭钩子无法执行
		try {
			GameSessionManagerI.getInstance().notifyCloseAllSession();
		} catch (Exception e) {
			logger.error("Session clear error.", e);
		}

		Cache roleCache = XsgCacheManager.getInstance().getCache(
				Const.Role.CACHE_NAME_ROLE);
		Cache lv2Cache = XsgCacheManager.getInstance().getCache(
				Const.L2_Cache_name);
		// 优先保存当前在线玩家数据
		List<IRole> list = XsgRoleManager.getInstance().findOnlineList();
		logger.info(list.size() + " online roles to save.");
		for (IRole role : list) {
			role.saveAsyn();
			roleCache.remove(role.getRoleId());
			lv2Cache.remove(role.getRoleId());
		}

		// 保存帮派数据
		Cache factionCache = XsgCacheManager.getInstance().getCache(
				Const.Faction.CACHE_NAME_FACTION);
		logger.info(factionCache.getSize() + " factions to save.");
		for (Object key : factionCache.getKeysWithExpiryCheck()) {
			((IFaction) factionCache.get(key).getObjectValue()).saveAsyn();
		}

		try {// 保存公共数据，这一步要在对外接口和逻辑关闭之后执行，避免ID序列错乱
			logger.info("Saving server data...");
			GlobalDataManager.getInstance().shutdown();
		} catch (Exception e1) {
			logger.error(e1);
		}
		// 保存货币统计数据
		logger.info("Saving ecnomy data...");
		XsgStatManager.getInstance().save2Cache();
		// XsgCacheManager
		// .getInstance()
		// .getCache(XsgStatManager.Ecnomy_Cache_Name)
		// .put(new Element(XsgStatManager.Ecnomy_Cache_Key,
		// XsgStatManager.getInstance().getEcnomyStat()));
		// 保存缓存中的角色数据
		logger.info(roleCache.getSize() + "cache roles to save.");
		// 优先保存二级缓存中有的角色，这样可以减轻IO负担，加快处理速度
		for (Object key : lv2Cache.getKeysWithExpiryCheck()) {
			if (roleCache.get(key) != null) {
				saveCacheRole(roleCache, key);
				roleCache.remove(key);
			}
		}
		for (Object key : roleCache.getKeysWithExpiryCheck()) {
			saveCacheRole(roleCache, key);
		}

		// 保存拍卖行数据
		logger.info("Saving auction house data...");
		XsgAuctionHouseManager.getInstance().saveAuctionHouseStatus();
		logger.info("saving redpacket data...");
		XsgHaoqingbaoManager.getInstance().saveAll();

		// 保存性能监控数据
		final ServerOpenCloseLog ocl = new ServerOpenCloseLog(openTime,
				Calendar.getInstance().getTime());
		for (ProcessStat stat : XsgMonitorManager.getInstance()
				.getAllMonitorItems()) {
			ocl.getPerformances().add(
					new ServerPerformance(ocl, stat.getName(), stat
							.getProcessTime(), stat.getTotalCostTime(), stat
							.getMaxCostTime(), stat.getAvgCostTime()));
		}

		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				SimpleDAO.getFromApplicationContext(getAc()).save(ocl);
			}
		});

		DBThreads.shutdown();
		try {
			while (!DBThreads.isTerminated()) {
				logger.info(TextUtil.format(
						"Waiting for Database completed,{0} tasks remain.",
						DBThreads.getQueueSize()));
				Thread.sleep(2000);
			}
		} catch (InterruptedException e) {
			LogManager.error(e);
		}

		Cache errorRoleCache = XsgCacheManager.getInstance().getCache(
				Const.ErrorData_Cache_Name);
		logger.warn(TextUtil.format("Error Role count is {0}.",
				errorRoleCache.getSize()));
		logger.info("Closing cache");
		XsgCacheManager.getInstance().shutdown();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			LogManager.error(e);
		}

		logger.info("Game Over!");
	}

	/**
	 * 保存缓存的角色对象，其他对象缓存不应调用此方法，否则会抛类型转换异常
	 * 
	 * @param cache
	 * @param key
	 */
	private static void saveCacheRole(Cache cache, Object key) {
		Element element = cache.get(key);
		if (element == null) {
			return;
		}
		IRole role = (IRole) element.getObjectValue();
		if (!role.isRobot()) {// 机器人就不要浪费资源了
			role.saveAsyn();
		}
	}

	/**
	 * 数据维护
	 */
	private static void vindicate() {
	}

	public static ApplicationContext getAc() {
		return ac;
	}

	public static int getServerId() {
		return serverId;
	}

	public static boolean isDebug() {
		return debug;
	}

	public static boolean canCreateNewRole() {
		return canCreateNewRole;
	}

	public static boolean isRoleL2cacheOpen() {
		return roleL2cache;
	}
}

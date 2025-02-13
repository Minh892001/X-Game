/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;

import com.XSanGo.Protocol.AMD_Gm_editChannel;
import com.XSanGo.Protocol.AMD_Gm_editGameServer;
import com.XSanGo.Protocol.GuideConfig;
import com.XSanGo.Protocol.ServerItem;
import com.XSanGo.Protocol.ServerList;
import com.XSanGo.Protocol.WhiteList;
import com.XSanGo.Protocol.WhiteListType;
import com.morefun.XSanGo.MFBI.XsgMFBIManager;
import com.morefun.XSanGo.announce.XsgAnnounceManager;
import com.morefun.XSanGo.channel.XsgChannelServerManager;
import com.morefun.XSanGo.db.Account;
import com.morefun.XSanGo.db.Channel;
import com.morefun.XSanGo.db.ChannelDAO;
import com.morefun.XSanGo.db.GameServer;
import com.morefun.XSanGo.db.GameServerDAO;
import com.morefun.XSanGo.gm.DynamicConfig;
import com.morefun.XSanGo.http.CheckServerConfig;
import com.morefun.XSanGo.net.GameServerRT;
import com.morefun.XSanGo.net.IceEntry;
import com.morefun.XSanGo.net.ServerState;
import com.morefun.XSanGo.stat.DragonStatManager;
import com.morefun.XSanGo.stat.IStat;
import com.morefun.XSanGo.util.CollectionUtil;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.DelayedTaskThread;
import com.morefun.XSanGo.util.IPredicate;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

public class CenterServer {
	protected final static Log logger = LogFactory.getLog(CenterServer.class);

	private static final String Dynamic_Config_FileName = "dynamic.config";

	private static CenterServer instance;

	public synchronized static CenterServer instance() {
		if (instance == null) {
			instance = new CenterServer();
		}
		return instance;
	}

	private static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);

	private static DelayedTaskThread timer = new DelayedTaskThread();

	/** 服务器DAO */
	private GameServerDAO serverDAO;

	/** 渠道DAO */
	private ChannelDAO channelDAO;

	/** 渠道列表 */
	private Map<Integer, Channel> channelMap;

	/** 服务器列表缓存 */
	private Map<Integer, GameServerInfo> gameServerMap;

	/** 统计接口 */
	private IStat stat;

	/** 开发者CP白名单 */
	// private WhiteList cpWhiteList = LoginDatabase.instance().getAc()
	// .getBean("CpWhiteList", WhiteList.class);
	/** SP审核配置 */
	private CheckServerConfig spConfig = LoginDatabase.instance().getAc()
			.getBean("SpCheckServer", CheckServerConfig.class);

	/**
	 * 包含白名单，推荐服务器等动态参数
	 */
	private DynamicConfig dynamicConfig;

	/**
	 * @param runnable
	 */
	public static void execute(Runnable runnable) {
		executor.execute(runnable);
	}

	/**
	 * 
	 */
	public void init() {
		this.dynamicConfig = this.readDynamicConfigFromFile();
		// 发送数据中心
//		XsgMFBIManager.getInstance();TODO 暂时用不到，先关闭

		// 初始化辅助线程
		timer.start();

		// 初始化DAO
		initDAO();

		// 从数据库加载服务器列表
		loadServerList();
		logger.debug("server list on line!");

		// 加载渠道
		loadChannels();
		logger.debug("channel on line!");

		XsgAnnounceManager.getInstance();
		XsgChannelServerManager.getInstance();

		// 初始化统计接口
		this.stat = DragonStatManager.instance().getStat();

		// 保存各服务器的在线人数
		scheduleTask(new DelayedTask(20 * 1000, 10 * 60 * 1000) {

			@Override
			public void run() {

				execute(new Runnable() {

					@Override
					public void run() {

						updateOnlineCounts();
					};
				});
			}
		});

		// 初始化连接检测定时器
		scheduleTask(new DelayedTask(0, 5 * 1000) {

			@Override
			public void run() {
				int size = executor.getQueue().size();
				if (size > 50) {
					LogManager.warn("center executor queue size:" + size);
				}
				execute(new Runnable() {

					@Override
					public void run() {
						updateServerStates();
					};
				});
			}
		});
	}

	private DynamicConfig readDynamicConfigFromFile() {
		String text = "";
		ClassPathResource cpr = new ClassPathResource(Dynamic_Config_FileName);
		try {
			if (cpr.getFile().exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(cpr.getFile()));// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					text += lineTxt;
					text += "\n";
				}
				bufferedReader.close();
				read.close();
			}
		} catch (FileNotFoundException e) {
			logger.error(e, e);
		} catch (IOException e) {
			logger.error(e, e);
		}

		DynamicConfig config = TextUtil.GSON.fromJson(text, DynamicConfig.class);
		if (config == null) {
			config = new DynamicConfig();
		}

		return config;
	}

	private void initDAO() {
		this.serverDAO = GameServerDAO.getFromApplicationContext(LoginDatabase.instance().getAc());
		this.channelDAO = ChannelDAO.getFromApplicationContext(LoginDatabase.instance().getAc());
	}

	/**
	 * 
	 */
	public void fini() {

		timer.setWork(false);
		// updateServerThread.setWork(false);
	}

	public static void scheduleTask(DelayedTask task) {
		timer.putTask(task);
	}

	/**
	 * 开启服务
	 */
	public void start() {
		IceEntry.instance().startLogin();
	}

	private void loadChannels() {
		this.channelMap = new ConcurrentHashMap<Integer, Channel>();
		for (Channel channel : channelDAO.findAll()) {
			this.channelMap.put(channel.getId(), channel);
		}
	}

	/**
	 * 注意,只能在初始化的时候调用该函数
	 */
	@SuppressWarnings("unchecked")
	private void loadServerList() {
		this.gameServerMap = new HashMap<Integer, GameServerInfo>();

		// 从数据库中获取服务器列表
		List<GameServer> serverDBs = serverDAO.findAll();

		for (GameServer serverDB : serverDBs) {
			// 获取服务器的当前动态信息
			GameServerInfo serverInfo = this.addOrUpdateGameServer(serverDB);

			logger.debug("add server " + serverInfo.getName() + "(" + serverInfo.getId() + ")," + serverInfo.getHost()
					+ ":" + serverInfo.getPort());
		}
	}

	/**
	 * @param serverDB
	 * @return
	 */
	private GameServerInfo addOrUpdateGameServer(GameServer serverDB) {
		GameServerInfo serverInfo = this.gameServerMap.get(serverDB.getId());
		if (serverInfo != null) {// 已经存在则更新
			serverInfo.setDb(serverDB);
			return serverInfo;
		}

		GameServerRT serverRT = new GameServerRT();
		serverRT.id = serverDB.getId();

		// 生成服务器信息对象并且保存在缓存中
		serverInfo = new GameServerInfo(serverDB, serverRT);
		this.gameServerMap.put(serverInfo.getId(), serverInfo);

		return serverInfo;
	}

	public ServerItem getServerItemView(GameServer server) {
		if (server == null) {
			return new ServerItem(0, 0, "", "", 0, 0, false, 0);
		}

		return getServerItemView(server.getId());
	}

	/**
	 * 获取服务器网络数据
	 * 
	 * @param id
	 * @param roleCount
	 * @return
	 */
	private ServerItem getServerItemView(int id) {
		GameServerInfo serverInfo = findServerInfoById(id);
		ServerItem serverItem = new ServerItem(id, serverInfo.getDB().getShowId(), serverInfo.getName(),
				serverInfo.getHost(), serverInfo.getPort(), serverInfo.getState().ordinal(), serverInfo.isNew(),
				serverInfo.getDB().getTargetId());

		return serverItem;
	}

	/**
	 * 根据id查找服务器信息
	 * 
	 * @param id
	 * @return
	 */
	public GameServerInfo findServerInfoById(int id) {
		return this.gameServerMap.get(id);
	}

	/**
	 * 获取服务器列表网络数据
	 * 
	 * @param accountDB
	 * @return
	 */
	public ServerList getServerListView(Account accountDB) {
		ServerList curList = new ServerList();
		RecentData[] dataArray = this.parseRecentData(accountDB);
		Arrays.sort(dataArray, new Comparator<RecentData>() {
			@Override
			public int compare(RecentData arg0, RecentData arg1) {
				return arg1.loginTime.compareTo(arg0.loginTime);
			}
		});

		// 最近登陆的服务器
		List<ServerItem> lastLogins = new ArrayList<ServerItem>();
		for (int i = 0; i < dataArray.length; i++) {
			int id = dataArray[i].serverId;
			if (this.gameServerMap.containsKey(id)) {
				lastLogins.add(this.getServerItemView(this.gameServerMap.get(id).getDB()));
			}
		}

		curList.lastLogin = lastLogins.toArray(new ServerItem[] {});

		// 其他服务器
		SortedSet<ServerItem> servers = new TreeSet<ServerItem>(new Comparator<ServerItem>() {

			@Override
			public int compare(ServerItem o1, ServerItem o2) {
				// 谁是推荐服谁就比较小，这样可以排前面
				int recommend = dynamicConfig.getRecommendServer();
				if (o1.id == recommend) {
					return -1;
				}
				if (o2.id == recommend) {
					return 1;
				}

				return o2.id - o1.id;
			}
		});
		for (GameServerInfo info : this.gameServerMap.values()) {
			servers.add(getServerItemView(info.getDB()));
		}

		curList.totalList = servers.toArray(new ServerItem[] {});
		this.guidePolicy(curList, accountDB);

		return curList;

	}

	/**
	 * 查找渠道
	 * 
	 * @param id
	 * @return
	 */
	public Channel findChannel(int id) {
		return this.channelMap.get(id);
	}

	/**
	 * 服务器器是否已经准备好
	 * 
	 * @param id
	 * @return
	 */
	public boolean isServerReady(int id) {
		GameServerInfo info = findServerInfoById(id);

		if (info == null) {
			return false;
		}

		if (info.getState() == ServerState.DISABLED) {
			return false;
		}
		return true;
	}

	/**
	 * @param gameServerInfo
	 */
	public void notifyServerStateChange(int serverId) {
		GameServerInfo serverInfo = findServerInfoById(serverId);

		System.out.println("server" + serverInfo.getName() + "state= " + serverInfo.getState());
	}

	private void updateOnlineCounts() {
		for (final GameServerInfo item : this.gameServerMap.values()) {
			LoginDatabase.execute(new Runnable() {

				@Override
				public void run() {
					stat.online(item.getId(), item.getOnlineCount());
				}
			});
		}
	}

	public Map<Integer, GameServerInfo> getAllServers() {
		return this.gameServerMap;
	}

	/**
	 * 刷新服务器状态,这个函数由专门的线程调用
	 */
	public void updateServerStates() {
		for (final GameServerInfo item : this.gameServerMap.values()) {
			IceEntry.instance().updateServerState(item.getId());
		}
	}

	/**
	 * @param id
	 */
	public void removeGameServer(int id) {
		this.gameServerMap.remove(id);
	}

	/**
	 * 更新帐号的最近登录服务器列表数据
	 * 
	 * @param account
	 * @param serverId
	 */
	public void updateAccountRecentServerData(Account account, int serverId) {
		RecentData[] array = parseRecentData(account);
		HashMap<Integer, RecentData> dataMap = new HashMap<Integer, RecentData>();
		for (RecentData recentData : array) {
			dataMap.put(recentData.serverId, recentData);
		}
		dataMap.put(serverId, new RecentData(serverId, Calendar.getInstance().getTime()));
		array = dataMap.values().toArray(new RecentData[0]);

		Arrays.sort(array, new Comparator<RecentData>() {
			@Override
			public int compare(RecentData arg0, RecentData arg1) {
				return arg1.loginTime.compareTo(arg0.loginTime);
			}
		});
		// 最近登录服务器个数限制
		int lengthLimit = 8;
		if (array.length > lengthLimit) {
			array = Arrays.copyOf(array, lengthLimit);
		}
		account.setRecentServers(TextUtil.GSON.toJson(array));
		XsgAccountManager.getInstance().updateAccount(account);
	}

	private RecentData[] parseRecentData(Account account) {
		RecentData[] dataArray = TextUtil.GSON.fromJson(account.getRecentServers(), RecentData[].class);
		if (dataArray == null) {
			dataArray = new RecentData[0];
		}
		return dataArray;
	}

	public Collection<Channel> getAllChannel() {
		return this.channelMap.values();
	}

	/**
	 * 是否CP用户，如果CP控制未开启或者开启，但是用户在CP白名单中，则返回true
	 * 
	 * @param account
	 *            玩家帐号
	 * @param remoteIp
	 *            远程登录IP
	 * @return
	 */
	public boolean isCpUser(String account, String remoteIp) {
		WhiteList wl = this.dynamicConfig.findWhiteList(WhiteListType.Cp);

		return wl == null || this.isInWhiteList(wl, remoteIp, account);
	}

	/**
	 * 匹配白名单规则
	 * 
	 * @param wl
	 * @param remoteIp
	 * @param account
	 * @return
	 */
	public boolean isInWhiteList(WhiteList wl, String remoteIp, String account) {
		if (!wl.enable) {
			return true;
		}

		for (String ip : wl.whiteIpList) {
			if (ip.equals(remoteIp)) {
				return true;
			}
		}

		for (String wa : wl.whiteAccountList) {
			if (wa.equals(account)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 从SP审核角度控制指定服务器对指定的版本是否可见
	 * 
	 * @param server
	 * @param clientVersion
	 * @return
	 */
	public boolean checkAvilableForSp(ServerItem server, String clientVersion) {
		boolean versionEqual = this.spConfig.getVersion().equals(clientVersion);
		boolean serverEqual = this.spConfig.getServerId() == server.id;
		return (versionEqual && serverEqual) || (!versionEqual && !serverEqual);
	}

	public void editGameServer(final AMD_Gm_editGameServer __cb, final GameServer gs) {
		LoginDatabase.execute(new Runnable() {
			@Override
			public void run() {
				serverDAO.attachDirty(gs);

				execute(new Runnable() {

					@Override
					public void run() {
						addOrUpdateGameServer(gs);
						__cb.ice_response();
					}
				});
			}
		});
	}

	public void editChannel(final AMD_Gm_editChannel __cb, final Channel channel) {
		LoginDatabase.execute(new Runnable() {
			@Override
			public void run() {
				channelDAO.attachDirty(channel);

				execute(new Runnable() {

					@Override
					public void run() {
						channelMap.put(channel.getId(), channel);
						__cb.ice_response();
					}
				});
			}
		});
	}

	/**
	 * 获取指定类型白名单，
	 * 
	 * @param type
	 * @return
	 */
	public WhiteList findWhiteList(WhiteListType type) {
		WhiteList wl = this.dynamicConfig.findWhiteList(type);
		if (wl == null) {
			wl = new WhiteList(type, false, new String[0], new String[0], "");
			this.dynamicConfig.setWhiteList(wl);
		}

		return wl;
	}

	/**
	 * 设置推荐服务器
	 * 
	 * @param serverId
	 */
	public void setRecommendServer(int serverId) {
		this.dynamicConfig.setRecommendServer(serverId);
	}

	/**
	 * 获取当前的白名单配置，没有则根据种类生成默认对象
	 * 
	 * @return
	 */
	public WhiteList[] getCurrentWhiteList() {
		List<WhiteList> list = new ArrayList<WhiteList>();
		for (WhiteListType type : WhiteListType.values()) {
			list.add(this.findWhiteList(type));
		}

		return list.toArray(new WhiteList[0]);
	}

	public void setWhiteList(WhiteList param) {
		this.dynamicConfig.setWhiteList(param);
	}

	public void shutdown() throws FileNotFoundException, IOException {
		ClassPathResource cpr = new ClassPathResource(Dynamic_Config_FileName);
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(cpr.getFile()));// 考虑到编码格式
		writer.write(TextUtil.GSON.toJson(this.dynamicConfig));
		writer.close();
	}

	/**
	 * 获取小黑屋配置规则，没有则创建个默认的
	 * 
	 * @return
	 */
	public GuideConfig getGuideConfig() {
		GuideConfig result = this.dynamicConfig.getGuideConfig();
		if (result == null) {
			result = new GuideConfig(false, new int[0], "", "", new int[0]);
			this.dynamicConfig.setGuideConfig(result);
		}

		return result;
	}

	/**
	 * 设置小黑屋配置规则
	 * 
	 * @param config
	 */
	public void setGuideConfig(GuideConfig config) {
		this.dynamicConfig.setGuideConfig(config);
	}

	/**
	 * 服务器列表的小黑屋控制，用于特殊情况根据渠道或注册时间引导到小黑屋
	 * 
	 * @param serverList
	 * @param db
	 */
	private void guidePolicy(ServerList serverList, Account db) {
		GuideConfig config = this.getGuideConfig();
		if (!config.enable) {
			return;
		}

		// 不同的配置开关及匹配结果
		boolean channelFlag = false, channelMatch = false, beginFlag = false, beginMatch = false, endFlag = false, endMatch = false;
		if (config.channelIds.length > 0) {
			channelFlag = true;
			for (int channel : config.channelIds) {
				if (db.getChannelId() == channel) {
					channelMatch = true;
					break;
				}
			}
		}

		if (!TextUtil.isNullOrEmpty(config.beginTime)) {
			beginFlag = true;
			Date temp = DateUtil.parseDate(config.beginTime);
			beginMatch = db.getCreateTime().after(temp);
		}

		if (!TextUtil.isNullOrEmpty(config.endTime)) {
			endFlag = true;
			Date temp = DateUtil.parseDate(config.endTime);
			endMatch = temp.after(db.getCreateTime());
		}

		boolean match = true;
		if (channelFlag) {
			match &= channelMatch;
		}
		if (beginFlag) {
			match &= beginMatch;
		}
		if (endFlag) {
			match &= endMatch;
		}
		if (!match) {
			return;
		}
		List<Integer> showList = new ArrayList<Integer>();// 需要显示的服务器
		for (int server : config.targetServers) {
			showList.add(server);
		}

		if (showList.size() > 0) {
			this.handleServerList(serverList, showList, false);
		}
	}

	/**
	 * 根据子列表及对应的控制开关，清理或保留子列表中的服务器
	 * 
	 * @param serverList
	 *            服务器列表
	 * @param subList
	 *            需要控制的列表
	 * @param containInSub
	 *            删除的服务器是否包含在控制表中，是则从服务器列表中移除所有subList中的服务器，否则只保留subList中的服务器
	 */
	public void handleServerList(ServerList serverList, final List<Integer> subList, final boolean containInSub) {
		IPredicate<ServerItem> pre = new IPredicate<ServerItem>() {
			@Override
			public boolean check(ServerItem item) {
				boolean temp = subList.contains(item.id);
				return containInSub ? temp : !temp;
			}
		};
		// 过滤服务器
		List<ServerItem> list = new ArrayList<ServerItem>(Arrays.asList(serverList.lastLogin));
		CollectionUtil.removeWhere(list, pre);
		serverList.lastLogin = list.toArray(new ServerItem[0]);

		list = new ArrayList<ServerItem>(Arrays.asList(serverList.totalList));
		CollectionUtil.removeWhere(list, pre);
		serverList.totalList = list.toArray(new ServerItem[0]);
	}

	/**
	 * 获取指定服务器ID当前所在的实际游戏服编号
	 * 
	 * @param serverId
	 * @return
	 */
	public int getRealServerId(int serverId) {
		GameServerInfo gs = null;
		while ((gs = this.findServerInfoById(serverId)) != null && gs.getDB().getTargetId() != 0) {
			serverId = gs.getDB().getTargetId();
			if (gs.getId() == serverId) {
				break;
			}
		}

		return serverId;
	}
}

class RecentData {

	public int serverId;

	public Date loginTime;

	public RecentData(int serverId, Date time) {
		this.serverId = serverId;
		this.loginTime = time;
	}

}
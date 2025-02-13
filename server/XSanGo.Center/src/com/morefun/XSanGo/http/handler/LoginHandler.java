/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.http.handler;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.XSanGo.Protocol.ClientConfig;
import com.XSanGo.Protocol.ServerItem;
import com.XSanGo.Protocol.ServerList;
import com.morefun.XSanGo.CenterServer;
import com.morefun.XSanGo.GameServerInfo;
import com.morefun.XSanGo.LoginDatabase;
import com.morefun.XSanGo.XsgAccountManager;
import com.morefun.XSanGo.MFBI.XsgMFBIManager;
import com.morefun.XSanGo.channel.ChannelServerT;
import com.morefun.XSanGo.channel.XsgChannelServerManager;
import com.morefun.XSanGo.client.Announcement;
import com.morefun.XSanGo.db.Account;
import com.morefun.XSanGo.db.GameServer;
import com.morefun.XSanGo.db.stat.LoginLog;
import com.morefun.XSanGo.db.stat.StatSimpleDAO;
import com.morefun.XSanGo.http.DevelopSdk;
import com.morefun.XSanGo.net.ServerState;
import com.morefun.XSanGo.sdk.ISdk;
import com.morefun.XSanGo.sdk.LoginResponse;
import com.morefun.XSanGo.sdk.meifeng.MeiFengSdk;
import com.morefun.XSanGo.sdk.miyu.MiYuSdk;
import com.morefun.XSanGo.util.CollectionUtil;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.EncryptUtil;
import com.morefun.XSanGo.util.IPredicate;
import com.morefun.XSanGo.util.TextUtil;
import com.sun.net.httpserver.HttpExchange;

/**
 * 登录请求处理器
 * 
 * @author sulingyun
 * 
 */
public class LoginHandler extends AbsHandler {
	private static Logger log = LogManager.getLogger(LoginHandler.class);
	private static AtomicLong monitorThreadId = new AtomicLong(0);
	private static Map<String, ISdk> SDK_MAP = new HashMap<String, ISdk>();
	static {//跟客户端约定的key
		SDK_MAP.put("develop", new DevelopSdk());//仅供开发期间绕过帐号验证用
		SDK_MAP.put("mf", new MeiFengSdk());
		SDK_MAP.put("miyu", new MiYuSdk());//米娱sdk
	}

	/**
	 * 服务器列SP权限控制，审核跟正常对外服务器可见逻辑是互斥的，即当客户端版本为SP审核时只能看到审核服，否则只能看到非审核服
	 * 
	 * @param serverList
	 * @param clientVersion
	 */
	private void spPolicy(ServerList serverList, String clientVersion) {
		List<Integer> delList = new ArrayList<Integer>();// 需要移除的服务器
		// 遍历所有服务器，记录需要过滤的服务器
		for (ServerItem server : serverList.totalList) {
			GameServer gs = CenterServer.instance().findServerInfoById(server.id).getDB();
			if (!CenterServer.instance().checkAvilableForSp(server, clientVersion)) {
				delList.add(gs.getId());
			}
		}

		CenterServer.instance().handleServerList(serverList, delList, true);
	}

	/**
	 * 服务器列表CP权限控制，可见及可入权限控制
	 * 
	 * @param serverList
	 */
	private void cpPolicy(ServerList serverList, String account, String remoteIp) {
		boolean isCp = CenterServer.instance().isCpUser(account, remoteIp);
		if (isCp) {// 如果是CP，则不做任何特殊处理
			return;
		}

		List<Integer> delList = new ArrayList<Integer>();// 需要移除的服务器
		// 先遍历所有服务器，记录需要过滤的服务器和改变服务器状态
		for (ServerItem server : serverList.totalList) {
			GameServerInfo gs = CenterServer.instance().findServerInfoById(server.id);
			if (gs.isCpShowOnly()) {
				delList.add(gs.getId());
			} else if (gs.isCpEnterOnly()) {
				server.state = ServerState.DISABLED.ordinal();
			}
		}
		// 再遍历最近登录服务器，由于需要过滤的前面已经处理，所以这里只做改变服务器状态的逻辑
		for (ServerItem server : serverList.lastLogin) {
			GameServerInfo gs = CenterServer.instance().findServerInfoById(server.id);
			if (gs.isCpEnterOnly()) {
				server.state = ServerState.DISABLED.ordinal();
			}
		}

		CenterServer.instance().handleServerList(serverList, delList, true);
	}

	@Override
	protected void innerHandle(HttpExchange httpExchange) throws IOException {
		this.initMonitor();
		LoginResponse2Client clientRes = new LoginResponse2Client();
		Map<String, String> paramsMap = this.parseParamsMap(httpExchange);
		String userName = paramsMap.get("userName");
		String pwd = paramsMap.get("pwd");
		String sdk = paramsMap.get("sdk");
		String time = paramsMap.get("time");
		String client = paramsMap.get("client");
		log.debug(client);
		// xmdmm=小马的秘密=客户端程序签名MD5
		// {"version":"2.1.6","channel":"10000","mac":"none","xmdmm":"xsanguo_not_supported","xmamg":"9C-C9-76-D9-5D-16-2D-54-5C-2E-55-3E-E2-39-E0-8C"}
		final ClientConfig cInfo = TextUtil.GSON.fromJson(client, ClientConfig.class);

		if (cInfo == null || TextUtil.isNullOrEmpty(cInfo.version) || TextUtil.isNullOrEmpty(cInfo.channel)) {
			clientRes.errorMsg = "缺少参数。";
			String responseMsg = TextUtil.GSON.toJson(clientRes);
			this.printMonitorInfo();
			this.sendResponse(httpExchange, responseMsg);
			return;
		}
		// this.validateChannel(userName, cInfo);
		int channel = Integer.parseInt(cInfo.channel);

		if (SDK_MAP.containsKey(sdk)) {//所有登录帐号的验证包括美峰帐号、360、uc等，都统一提交一号通验证或是中转验证，即MeiFengSdk.java
			ISdk sdkHandler = SDK_MAP.get(sdk);
			//checkUserLogin里发送http请求验证帐号
			LoginResponse sdkServerResponse = sdkHandler.checkUserLogin(userName, pwd, time);
			final String remoteIp = this.getRemoteIp(httpExchange);
			if (sdkServerResponse.isSuccess()) {
				// 创建或更新数据库
				userName = this.generateUserName(userName);
				Account db = XsgAccountManager.getInstance().findAccount(userName);
				if (db == null) {
					db = XsgAccountManager.getInstance().createAccount(userName, channel, cInfo.mac);
					XsgAccountManager.getInstance().updateAccount(db);
					// 通知公司平台部门的接口 TODO
//					XsgMFBIManager.getInstance().onRegisterAccount(userName, remoteIp, cInfo.mac, cInfo.channel,
//							cInfo.version);
				}

				clientRes.active = db.getActive() == 1;
				clientRes.account = userName;
				clientRes.sessionId = UUID.randomUUID().toString();
				clientRes.serverList = CenterServer.instance().getServerListView(db);
				log.debug(userName + ":cp begin.");
				this.cpPolicy(clientRes.serverList, db.getAccount(), remoteIp);
				log.debug(userName + ":cp end.");
				this.spPolicy(clientRes.serverList, cInfo.version);

				// 市场运营AB包处理方案
				this.marketPolicy(clientRes.serverList, channel);

				XsgAccountManager.getInstance().loginSuccess(db, clientRes.sessionId, cInfo.mac, remoteIp,
						cInfo.version, channel);

				Announcement anncounce = LoginDatabase.instance().getAc().getBean("Announce", Announcement.class);
				if (anncounce.isShow()) {
					clientRes.announcement = anncounce.getContent();
				}

				// 记录日志
				final String finalUserName = userName;
				final int finalChannelId = db.getChannelId();
				LoginDatabase.execute(new Runnable() {
					@Override
					public void run() {
						StatSimpleDAO.getForStat(LoginDatabase.instance().getAc()).save(
								new LoginLog(finalUserName, finalChannelId, cInfo.mac, remoteIp, cInfo.version,
										Calendar.getInstance().getTime(), cInfo.xmdmm));
					}
				});

				// 通知公司平台部门的接口
				XsgMFBIManager.getInstance().onLoginAccount(userName, db.getCreateTime().getTime(), remoteIp,
						cInfo.mac, cInfo.channel, cInfo.version);
			} else {
				clientRes.errorMsg = "用户验证失败。";
			}
		} else {
			log.error("error setting for " + sdk);
			clientRes.errorMsg = "非法请求。";
		}

		String responseMsg = TextUtil.GSON.toJson(clientRes);
		log.debug(userName + ":" + responseMsg);
		this.printMonitorInfo();
		this.sendResponse(httpExchange, responseMsg);
	}

	/**
	 * 市场运营AB包方案，切割服务器列表的实现，根据渠道实现互斥
	 * 
	 * @param serverList
	 * @param channel
	 */
	private void marketPolicy(ServerList serverList, int channel) {
		final ChannelServerT template = XsgChannelServerManager.getInstance().getChannelServerT(channel);
		if (template == null) {
			return;
		}

		IPredicate<ServerItem> pre = new IPredicate<ServerItem>() {
			@Override
			public boolean check(ServerItem item) {
				return !template.containsServer(item.id);
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

	private void printMonitorInfo() {
		long id = Thread.currentThread().getId();
		if (monitorThreadId.get() == id) {
			log.info(TextUtil.format("Thread {0} begin writing...", id, DateUtil.toString(System.currentTimeMillis())));
		}
	}

	private void initMonitor() {
		if (monitorThreadId.get() == 0) {
			monitorThreadId.set(Thread.currentThread().getId());
		}
	}

	/**
	 * 验证渠道签名
	 * 
	 * @param userName
	 *            用户名
	 * @param cInfo
	 *            客户端提交的本地数据
	 */
	private void validateChannel(String userName, final ClientConfig cInfo) {
		// xmamg=md5(userName+channel+KEY)
		// KEY=1bc2541135f5c96efd5341907ab51f3d
		try {
			String channelMd5 = EncryptUtil.bytesToHexString(EncryptUtil.getMD5(TextUtil.format(
					"{0}{1}1bc2541135f5c96efd5341907ab51f3d", userName, cInfo.channel).getBytes()));

			if (TextUtil.isNullOrEmpty(cInfo.xmamg) || !cInfo.xmamg.equalsIgnoreCase(channelMd5)) {
				log.warn(TextUtil.format("Channel sign error:userName={0},channel={1}.", userName, cInfo.channel));
			}
		} catch (NoSuchAlgorithmException e) {
			log.error("Encrypt Error.", e);
		}
	}
}

class LoginResponse2Client {
	boolean active;
	String account;
	String sessionId;
	String errorMsg;
	ServerList serverList;
	String announcement;
}

class HttpResponseMonitor {
	/**
	 * 监控的线程编号
	 */
	public AtomicLong threadId = new AtomicLong(0);
	/**
	 * 最后一次开始回写的时间
	 */
	public AtomicLong lastResponseTime = new AtomicLong(0);
}
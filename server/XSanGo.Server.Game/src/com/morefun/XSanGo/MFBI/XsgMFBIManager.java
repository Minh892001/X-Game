/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.MFBI;

import java.util.concurrent.TimeUnit;

import com.XSanGo.Protocol.DeviceInfo;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.net.GameSessionManagerI;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.TextUtil;
import com.morefun.bi.sdk.EventType;
import com.morefun.bi.sdk.RoleInfo;
import com.morefun.bi.sdk.UserInfo;

/**
 * BI数据统计 管理
 * 
 * @author lvmingtao
 * 
 */
public class XsgMFBIManager {

	// private static final Log log = LogFactory.getLog(XsgMFBIManager.class);

	private static XsgMFBIManager instance = new XsgMFBIManager();
	public static DelayedBILogThread BILogThread = new DelayedBILogThread();

	// 4位游戏代码
	public String gameId = ServerLancher.getAc().getBean("BI_gameId",
			String.class);
	// 是否发送数据
	public boolean isStart = ServerLancher.getAc().getBean("BI_isStart",
			Boolean.class);
	// 在线统计玩家数量，间隔时间
	private int intervalTime = ServerLancher.getAc().getBean(
			"BI_onlineIntervalTime", Integer.class);
	// 是否测试数据
	public boolean isTest = ServerLancher.getAc().getBean("BI_isTest",
			Boolean.class);

	public static XsgMFBIManager getInstance() {
		return instance;
	}

	/**
	 * BI数据统计 的控制模块
	 * 
	 * @param roleRt
	 * @param roleDB
	 * @return
	 */
	public IMFBIControler createMFBIControler(IRole roleRt, Role roleDB) {
		return new MFBIControler(roleRt, roleDB);
	}

	private XsgMFBIManager() {

		// 定时 统计在线人数
		LogicThread.scheduleTask(new DelayedTask(0, TimeUnit.MINUTES
				.toMillis(intervalTime)) {
			@Override
			public void run() {
				onOnlineNum();
			}
		});

		// 启动BI发送数据的线程
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				XsgMFBIManager.BILogThread.start();
			}
		});
	}

	/**
	 * 开启BI数据发送
	 */
	public void star() {
		XsgMFBIManager.getInstance().isStart = true;
	}

	/**
	 * 设置BI是否有效,可用
	 * 
	 * @param enable
	 */
	public void star(boolean enable) {
		XsgMFBIManager.getInstance().isStart = enable;
		BILogThread.setEnable(enable);
	}

	/**
	 * 关闭BI数据发送
	 * 
	 * @throws InterruptedException
	 */
	public void shutdown() throws InterruptedException {
		XsgMFBIManager.getInstance().isStart = false;
		BILogThread.setEnable(false);
		TimeUnit.SECONDS.sleep(5);
		BILogThread.shutdown();
	}

	/**
	 * 发送消息
	 * 
	 * @param msg
	 */
	public void sendMsg(BIMessage msg) {
		if (isStart) {
			BILogThread.addMess(msg);
		}
	}

	/**
	 * 查询ID
	 * 
	 * @param roleId
	 * @return
	 */
	private String getIp(String roleId) {
		String ip = "";
		IRole roleRt = XsgRoleManager.getInstance().findRoleById(roleId);
		if (roleRt != null)
			ip = roleRt.getRemoteIp();

		return ip;
	}

	/**
	 * 角色注册 发送消息
	 * 
	 * @param role
	 * @param device
	 * @throws InterruptedException
	 */
	public void sendCreateRole(Role role, DeviceInfo device) {

		UserInfo userInfo = new UserInfo(isTest, role.getAccount(),
				device.firstInTime, 0, null, null, null,
				null, device.ip, device.mac, null, null, null,
				String.valueOf(device.channel), null);
		RoleInfo roleInfo = new RoleInfo(ServerLancher.getServerId(),
				role.getId(), role.getCreateTime().getTime(), 1, 0, 0, 0, 0, 0,
				0, null, null, null, null);

		roleInfo.reserve4 = TextUtil.GSON.toJson(new RoleParam(role.getName(), role.getSex()));
		
		BIMessage msg = new BIMessage();
		msg.setUserInfo(userInfo);
		msg.setRoleInfo(roleInfo);
		msg.setType(EventType.RoleCreate);
		msg.setParams(1);
		this.sendMsg(msg);
	}

	/**
	 * 账号在线数
	 */
	public void onOnlineNum() {
		BIMessage msg = new BIMessage();
		msg.setUserInfo(null);
		msg.setRoleInfo(new RoleInfo(ServerLancher.getServerId(), "", 0, 1, 0,
				0, 0, 0, 0, 0, null, null, null, null));
		msg.setType(EventType.Online);
		int onlineCount = GameSessionManagerI.getInstance().getOnlineCount();
		msg.setParams(DateUtil.toString(System.currentTimeMillis()),
				onlineCount, onlineCount, null);

		this.sendMsg(msg);
	}
}

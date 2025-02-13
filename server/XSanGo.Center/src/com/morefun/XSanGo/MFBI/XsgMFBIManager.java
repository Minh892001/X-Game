/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.MFBI;

import java.util.concurrent.TimeUnit;

import com.morefun.XSanGo.LoginDatabase;
import com.morefun.bi.sdk.EventType;
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
	private DelayedBILogThread BILogThread = new DelayedBILogThread();

	// 4位游戏代码
	public String gameId = LoginDatabase.instance().getAc()
			.getBean("BI_gameId", String.class);
	// 是否发送数据
	public boolean isStart = LoginDatabase.instance().getAc()
			.getBean("BI_isStart", Boolean.class);
	// 是否测试数据
	public boolean isTest = LoginDatabase.instance().getAc()
			.getBean("BI_isTest", Boolean.class);

	public static XsgMFBIManager getInstance() {
		return instance;
	}

	private XsgMFBIManager() {
		this.BILogThread.start();
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
	 * 帐号注册 发送消息
	 * 
	 * @param userName
	 * @param ip
	 * @param mac
	 * @param channel
	 * @param version
	 */
	public void onRegisterAccount(String userName, String ip, String mac,
			String channel, String version) {
		UserInfo userInfo = new UserInfo(isTest, userName,
				System.currentTimeMillis(), 0, null, null, null, null, ip, mac,
				null, null, null, channel, version);

		BIMessage msg = new BIMessage();
		msg.setUserInfo(userInfo);
		msg.setRoleInfo(null);
		msg.setType(EventType.GameAccountReg);

		this.sendMsg(msg);
	}

	/**
	 * 通知数据中心帐号登录
	 * 
	 * @param userName
	 * @param ip
	 * @param mac
	 * @param channel
	 * @param version
	 */
	public void onLoginAccount(String userName, long firstInTime, String ip,
			String mac, String channel, String version) {
		UserInfo userInfo = new UserInfo(isTest, userName, firstInTime, 0,
				null, null, null, null, ip, mac, null, null, null, channel,
				version);

		BIMessage msg = new BIMessage();
		msg.setUserInfo(userInfo);
		msg.setRoleInfo(null);
		msg.setType(EventType.GameAccountLogin);

		this.sendMsg(msg);
	}
}

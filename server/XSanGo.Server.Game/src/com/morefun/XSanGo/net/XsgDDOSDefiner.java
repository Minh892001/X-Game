/**
 * 
 */
package com.morefun.XSanGo.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.center.CenterI;
import com.morefun.XSanGo.center.XsgCenterManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.TextUtil;

/**
 * DDOS攻击防御类
 * 
 * @author sulingyun
 * 
 */
public final class XsgDDOSDefiner {

	/** 红色警戒线 */
	public static int RedThresholdPerMiniute = 75;
	/** 黄色警戒线 */
	public static int YellowThresholdPerMiniute = 55;
	/** 蓝色警戒线 */
	public static int BlueThresholdPerMiniute = 45;
	private static XsgDDOSDefiner instance = new XsgDDOSDefiner();

	public static XsgDDOSDefiner getInstance() {
		return instance;
	}

	/** 连接实时监控，每分钟或闲时清理 */
	private Map<String, Integer> monitorMap;
	/** 警告名单历史记录，闲时清理 */
	private Map<String, Integer> punishMap;
	/**
	 * 操作白名单
	 */
	private List<String> whiteList;

	private XsgDDOSDefiner() {
		// 这里暂时硬编码，需过滤操作变多或者更改频繁，则改为配置方式
		this.whiteList = new ArrayList<String>();
		whiteList.add("ping");
		whiteList.add("selectRival");
		whiteList.add("heroSkillLevelUp");
		whiteList.add("clear");
		whiteList.add("resetPractice");
		whiteList.add("useItem");

		this.monitorMap = new HashMap<String, Integer>();
		this.punishMap = new HashMap<String, Integer>();
		long interval = 60 * 1000;
		LogicThread.scheduleTask(new DelayedTask(0, interval) {

			@Override
			public void run() {
				handleMonitorData();
			}
		});
	}

	/**
	 * 处理监控数据
	 */
	private void handleMonitorData() {
		// 当逻辑负载较低时，直接清理监控数据
		// 服务器有压力的情况下，根据严重程度记分，然后根据记分情况予以封杀
		int currentLogicQueueSize = LogicThread.getLogicQueueSize();
		if (currentLogicQueueSize < 100) {// 负载较低，有攻击的也暂时放过
			this.punishMap.clear();
			this.monitorMap.clear();
			return;
		}

		for (String ip : this.monitorMap.keySet()) {
			int count = this.monitorMap.get(ip);
			int punishValue = 0;// 惩罚分值
			if (count > RedThresholdPerMiniute) {// 红色警戒，直接T并且冻结
				punishValue = 5;
			} else if (count > YellowThresholdPerMiniute) {// 黄色警戒，记2分
				punishValue = 2;
			} else if (count > BlueThresholdPerMiniute) {// 蓝色警戒，记1分
				punishValue = 1;
			}

			if (punishValue != 0) {
				this.addPunishRecord(ip, punishValue);
			}
		}

		this.handlePunish();
		this.monitorMap.clear();
	}

	/**
	 * 处理惩罚数据
	 */
	private void handlePunish() {
		for (String ip : this.punishMap.keySet()) {
			int punishValue = this.punishMap.get(ip);
			if (punishValue > 2) {// 红色1次，黄色2次，蓝色3次
				List<IRole> list = XsgRoleManager.getInstance().findRoleByRemoteAddress(ip);
				for (IRole role : list) {// 冻结帐号并T下线
					XsgCenterManager.instance().frozenAccount(
							role.getAccount(),
							TextUtil.format("[DDOS]{0}|{1}|{2}|{3}|{4}", role.getAccount(), role.getRoleId(),
									role.getName(), ip, punishValue));
					CenterI.instance().kick(role.getAccount(), role.getRoleId());
				}
			}
		}
	}

	/**
	 * 添加惩罚记录
	 * 
	 * @param ip
	 * @param punishValue
	 */
	private void addPunishRecord(String ip, int punishValue) {
		int old = this.punishMap.containsKey(ip) ? this.punishMap.get(ip) : 0;
		this.punishMap.put(ip, old + 1);
	}

	/**
	 * 接受一个客户端请求的处理逻辑
	 * 
	 * @param operation
	 * @param remoteAddress
	 */
	public void receiveRequest(String operation, String remoteAddress) {
		if (this.whiteList.contains(operation)) {// 白名单操作，直接放行，不计算次数
			return;
		}

		int old = this.monitorMap.containsKey(remoteAddress) ? this.monitorMap.get(remoteAddress) : 0;

		this.monitorMap.put(remoteAddress, old + 1);
	}
}

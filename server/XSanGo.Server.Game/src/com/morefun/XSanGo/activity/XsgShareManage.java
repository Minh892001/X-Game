package com.morefun.XSanGo.activity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.achieve.XsgAchieveManager.AchieveTemplate;
import com.morefun.XSanGo.activity.XsgActivityManage.ActivityTemplate;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 分享活动
 */
public class XsgShareManage {

	private static XsgShareManage instance = new XsgShareManage();

	public static XsgShareManage getInstance() {
		return instance;
	}

	/*
	 * 类型
	 */
	public enum ShareTemplate {
		KingLv, Power;
		public static ShareTemplate getShareTemplate(String type) {
			return valueOf(type);
		}

	}

	/**
	 * 功能参数
	 */
	private ShareOpenParaT openT;

	/**
	 * 任务API配置<类型,<ID,vo>>
	 */
	private Map<String, Map<Integer, ShareBaseTaskT>> taskBaseCfg4Target = new HashMap<String, Map<Integer, ShareBaseTaskT>>();

	/**
	 * 任务API配置<ID,vo>
	 */
	private Map<Integer, ShareBaseTaskT> taskBaseCfg4Id = new HashMap<Integer, ShareBaseTaskT>();

	/**
	 * 热加载相关分享配置<ID,vo>
	 */
	private Map<Integer, ShareConfigT> autoBaseCfg4Id = new HashMap<Integer, ShareConfigT>();

	/**
	 * 创建分享活动的控制模块
	 * 
	 * @param roleRt
	 * @param roleDB
	 * @return
	 */
	public ShareControler createShareControler(IRole roleRt, Role roleDB) {
		return new ShareControler(roleRt, roleDB);
	}

	private XsgShareManage() {
		loadTaskBaseCfg4Target();
		loadOpenT();
		loadAutoBaseCfg4Id();
	}

	/**
	 * 加载开启配置
	 */
	private void loadOpenT() {
		List<ShareOpenParaT> list = ExcelParser.parse(ShareOpenParaT.class);
		for (ShareOpenParaT t : list) {
			if (t != null) {
				openT = t;
				break;
			}
		}
	}

	/**
	 * 加载任务API配置
	 */
	private void loadTaskBaseCfg4Target() {
		List<ShareBaseTaskT> list = ExcelParser.parse(ShareBaseTaskT.class);
		taskBaseCfg4Target.clear();
		taskBaseCfg4Id.clear();
		for (ShareBaseTaskT t : list) {
			taskBaseCfg4Id.put(t.id, t);
			Map<Integer, ShareBaseTaskT> map = taskBaseCfg4Target.get(t.target);
			if (map == null) {
				map = new HashMap<Integer, ShareBaseTaskT>();
				map.put(t.id, t);
				taskBaseCfg4Target.put(t.target, map);
			} else {
				map.put(t.id, t);
			}
		}
	}

	/**
	 * 加载热加载配置
	 */
	public void loadAutoBaseCfg4Id() {
		List<ShareConfigT> list = ExcelParser.parse(ShareConfigT.class);
		autoBaseCfg4Id.clear();
		for (ShareConfigT t : list) {
			if (!TextUtil.isBlank(t.item1) && t.item1Num > 0)
				t.itemMap.put(t.item1, t.item1Num);
			if (!TextUtil.isBlank(t.item2) && t.item2Num > 0)
				t.itemMap.put(t.item2, t.item2Num);
			if (!TextUtil.isBlank(t.item3) && t.item3Num > 0)
				t.itemMap.put(t.item3, t.item3Num);
			autoBaseCfg4Id.put(t.taskId, t);
		}
	}

	/**
	 * 是否开启
	 * 
	 * @return
	 */
	public boolean isOpen() {
		ActivityT activityT = XsgActivityManage.getInstance().getActivityT(ActivityTemplate.Share);
		if (activityT == null)
			return false;
		boolean isBetween = DateUtil.isBetween(new Date(), DateUtil.parseDate(activityT.startTime),
				DateUtil.parseDate(activityT.endTime));
		if (!isBetween) {
			return false;
		}
		if (openT != null) {
			String serverId = ServerLancher.getServerId() + "";
			if ("0".equalsIgnoreCase(openT.serverIds))
				return true;
			if (openT.serverIds.indexOf(serverId) != -1)
				return true;
		}
		return false;
	}

	public ShareOpenParaT getOpenT() {
		return openT;
	}

	public Map<String, Map<Integer, ShareBaseTaskT>> getTaskBaseCfg4Target() {
		return taskBaseCfg4Target;
	}

	public Map<Integer, ShareBaseTaskT> getTaskBaseCfg4Id() {
		return taskBaseCfg4Id;
	}

	public Map<Integer, ShareConfigT> getAutoBaseCfg4Id() {
		return autoBaseCfg4Id;
	}
}

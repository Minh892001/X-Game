package com.morefun.XSanGo.friendsRecall;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleFriendsInvitation;
import com.morefun.XSanGo.db.game.RoleFriendsInvitationDao;
import com.morefun.XSanGo.db.game.RoleFriendsRecalled;
import com.morefun.XSanGo.db.game.RoleFriendsRecalledDao;
import com.morefun.XSanGo.db.game.SimpleDAO;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.CodeUtil;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 老友召回
 * 
 * @author zhangwei02.zhang
 * @since 2015年10月28日
 * @version 1.0
 */
public class XsgFriendsRecallManager {
	private static final Logger log = LoggerFactory.getLogger(XsgFriendsRecallManager.class);

	private static XsgFriendsRecallManager _instance = new XsgFriendsRecallManager();

	public static XsgFriendsRecallManager getInstance() {
		return _instance;
	}

	public XsgFriendsRecallManager() {
		loadFriendsRecallScript();
		initData();
	}

	/** 配置 */
	private FriendsRecallCfgT friendsRecallCfgT;

	/** 邀请任务配置 */
	private Map<String, TreeMap<Integer, FriendsRecallInviteTaskT>> inviteTaskTMap = new HashMap<String, TreeMap<Integer, FriendsRecallInviteTaskT>>();

	/** 回归任务配置, key：target，TreeMap key:demand */
	private Map<String, TreeMap<Integer, FriendsRecallTaskT>> recallTaskTMap = new HashMap<String, TreeMap<Integer, FriendsRecallTaskT>>();

	/** 所有任务配置，key:taskId */
	private Map<Integer, FriendsRecallBaseT> taskMap = new HashMap<Integer, FriendsRecallBaseT>();

	/** 符合条件时默认显示任务集合， key：task type */
	private Map<Integer, List<FriendsRecallBaseT>> initTask = new HashMap<Integer, List<FriendsRecallBaseT>>();

	/** 一次性任务的任务目标集合 */
	private List<String> taskTargetOnce = new ArrayList<String>();

	/** 邀请人与被召回人的对应，key：邀请人id， value:被邀请人id集合 */
	private Map<String, List<String>> recalledFriendsMap = new HashMap<String, List<String>>();

	/** 邀请码与角色关联， key:邀请码；value:邀请人id */
	private Map<String, String> invitationCodeRole = new HashMap<String, String>();

	/** 邀请任务中每个目标与可获得最大元宝数对应 */
	private Map<String, Integer> taskYuanbaoCountMap = new HashMap<String, Integer>();

	/** 随机获取的有回归资格的玩家id， key:自己的id，value:随机出的id */
	private Map<String, List<String>> randomRoleIdMap = new HashMap<String, List<String>>();

	/** 活动应该开启时间, 但不能用来判断是否开启，需要调用isOpen方法才能确定是否开启 */
	private Date openTime;

	/** 系统默认邀请码 */
	public final static String DEFAULT_INVITATION_CODE = "XSANGUOMF";

	/** 邀请任务类型 */
	public final static int TASK_TYPE_INVITATION = 9;

	/** 回归任务类型 */
	public final static int TASK_TYPE_RECALL = 8;

	/** 回归有礼，主公等级任务 */
	public final static String taskTarget8KingLv = "KingLv";

	/** 回归有礼，累计充值 */
	public final static String taskTarget8AccCharge = "Allpay";

	/** 回归有礼， 签到 */
	public final static String taskTarget8Sign = "Signmine";

	/** 邀请任务，被邀请玩家等级成长 */
	public final static String taskTarget9Growup = "Growup";

	/** 邀请任务，被邀请玩家累计充值 */
	public final static String taskTarget9AccCharge = "Cumulateyb";

	/** 邀请任务，被邀请玩家累计消费 */
	public final static String taskTarget9AccConsumption = "Allcost";

	/** 邀请任务，累计召回 */
	public final static String taskTarget9AccRecall = "Allcall";

	/** 邀请任务， 被邀请玩家签到 */
	public final static String taskTarget9Sign = "Signother";

	public FriendsRecallController createFriendsRecallController(IRole r, Role db) {
		return new FriendsRecallController(r, db);
	}

	/**
	 * 加载脚本
	 * 
	 */
	public void loadFriendsRecallScript() {
		taskTargetOnce.add(taskTarget8KingLv);
		taskTargetOnce.add(taskTarget8AccCharge);
		taskTargetOnce.add(taskTarget8Sign);
		taskTargetOnce.add(taskTarget9Growup);
		taskTargetOnce.add(taskTarget9AccConsumption);
		taskTargetOnce.add(taskTarget9AccRecall);

		// 参数配置
		List<FriendsRecallCfgT> list = ExcelParser.parse(FriendsRecallCfgT.class);
		if (list != null && list.size() > 0) {
			friendsRecallCfgT = list.get(0);
		}
		if (friendsRecallCfgT == null) {
			log.error("老友召回配置参数错误");
		}

		Map<Integer, Integer> tMap = new HashMap<Integer, Integer>();
		List<FriendsRecallBaseT> tList = null;

		// 回归有礼
		TreeMap<Integer, FriendsRecallTaskT> tmpMap = null;
		List<FriendsRecallTaskT> recallList = ExcelParser.parse(FriendsRecallTaskT.class);
		for (FriendsRecallTaskT rt : recallList) {
			tmpMap = recallTaskTMap.get(rt.target);
			if (tmpMap == null) {
				tmpMap = new TreeMap<Integer, FriendsRecallTaskT>();
				recallTaskTMap.put(rt.target, tmpMap);
			}
			tmpMap.put(rt.taskId, rt);
			if (rt.prefixTask > 0) {
				tMap.put(rt.prefixTask, rt.taskId);
			}

			taskMap.put(rt.taskId, rt);
			if (rt.prefixTask == 0 && taskTargetOnce.contains(rt.target)) {
				tList = initTask.get(rt.taskType);
				if (tList == null) {
					tList = new ArrayList<FriendsRecallBaseT>();
					initTask.put(rt.taskType, tList);
				}
				tList.add(rt);
			}
		}
		// 加上后置任务，方便操作
		for (FriendsRecallTaskT rt : recallList) {
			if (tMap.containsKey(rt.taskId)) {
				rt.suffixTask = tMap.get(rt.taskId);
			}
		}
		if (recallTaskTMap.size() == 0) {
			log.error("老友召回没有配置回归任务");
		}

		// 邀请任务
		tMap.clear();
		TreeMap<Integer, FriendsRecallInviteTaskT> tmp2Map = null;
		List<FriendsRecallInviteTaskT> inviteList = ExcelParser.parse(FriendsRecallInviteTaskT.class);
		for (FriendsRecallInviteTaskT rt : inviteList) {
			tmp2Map = inviteTaskTMap.get(rt.target);
			if (tmp2Map == null) {
				tmp2Map = new TreeMap<Integer, FriendsRecallInviteTaskT>();
				inviteTaskTMap.put(rt.target, tmp2Map);
			}
			tmp2Map.put(rt.taskId, rt);
			if (rt.prefixTask > 0) {
				tMap.put(rt.prefixTask, rt.taskId);
			}

			taskMap.put(rt.taskId, rt);
			if (rt.prefixTask == 0 && taskTargetOnce.contains(rt.target)) {
				tList = initTask.get(rt.taskType);
				if (tList == null) {
					tList = new ArrayList<FriendsRecallBaseT>();
					initTask.put(rt.taskType, tList);
				}
				tList.add(rt);
			}

			// 目标与元宝数之和对应
			Integer amount = taskYuanbaoCountMap.get(rt.target);
			if (amount == null) {
				amount = 0;
			}
			taskYuanbaoCountMap.put(rt.target, rt.rewardItemCount + amount);

		}
		// 加上后置任务，方便操作
		for (FriendsRecallInviteTaskT rt : inviteList) {
			if (tMap.containsKey(rt.taskId)) {
				rt.suffixTask = tMap.get(rt.taskId);
			}
		}
		if (inviteTaskTMap.size() == 0) {
			log.error("老友召回没有配置邀请任务");
		}
	}

	/**
	 * 初始化数据
	 * 
	 */
	private void initData() {
//		SimpleDAO simpleDAO = SimpleDAO.getFromApplicationContext(ServerLancher.getAc());

		// 创建邀请人与被召回人的关联
//		List<RoleFriendsRecalled> recallList = simpleDAO.findAll(RoleFriendsRecalled.class);
//		for (RoleFriendsRecalled fi : recallList) {
//			this.addRecalledFriends(fi.getInviteRoleId(), fi.getRoleId());
//		}
		RoleFriendsRecalledDao recallDao = RoleFriendsRecalledDao.getFromApplicationContext(ServerLancher.getAc());
		List<Object[]> recallList = recallDao.findRecalledFriends();
		for (Object[] fi : recallList) {
			this.addRecalledFriends(String.valueOf(fi[1]), String.valueOf(fi[0]));
		}

		// 创建邀请码与角色的关联
//		List<RoleFriendsInvitation> codes = simpleDAO.findAll(RoleFriendsInvitation.class);
//		for (RoleFriendsInvitation fi : codes) {
//			this.addInvitationCodeRole(fi.getRecallCode(), fi.getRoleId());
//		}
		RoleFriendsInvitationDao inviteDao = RoleFriendsInvitationDao.getFromApplicationContext(ServerLancher.getAc());
		List<Object[]> inviteList = inviteDao.findRoleInvitation();
		for (Object[] fi : inviteList) {
			this.addInvitationCodeRole(String.valueOf(fi[1]), String.valueOf(fi[0]));
		}

		// 活动应该开启时间
		Calendar c = Calendar.getInstance();
		c.setTime(GlobalDataManager.getInstance().getServerOpenTime());
		c.set(Calendar.HOUR_OF_DAY, 6);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		openTime = DateUtil.addDays(c, friendsRecallCfgT.requireDays).getTime();
	}

	/**
	 * 随机生成一个不重复的邀请码
	 * 
	 * @return
	 */
	public String generateInvitationCode() {
		String code = "";
		while (true) {
			code = "XSG" + CodeUtil.generateRandomCode(6, CodeUtil.CHARACTER_DIGITAL, 1).get(0)
					+ CodeUtil.generateRandomCode(2, CodeUtil.CHARACTER_ALPHA, 1).get(0);
			if (!this.getAllInvitationCode().contains(code)) {
				return code;
			}
		}
	}

	/**
	 * 活动是否开启
	 * 
	 * @return
	 */
	public boolean isOpened() {
		if (Calendar.getInstance().getTime().before(openTime)) {
			return false;
		}
		return true;
	}

	/**
	 * 是否显示活动图标
	 * 
	 * @return
	 */
	public boolean isShowIcon() {
		// 忽悠app store用，正常情况下活动是常开的
		if (friendsRecallCfgT.opened == 0) {
			return false;
		}
		return isOpened();
	}

	/**
	 * 是否符合召回资格
	 * 
	 * @param dbRole
	 * @return
	 */
	public String canRecall(Role dbRole) {
		if (!isOpened()) {
			return "活动尚未开启";
		}

		// // 老用户
		// if (this.openTime.after(dbRole.getCreateTime())) {

		// 等级不符合
		if (dbRole.getLevel() < this.friendsRecallCfgT.roleLevel) {
			return "等级不满足";
		}

		// 不符合离线天数
		if (dbRole.getRoleFriendsRecalled() == null) {
			return "未达到规定离线天数";
		}

		// }
		return null;
	}

	/**
	 * 获取给定邀请人的被召回角色id集合
	 * 
	 * @param roleId
	 * @return
	 */
	public List<String> getRecalledFriends(String roleId) {
		return recalledFriendsMap.get(roleId);
	}

	/**
	 * 新增一个被召回人的关联
	 * 
	 * @param roleId
	 * @param recalledId
	 */
	public void addRecalledFriends(String roleId, String recalledId) {
		// 忽略仅有召回资格和使用系统默认码召回的记录
		if (TextUtil.isBlank(roleId)) {
			return;
		}
		List<String> list = this.recalledFriendsMap.get(roleId);
		if (list == null) {
			list = new ArrayList<String>();
			this.recalledFriendsMap.put(roleId, list);
		}
		list.add(recalledId);
	}

	/**
	 * 根据邀请码获取所属的角色id
	 * 
	 * @return
	 */
	public String getRoleByInvitationCode(String invitationCode) {
		return invitationCodeRole.get(invitationCode);
	}

	/**
	 * 新增邀请码并与所属角色关联
	 * 
	 * @param invitationCode
	 * @param roleId
	 */
	public void addInvitationCodeRole(String invitationCode, String roleId) {
		this.invitationCodeRole.put(invitationCode, roleId);
	}

	/**
	 * 获取已生成的邀请码
	 * 
	 * @return
	 */
	public List<String> getAllInvitationCode() {
		return new ArrayList<String>(invitationCodeRole.keySet());
	}

	public Date getOpenTime() {
		return openTime;
	}

	public FriendsRecallCfgT getFriendsRecallCfgT() {
		return friendsRecallCfgT;
	}

	public TreeMap<Integer, FriendsRecallInviteTaskT> getInviteTaskTMap(String target) {
		return inviteTaskTMap.get(target);
	}

	public TreeMap<Integer, FriendsRecallTaskT> getRecallTaskTMap(String target) {
		return recallTaskTMap.get(target);
	}

	public Map<Integer, FriendsRecallBaseT> getTaskMap() {
		return taskMap;
	}

	public List<FriendsRecallBaseT> getInitTask(int type) {
		return initTask.get(type);
	}

	public int getTaskYuanbaoCount(String target) {
		Integer amount = taskYuanbaoCountMap.get(target);
		return amount == null ? 0 : amount;
	}

	public void removeRandomRole(String roleId){
		randomRoleIdMap.remove(roleId);
	}
	
	public List<String> getRandomRoleIdMap(String roleId) {
		return randomRoleIdMap.get(roleId);
	}

	public void setRandomRoleIdMap(String roleId, List<String> list) {
		this.randomRoleIdMap.put(roleId, list);
	}

}

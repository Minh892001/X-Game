package com.morefun.XSanGo.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.TaskType;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.db.game.RedPacket;
import com.morefun.XSanGo.db.game.RedPacketDao;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.SimpleDAO;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.DelayedTask;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

/**
 * 碎片的 合成、掠夺、复仇抢回碎片 全局管理
 * 
 * @author 吕明涛
 * 
 */
public class XsgTaskManager {
	private static XsgTaskManager instance = new XsgTaskManager();

	/**
	 * 任务完成状态 <br>
	 * unFinish:未完成 <br>
	 * obtain:可以领取任务 <br>
	 * outTime：过时未完成 <br>
	 * finish：完成
	 */
	enum taskStat {
		unFinish, obtain, outTime, finish
	};

	public enum SevenTargetTemplt {
		KingLv, HeroLv, EquipLv, BreakUp, HeroStar, SkillLv, Friends, JionGuid, GuildMedal, Visit, Harvest, ShopBuy, fortPass, ST, Expedition, PVPS, Fight, PVPRank, Craft, CraftRank;
		public static SevenTargetTemplt getSevenTargetTemplt(String type) {
			return valueOf(type);
		}

	}

	/** 主线过关 */
	public static final String FORT_PASS = "fortPass";
	/** 扫荡 */
	public static final String FORT_WIPE = "fortWipe";
	/** 首次获得英雄 */
	public static final String GET_HERO_FIRST = "getHeroFirst";

	/** 日常 */
	/** 日常过关 */
	public static final String FORT_PASS_NUM = "fortPassNum";
	/** 强化任意装备 */
	public static final String STRENGTHEN = "Strengthen";
	/** 升级任意武将技能 */
	public static final String SKILL_UP = "Skillup";
	/** 重铸 */
	public static final String RECASTING = "Recasting";
	/** 竞技场作战3次 */
	public static final String PVPS = "PVPS";
	/** 名将探访 */
	public static final String VISIT = "Visit";
	/** 时空战役 */
	public static final String ST = "ST";
	/** 月卡元宝 */
	public static final String VIPM = "VIPM";
	/** VIP福利 */
	public static final String VIP = "VIP";
	/** 群雄争霸作战3次 */
	public static final String LADDER = "Craft";
	/** 团队副本作战1次 */
	public static final String FACTION_COPY = "TeamPass";
	/** 向公会捐赠1枚公会奖章 */
	public static final String FACTION_DONATION = "GuildMedal";
	/** 北伐作战1次 */
	public static final String EXPEDITION = "Expedition";
	/** 签到抽奖 */
	public static final String DRAW_SIGN = "DrawSign";
	/** 商城0元购买 */
	public static final String FREE_BUY = "FreeBuy";
	/** 领俸禄 */
	public static final String PAYMENT = "Payment";
	/** 熔炼绿装 */
	public static final String SMELT = "Smelt";
	/***/
	public static final String RECHARGE = "Recharge";
	/** 手不释卷 */
	public static final String OVER = "Over";
	/** 当日送好友军令XXX */
	public static final String PRESENT = "Present";
	/** 当日友情点增加n */
	public static final String FRIENDPOINTUP = "FriendPointUp";
	/** 寻宝收获1次 */
	public static final String HARVEST = "Harvest";

	/**
	 * 成就 角色达到的等级 /
	 */
	public static final String KING_LV = "KingLv";
	/** 拥有一定数量武将 */
	public static final String HERO_NUM = "HeroNum";
	/** 累积 金币 */
	public static final String GOLD_NUM = "GoldNum";
	/** 任意一名武将 升星 */
	public static final String HERO_STAR = "HeroStar";
	/** 千杯不醉 抽卡 */
	public static final String DRAW_CARD = "DrawCard";

	// 新手引导
	/** 通关普通难度荥阳 */
	public static final String FORT_PASS_G = "fortPassG";
	/** 召唤李典 */
	public static final String SUMMON_HERO = "SummonHero";
	/** 领取升级奖励 */
	public static final String GET_REWARD = "GetReward";
	/** 给潘凤吃小经验丹 */
	public static final String USE_EXPG = "UseExpg";
	/** 给朱然技能加点 */
	public static final String SKILL_UP_G = "SkillupG";
	/** 给任意武将进阶 */
	public static final String ADVANCED_HERO = "AdvancedHero";
	/** 掠夺桃木簪(3) */
	public static final String ROB_PIECES = "RobPieces";
	/** 掠夺桃木簪(3)，并合成后炫耀 */
	public static final String COMPOSE_SHOW = "ComposeShow";
	/** 完成竞技场 */
	public static final String ARENA = "Arena";
	/** 完成竞技场 */
	public static final String SMELT_BLUE = "SmeltBlue";
	/** 完成竞技场 */
	public static final String EXCHANGE_BLUE = "ExchangeBlue";
	/**
	 * 点金手
	 */
	public static final String BUY_JINBI = "ToGold";
	/**
	 * 切磋
	 */
	public static final String FIGHT = "Fight";
	/**
	 * 武将升级
	 * */
	public static final String HeroLvUp = "HeroLvUp";
	/** 友情点达到一定数量 */
	public static final String FriendPoint = "FriendPoint";

	/**救援好友5次*/
	public static final String TreasureHelpFriends = "TreasureHelpFriends";

	/**捐献1次任意科技*/
	public static final String GuildDonateTec = "GuildDonateTec";
	
	/**购买1次军令*/
	public static final String BuyMed = "BuyMed";
	
	/**聚贤庄宴请或购买1次*/
	public static final String RecruitBuyOne = "RecruitBuyOne";

	/**
	 * 全局红包开启状态
	 */
	public static final int OPEN = 1;

	// 聊天的模板信息 和 角色任务初始化数据
	public Map<Integer, TaskTemplT> taskTemplMap = new HashMap<Integer, TaskTemplT>();
	public List<TaskTemplT> taskTemplInitList = new ArrayList<TaskTemplT>();
	// 类型是主线、成就前置任务和引导任务
	private Map<Integer, TaskTemplT> TaskThroughMap_t1 = new HashMap<Integer, TaskTemplT>();
	private Map<Integer, TaskTemplT> TaskThroughMap_t3 = new HashMap<Integer, TaskTemplT>();
	private Map<Integer, TaskTemplT> TaskThroughMap_t6 = new HashMap<Integer, TaskTemplT>();

	// 任务点配置表
	private Map<Integer, TaskActivePointTemplT> activeAwardMap = new HashMap<Integer, TaskActivePointTemplT>();
	// 任务目标 过关任务
	public Map<String, Map<String, TaskTemplT>> targetPassMap = new HashMap<String, Map<String, TaskTemplT>>();
	// 任务目标 其他任务
	public Map<String, List<TaskTemplT>> targetMap = new HashMap<String, List<TaskTemplT>>();
	// vip升级红包
	public Map<Integer, RedPacketT> vipUpT = new HashMap<Integer, RedPacketT>();
	// 购买vip礼包红包
	public Map<Integer, RedPacketT> vipGiftT = new HashMap<Integer, RedPacketT>();
	// 武将进阶红包
	public Map<Integer, RedPacketT> heroAdvanceT = new HashMap<Integer, RedPacketT>();
	// 大神章节通过红包
	public Map<Integer, RedPacketT> tongGuanT = new HashMap<Integer, RedPacketT>();
	// 七日任务模板数据<第几天，<ID,VO>>
	public TreeMap<Integer, TreeMap<Integer, SevenTaskTemplT>> sevenTaskT = new TreeMap<Integer, TreeMap<Integer, SevenTaskTemplT>>();
	// 七日任务模板数据<TYPE，<ID,VO>>
	public Map<String, Map<Integer, SevenTaskTemplT>> sevenTaskDetailT = new HashMap<String, Map<Integer, SevenTaskTemplT>>();

	public Map<Integer, SevenTaskTemplT> sevenTaskBaseT = new HashMap<Integer, SevenTaskTemplT>();
	// 三星奖励
	public Map<Integer, ThreeStarTemplT> threeStarT = new HashMap<Integer, ThreeStarTemplT>();
	// 星级奖励<星的数量,vo>
	public TreeMap<Integer, StarAwardTemplT> starAwardT = new TreeMap<Integer, StarAwardTemplT>();
	/**
	 * 全局红包
	 */
	public List<RedPacket> redPacketSystem = new ArrayList<RedPacket>();

	public static XsgTaskManager getInstance() {
		return instance;
	}

	/**
	 * 创建任务的控制模块
	 * 
	 * @param roleRt
	 * @param roleDB
	 * @return
	 */
	public TaskControler createTaskControler(IRole roleRt, Role roleDB) {
		return new TaskControler(roleRt, roleDB);
	}

	private XsgTaskManager() {
		loadTaskPointAwardInfo();
		loadSevenTaskInfo();
		loadThreeStarAwardInfo();
		loadStartAwardInfo();
		List<TaskTemplT> taskTemplList = ExcelParser.parse("主线", TaskTemplT.class);
		taskTemplList.addAll(ExcelParser.parse("日常", TaskTemplT.class));
		// taskTemplList.addAll(ExcelParser.parse("成就", TaskTemplT.class));
		for (TaskTemplT templ : taskTemplList) {
			taskTemplMap.put(templ.id, templ);

			// 无前置任务，默认为任务的初始化数据
			if (templ.through == 0 && templ.type != TaskType.redPacket.ordinal()) {
				taskTemplInitList.add(templ);
			}

			// 初始化前置任务
			if (templ.through != 0) {
				if (templ.type == 1) {
					TaskThroughMap_t1.put(templ.through, templ);
				}
				if (templ.type == 3) {
					TaskThroughMap_t3.put(templ.through, templ);
				}
				if (templ.type == 6) {
					TaskThroughMap_t6.put(templ.through, templ);
				}
			}

			this.setTargetPassMap(templ);
		}
		List<RedPacketT> redPacketTs = ExcelParser.parse("充值VIP", RedPacketT.class);
		for (RedPacketT rp : redPacketTs) {
			vipUpT.put(rp.id, rp);
		}

		redPacketTs = ExcelParser.parse("武将进阶", RedPacketT.class);
		for (RedPacketT rp : redPacketTs) {
			heroAdvanceT.put(rp.id, rp);
		}

		redPacketTs = ExcelParser.parse("购买VIP礼包", RedPacketT.class);
		for (RedPacketT rp : redPacketTs) {
			vipGiftT.put(rp.id, rp);
		}

		redPacketTs = ExcelParser.parse("通关大神章", RedPacketT.class);
		for (RedPacketT rp : redPacketTs) {
			tongGuanT.put(rp.id, rp);
		}
		// 初始化全局红包
		SimpleDAO dao = SimpleDAO.getFromApplicationContext(ServerLancher.getAc());
		this.redPacketSystem = dao.findAll(RedPacket.class);
		// 每天凌晨检查清除所有数据
		LogicThread.scheduleTask(new DelayedTask(DateUtil.betweenTaskHourMillis(0), TimeUnit.DAYS.toMillis(1)) {
			@Override
			public void run() {
				redPacketSystem.clear();
				// DB删除
				DBThreads.execute(new Runnable() {
					@Override
					public void run() {
						// 删除全局红包
						RedPacketDao dao = RedPacketDao.getFromApplicationContext(ServerLancher.getAc());
						dao.deletePass();
					}
				});
			}
		});
	}

	/**
	 * 加载任务活跃点信息
	 */
	private void loadTaskPointAwardInfo() {
		List<TaskActivePointTemplT> taskPointList = ExcelParser.parse("活跃奖励", TaskActivePointTemplT.class);
		for (TaskActivePointTemplT t : taskPointList)
			activeAwardMap.put(t.id, t);
	}

	/**
	 * 加载七日目标信息
	 */
	private void loadSevenTaskInfo() {
		sevenTaskT.clear();
		sevenTaskDetailT.clear();
		sevenTaskBaseT.clear();
		List<SevenTaskTemplT> list = ExcelParser.parse("七日目标", SevenTaskTemplT.class);
		for (SevenTaskTemplT t : list) {
			sevenTaskBaseT.put(t.id, t);
			TreeMap<Integer, SevenTaskTemplT> map = sevenTaskT.get(t.day);
			Map<Integer, SevenTaskTemplT> detailMap = sevenTaskDetailT.get(t.target);
			if (null == map) {
				map = new TreeMap<Integer, SevenTaskTemplT>();
				map.put(t.id, t);
				sevenTaskT.put(t.day, map);
			} else {
				sevenTaskT.get(t.day).put(t.id, t);
			}
			if (null == detailMap) {
				detailMap = new HashMap<Integer, SevenTaskTemplT>();
				detailMap.put(t.id, t);
				sevenTaskDetailT.put(t.target, detailMap);
			} else {
				sevenTaskDetailT.get(t.target).put(t.id, t);
			}

		}
	}

	/**
	 * 加载七日三星奖励信息
	 */
	private void loadThreeStarAwardInfo() {
		threeStarT.clear();
		List<ThreeStarTemplT> list = ExcelParser.parse("七日三星奖励", ThreeStarTemplT.class);
		for (ThreeStarTemplT t : list) {
			threeStarT.put(t.day, t);
		}
	}

	/**
	 * 加载七七日目标星级奖励信息
	 */
	private void loadStartAwardInfo() {
		List<StarAwardTemplT> list = ExcelParser.parse("七日目标星级奖励", StarAwardTemplT.class);
		for (StarAwardTemplT t : list) {
			String[] awardInfos = t.reawad.split(";");
			for (int i = 0; i < awardInfos.length; i++) {
				String item = awardInfos[i].split(",")[0];
				int num = Integer.valueOf(awardInfos[i].split(",")[1]);
				t.award.put(item, num);
			}
			starAwardT.put(t.starNum, t);
		}

	}

	// 配置 任务目标
	private void setTargetPassMap(TaskTemplT templ) {
		// 任务过关 相关数据
		if (templ.target.equals(XsgTaskManager.FORT_PASS) || templ.target.equals(XsgTaskManager.FORT_PASS_G)) {

			Map<String, TaskTemplT> targetMap_sub = this.targetPassMap.get(templ.target);
			if (targetMap_sub == null) {
				targetMap_sub = new HashMap<String, TaskTemplT>();
			}

			targetMap_sub.put(templ.demand.split(",")[0], templ);
			this.targetPassMap.put(templ.target, targetMap_sub);
		} else {
			List<TaskTemplT> targetList = this.targetMap.get(templ.target);
			if (targetList == null) {
				targetList = new ArrayList<TaskTemplT>();
			}

			targetList.add(templ);
			targetMap.put(templ.target, targetList);
		}
	}

	public TaskTemplT getTaskThroughMap_t1(int taskThroughId) {
		return TaskThroughMap_t1.get(taskThroughId);
	}

	public TaskTemplT getTaskThroughMap_t3(int taskThroughId) {
		return TaskThroughMap_t3.get(taskThroughId);
	}

	public TaskTemplT getTaskThroughMap_t6(int taskThroughId) {
		return TaskThroughMap_t6.get(taskThroughId);
	}

	public Map<Integer, TaskActivePointTemplT> getActiveAwardMap() {
		return activeAwardMap;
	}

	public void setActiveAwardMap(Map<Integer, TaskActivePointTemplT> activeAwardMap) {
		this.activeAwardMap = activeAwardMap;
	}

	/**
	 * 七日星级奖励的转换 只适用于七日
	 * 
	 * @param awardInfo
	 * @return
	 */
	public IntString[] getIntString(String awardInfo) {
		IntString[] reward = null;
		String[] awards = awardInfo.split(";");
		reward = new IntString[awards.length];
		for (int i = 0; i < awards.length; i++) {
			String item = awards[i].split(",")[0];
			int num = Integer.valueOf(awards[i].split(",")[1]);
			reward[i] = new IntString(num, item);
		}
		return reward;
	}

	/**
	 * 七日三星星级奖励的转换
	 * 
	 * @param awardInfo
	 * @return
	 */
	public IntString[] getIntString(ThreeStarTemplT t) {
		IntString[] reward = new IntString[3];
		reward[0] = new IntString(t.itemNum1, t.item1);
		reward[1] = new IntString(t.itemNum2, t.item2);
		reward[2] = new IntString(t.itemNum3, t.item3);

		return reward;
	}
}

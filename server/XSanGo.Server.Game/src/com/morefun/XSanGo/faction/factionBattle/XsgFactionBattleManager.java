/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: XsgFactionBattleManager
 * 功能描述：
 * 文件名：XsgFactionBattleManager.java
 **************************************************
 */
package com.morefun.XSanGo.faction.factionBattle;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.persistence.Table;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.XSanGo.Protocol.ChatChannel;
import com.XSanGo.Protocol.ChatRole;
import com.XSanGo.Protocol.FactionBattlePersonalRankResultView;
import com.XSanGo.Protocol.FactionBattlePersonalRankView;
import com.XSanGo.Protocol.FactionBattleRankAwardView;
import com.XSanGo.Protocol.FactionBattleRankResultView;
import com.XSanGo.Protocol.FactionBattleRankView;
import com.XSanGo.Protocol.FactionCallBackPrx;
import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.StrongHoldState;
import com.XSanGo.Protocol.TextMessage;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.chat.ChatAdT;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.chat.XsgChatManager.AdContentType;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.db.game.FactionBattle;
import com.morefun.XSanGo.db.game.FactionBattleConfig;
import com.morefun.XSanGo.db.game.FactionBattleDAO;
import com.morefun.XSanGo.db.game.FactionBattleEvent;
import com.morefun.XSanGo.db.game.FactionBattleEventRatio;
import com.morefun.XSanGo.db.game.FactionBattleLog;
import com.morefun.XSanGo.db.game.FactionBattleMember;
import com.morefun.XSanGo.db.game.FactionBattleRobot;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleDAO;
import com.morefun.XSanGo.db.game.SimpleDAO;
import com.morefun.XSanGo.faction.IFaction;
import com.morefun.XSanGo.faction.XsgFactionManager;
import com.morefun.XSanGo.hero.HeroT;
import com.morefun.XSanGo.hero.XsgHeroManager;
import com.morefun.XSanGo.item.AbsItemT;
import com.morefun.XSanGo.item.NormalItemT;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.reward.TcResult;
import com.morefun.XSanGo.reward.XsgRewardManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 公会战数据管理
 * 
 * @author zwy
 * @since 2016-1-5
 * @version 1.0
 */
public class XsgFactionBattleManager {

	private static XsgFactionBattleManager instance = new XsgFactionBattleManager();

	private static final Logger log = LoggerFactory.getLogger(XsgFactionBattleManager.class);

	/** 据点类型:大本营 */
	public final static byte BASE_CAMP = 1;

	/** 粮草 */
	public final static String FORAGE = "army";

	/** 徽章 */
	public final static String BADGE = "g_badge";

	/** 公会战基本配置 */
	private FactionBattleBaseT battleBaseT;

	/** 场景数据 */
	private Map<Integer, FactionBattleSceneT> battleScenes = new HashMap<Integer, FactionBattleSceneT>();

	/** 各类型据点列表 */
	private Map<Byte, List<Integer>> sceneIdList = new HashMap<Byte, List<Integer>>();

	/** 阵营数据 */
	private int[] camps;

	/** 据点负载 */
	private Map<Byte, FactionBattleStrongholdT> battleStrongholds = new HashMap<Byte, FactionBattleStrongholdT>();

	/** 锦囊道具 */
	private Map<Integer, FactionBattleKitsT> battleKitses = new TreeMap<Integer, FactionBattleKitsT>();

	/** 锦囊权值列表 */
	private int[] kitsWeights;

	/** 锦囊获取提示 */
	private Map<Byte, List<String>> battleKitsMessages = new HashMap<Byte, List<String>>();

	/** 挖宝掉落 */
	private Map<Byte, FactionBattleDiggingTreasureT> battleDiggingTreasures = new HashMap<Byte, FactionBattleDiggingTreasureT>();

	/** 消息 */
	private Map<String, FactionBattleMessageT> battleMessages = new HashMap<String, FactionBattleMessageT>();

	/** 排行奖励 <名次> */
	private Map<Integer, FactionBattleRankAwardT> battleRankAwards = new TreeMap<Integer, FactionBattleRankAwardT>();

	/** 排行奖励协议数据,预先生成,提高查看时的效率 */
	private FactionBattleRankAwardView[] rankAwardViews;

	/** 加成奖励 */
	private Map<Integer, FactionBattleAddAwardT> battleAddAwards = new HashMap<Integer, FactionBattleAddAwardT>();

	/** 最高有效连杀 */
	private int maxEvenKillNum = 0;

	/** 随机事件 */
	private Map<Integer, FactionBattleRandomEventT> eventTs = new TreeMap<Integer, FactionBattleRandomEventT>();

	/** 随机事件权值 */
	private int[] eventWeights;

	/** DEBUFF数据 */
	private Map<Integer, FactionBattleDebuffT> debuffs = new HashMap<Integer, FactionBattleDebuffT>();

	/** 个人排行奖励 */
	private List<FactionBattlePersonalRankAwardT> personalRankListTs;

	/** 个人排行奖励配置 */
	private Map<Integer, Integer> personalRankTs = new HashMap<Integer, Integer>();

	/**************************************** 以下运行时数据需要考虑及时清理 ****************************************/
	/** 公会战运行时参数 */
	private FactionBattleConfig runTimeConfig;

	/** 公会战报名参战的公会，下次开启清理 */
	private List<String> joinFactionList = new ArrayList<String>();

	/** 参战公会排行，下次开启清理 */
	private List<FactionBattle> rankList = new ArrayList<FactionBattle>();

	/** 参战各个公会的个人排行，下次开启清理 */
	private Map<String, List<FactionBattleMember>> personalRankList = new HashMap<String, List<FactionBattleMember>>();

	/**************************************** 公会战关闭清理的数据 ****************************************/
	/** 所有参战的角色列表 */
	private List<String> joinRoleList = new ArrayList<String>();

	/** 入驻的据点信息 */
	private Map<Integer, StrongHold> strongholdDatas = new HashMap<Integer, StrongHold>();

	/** 随机事件DB */
	private FactionBattleEvent randomEvent;

	/** 公会日志DB */
	private Map<String, List<FactionBattleLog>> factionBattleLogs = new HashMap<String, List<FactionBattleLog>>();

	/** 随机事件各个玩家的随机次数DB */
	private Map<Integer, Map<String, FactionBattleEventRatio>> factionBattleEventRatios = new HashMap<Integer, Map<String, FactionBattleEventRatio>>();

	/**
	 * 据点机器人缓存
	 */
	private List<FactionBattleRobot> battleRobotList = new ArrayList<FactionBattleRobot>();

	/**
	 * 据点机器人占领时间
	 */
	private Map<Integer, Long> refreshRobotTime = new HashMap<Integer, Long>();

	/**
	 * 机器人占领后发送滚动公告时间 5分钟发一次
	 */
	private Date refreshRobotDate;

	/***
	 * 注： 1、joinRoleList、strongholdDatas、randomEvent、factionBattleLogs、
	 * factionBattleEventRatios公会战关闭时立即清理相关数据，其中后面3个需要考虑在非正常关闭时，DB的处理情况
	 * 2、joinFactionList、rankList、personalRankList务必在下次开始报名之前进行数据清理
	 * **/

	private XsgFactionBattleManager() {
		loadFactionBattleScript();
		initFactionBattleData();
		startFactionBattle();
	}

	public static XsgFactionBattleManager getInstance() {
		return instance;
	}

	/**
	 * 加载公会战相关配置
	 */
	private void loadFactionBattleScript() {
		this.rankAwardViews = null;
		// 加载公会战基本数据
		loadFactionBattleBaseT();
		// 加载据点场景数据
		loadFactionBattleSceneT();
		// 加载据点负载数据
		loadFactionBattleStrongholdT();
		// 加载锦囊数据
		loadFactionBattleKitsT();
		// 加载锦囊消息数据
		loadFactionBattleKitsMsgT();
		// 加载挖宝数据
		loadFactionBattleDiggingTreasureT();
		// 加载公会战消息表
		loadFactionBattleMessageT();
		// 加载公会战排行奖励
		loadFactionBattleRankAwardT();
		// 加载公会战加成奖励
		loadFactionBattleAddAwardT();
		// 加载事件表
		loadFactionBattleRandomEventT();
		// 加载debuff
		loadFactionBattleDebuffT();
		// 加载个人排行奖励
		loadFactionBattlePersonalRankAwardT();
	}

	/**
	 * 加载公会战基本数据
	 */
	private void loadFactionBattleBaseT() {
		List<FactionBattleBaseT> list = ExcelParser.parse(FactionBattleBaseT.class);
		if (list != null && !list.isEmpty()) {
			this.battleBaseT = list.get(0);
		}
	}

	/**
	 * 加载据点场景数据
	 */
	private void loadFactionBattleSceneT() {
		List<FactionBattleSceneT> listScene = ExcelParser.parse(FactionBattleSceneT.class);
		if (listScene != null) {
			for (FactionBattleSceneT t : listScene) {
				if (t.strongholdId > 0) {
					if (t.type == BASE_CAMP) {
						camps = ArrayUtils.add(camps, t.strongholdId);
					} else {
						List<Integer> idList = this.sceneIdList.get(t.type);
						if (idList == null) {
							idList = new ArrayList<Integer>();
							this.sceneIdList.put(t.type, idList);
						}
						idList.add(t.strongholdId);
					}
					this.battleScenes.put(t.strongholdId, t);
				}
			}
		}
		if (this.camps == null) {
			log.error("campList Can't be empty !!!!!!!!!!");
		}
	}

	/**
	 * 加载据点负载数据
	 */
	private void loadFactionBattleStrongholdT() {
		List<FactionBattleStrongholdT> listStronghold = ExcelParser.parse(FactionBattleStrongholdT.class);
		if (listStronghold != null) {
			for (FactionBattleStrongholdT t : listStronghold) {
				if (t.type > 0) {
					this.battleStrongholds.put(t.type, t);
				}
			}
		}
	}

	/**
	 * 加载锦囊数据
	 */
	private void loadFactionBattleKitsT() {
		this.kitsWeights = null;
		List<FactionBattleKitsT> listKits = ExcelParser.parse(FactionBattleKitsT.class);
		if (listKits != null) {
			for (FactionBattleKitsT t : listKits) {
				if (t.kitsId > 0) {
					this.battleKitses.put(t.kitsId, t);
					kitsWeights = ArrayUtils.add(kitsWeights, t.weight);
				}
			}
		}
	}

	/**
	 * 加载锦囊消息数据
	 */
	private void loadFactionBattleKitsMsgT() {
		List<FactionBattleKitsMsgT> listKitsMsg = ExcelParser.parse(FactionBattleKitsMsgT.class);
		if (listKitsMsg != null) {
			for (FactionBattleKitsMsgT t : listKitsMsg) {
				if (t.type > 0) {
					List<String> msgs = this.battleKitsMessages.get(t.type);
					if (msgs == null) {
						msgs = new ArrayList<String>();
						this.battleKitsMessages.put(t.type, msgs);
					}
					msgs.add(t.content);
				}
			}
		}
	}

	/**
	 * 加载挖宝数据
	 */
	private void loadFactionBattleDiggingTreasureT() {
		List<FactionBattleDiggingTreasureT> listDT = ExcelParser.parse(FactionBattleDiggingTreasureT.class);
		if (listDT != null) {
			for (FactionBattleDiggingTreasureT t : listDT) {
				if (t.type > 0) {
					this.battleDiggingTreasures.put(t.type, t);
				}
			}
		}
	}

	/**
	 * 加载公会战消息表
	 */
	private void loadFactionBattleMessageT() {
		List<FactionBattleMessageT> listMessage = ExcelParser.parse(FactionBattleMessageT.class);
		if (listMessage != null) {
			for (FactionBattleMessageT t : listMessage) {
				if (TextUtil.isNotBlank(t.msgId)) {
					this.battleMessages.put(t.msgId, t);
				}
			}
		}
	}

	/**
	 * 加载公会战排行奖励
	 */
	private void loadFactionBattleRankAwardT() {
		List<FactionBattleRankAwardT> listRank = ExcelParser.parse(FactionBattleRankAwardT.class);
		if (listRank != null) {
			for (FactionBattleRankAwardT t : listRank) {
				if (t.id <= 0) {
					continue;
				}
				String[] ranks = StringUtils.split(t.rank, ",");
				int startRank = Integer.parseInt(ranks[0]);
				int endRank = ranks.length > 1 ? Integer.parseInt(ranks[1]) : startRank;
				for (int rank = startRank; rank <= endRank; rank++) {
					this.battleRankAwards.put(rank, t);
				}
				// 生成协议数据
				String[] items = StringUtils.split(t.items, ",");
				IntString[] intStrings = new IntString[items.length];
				for (int i = 0; i < items.length; i++) {
					String[] item = items[i].split(":");
					intStrings[i] = new IntString(Integer.parseInt(item[1]), item[0]);
				}
				rankAwardViews = (FactionBattleRankAwardView[]) ArrayUtils.add(rankAwardViews,
						new FactionBattleRankAwardView(t.rank, intStrings));
			}
		}
	}

	/**
	 * 加载公会战加成奖励
	 */
	private void loadFactionBattleAddAwardT() {
		List<FactionBattleAddAwardT> listAddAward = ExcelParser.parse(FactionBattleAddAwardT.class);
		if (listAddAward != null) {
			for (FactionBattleAddAwardT t : listAddAward) {
				this.battleAddAwards.put(t.num, t);
				if (t.num > maxEvenKillNum) {
					maxEvenKillNum = t.num;
				}
			}
		}
	}

	/**
	 * 加载事件表
	 */
	private void loadFactionBattleRandomEventT() {
		this.eventTs.clear();
		this.eventWeights = null;
		List<FactionBattleRandomEventT> listEvents = ExcelParser.parse(FactionBattleRandomEventT.class);
		if (listEvents != null) {
			for (FactionBattleRandomEventT t : listEvents) {
				if (t.eventId > 0 && t.eventType != 2 && t.isOpen == 1) {
					this.eventTs.put(t.eventId, t);
					this.eventWeights = ArrayUtils.add(this.eventWeights, t.weight);
				}
			}
		}
	}

	/**
	 * 加载debuff
	 */
	private void loadFactionBattleDebuffT() {
		List<FactionBattleDebuffT> list = ExcelParser.parse(FactionBattleDebuffT.class);
		if (list != null) {
			for (FactionBattleDebuffT t : list) {
				if (t.debuffLvl > 0) {
					this.debuffs.put(t.debuffLvl, t);
				}
			}
		}
	}

	/**
	 * 加载个人排行奖励
	 */
	private void loadFactionBattlePersonalRankAwardT() {
		this.personalRankListTs = ExcelParser.parse(FactionBattlePersonalRankAwardT.class);
		for (FactionBattlePersonalRankAwardT t : personalRankListTs) {
			String[] ranks = StringUtils.split(t.rank, "-");
			int startRank = Integer.parseInt(ranks[0]);
			int endRank = ranks.length > 1 ? Integer.parseInt(ranks[1]) : startRank;
			for (int rank = startRank; rank <= endRank; rank++) {
				this.personalRankTs.put(rank, t.scale);
			}
		}
	}

	/**
	 * 初始化公会战参战数据
	 */
	private void initFactionBattleData() {
		// 初始化公会战运行时参数
		SimpleDAO simpleDao = SimpleDAO.getFromApplicationContext(ServerLancher.getAc());
		List<FactionBattleConfig> list = simpleDao.findAll(FactionBattleConfig.class);
		if (!list.isEmpty()) {
			this.runTimeConfig = list.get(0);
		}

		FactionBattleDAO factionBattleDao = FactionBattleDAO.getFromApplicationContext(ServerLancher.getAc());
		boolean isOpen = false;
		if (isFactionBattleOpenTime()) {// 在开战周期
			isOpen = true;
		}
		// 加载公会战公会排行数据和个人排行榜数据
		List<FactionBattle> factionBattleList = factionBattleDao.findAllFactionBattle();
		for (FactionBattle fb : factionBattleList) {
			IFaction faction = XsgFactionManager.getInstance().findFaction(fb.getFactionId());
			this.joinFactionList.add(fb.getFactionId());
			if (fb.getBadge() > 0) {// 获得徽章
				rankList.add(faction.getFactionBattle());
			}
			// 初始化个人排行榜数据
			Map<String, FactionBattleMember> fbms = faction.getAllFactionBattleMember();
			for (FactionBattleMember fbm : fbms.values()) {
				if (fbm.getBadge() > 0) {// 获得徽章
					setPersonalRankList(fbm);
				}
			}
		}
		// 排行榜排序
		sortFactionBattleRank();
		factionBattleList = null;

		// 初始化开战过程中的数据
		if (isOpen) {
			List<FactionBattleEvent> factionBattleEventList = factionBattleDao.findAllFactionBattleEvent();
			if (!factionBattleEventList.isEmpty()) {
				this.randomEvent = factionBattleEventList.get(0);
			}
			List<FactionBattleEventRatio> factionBattleEventRatioList = factionBattleDao
					.findAllFactionBattleEventRatio();
			for (FactionBattleEventRatio fber : factionBattleEventRatioList) {
				addEventRatio(fber);
			}

			// 公会日志
			List<FactionBattleLog> logList = factionBattleDao.findAllFactionBattleLog();
			for (FactionBattleLog log : logList) {
				List<FactionBattleLog> logs = factionBattleLogs.get(log.getRole_id());
				if (logs == null) {
					logs = new ArrayList<FactionBattleLog>();
					factionBattleLogs.put(log.getRole_id(), logs);
				}
				logs.add(log);
			}
			// 公会战机器人数据
			List<FactionBattleRobot> robotList = simpleDao.findAll(FactionBattleRobot.class);
			for (FactionBattleSceneT t : battleScenes.values()) {
				StrongHold sh = new StrongHold();
				strongholdDatas.put(t.strongholdId, sh);
			}
			for (FactionBattleRobot robot : robotList) {
				StrongHold sh = strongholdDatas.get(robot.getStrongholdId());
				FactionBattleSceneT t = battleScenes.get(robot.getStrongholdId());
				String[] robotNames = StringUtils.split(t.monsterName, ",");
				sh.addDefendRobotList(robot, robotNames[NumberUtil.random(robotNames.length)]);
			}
		}
	}

	/**
	 * 排行榜排序
	 */
	private void sortFactionBattleRank() {
		Collections.sort(rankList, new Comparator<FactionBattle>() {
			@Override
			public int compare(FactionBattle o1, FactionBattle o2) {
				if (o1.getBadge() > o2.getBadge()) {
					return -1;
				}
				if (o1.getBadge() < o2.getBadge()) {
					return 1;
				}
				// if (o1.getKillNum() > o2.getKillNum()) {
				// return -1;
				// }
				// if (o1.getKillNum() < o2.getKillNum()) {
				// return 1;
				// }
				if (o1.getUpdateTime().getTime() < o2.getUpdateTime().getTime()) {
					return -1;
				}
				if (o1.getUpdateTime().getTime() > o2.getUpdateTime().getTime()) {
					return 1;
				}
				return 0;
			}
		});
		if (this.rankList.size() > this.battleBaseT.maxRankSize) {
			this.rankList.remove(this.rankList.size() - 1);
		}
	}

	/**
	 * 个人排行榜排序
	 */
	private void sortFactionBattleMemberRank(List<FactionBattleMember> personalRankList) {
		Collections.sort(personalRankList, new Comparator<FactionBattleMember>() {
			@Override
			public int compare(FactionBattleMember o1, FactionBattleMember o2) {
				if (o1.getBadge() > o2.getBadge()) {
					return -1;
				}
				if (o1.getBadge() < o2.getBadge()) {
					return 1;
				}
				if (o1.getUpdateTime().getTime() < o2.getUpdateTime().getTime()) {
					return -1;
				}
				if (o1.getUpdateTime().getTime() > o2.getUpdateTime().getTime()) {
					return 1;
				}
				return 0;
			}
		});
		if (this.personalRankList.size() > this.battleBaseT.maxPersonalRankSize) {
			this.personalRankList.remove(this.personalRankList.size() - 1);
		}
	}

	/**
	 * 开启公会战定时器
	 */
	private void startFactionBattle() {
		if (this.battleBaseT == null) {
			return;
		}
		List<String> openWeekDay = TextUtil.stringToList(this.battleBaseT.openWeek);
		// 公会战报名处理
		startFactionBattleEnrollTimer(openWeekDay);
		// 公会战开战处理
		startFactionBattleOpenTimer(openWeekDay);
		robotAutoOccupy();
		// 公会战关闭处理
		startFactionBattleCloseTimer(openWeekDay);
		// 公会战随机事件处理
		doFactionBattleRandomEvent();
	}

	/**
	 * 机器人自动占领空据点
	 */
	private void robotAutoOccupy() {
		final Calendar startDate = Calendar.getInstance();
		startDate.setTime(this.battleBaseT.openTime);

		// 计算延迟时间
		long delayTime = DateUtil.betweenTaskHourMillis(startDate.get(Calendar.HOUR_OF_DAY),
				startDate.get(Calendar.MINUTE));
		// 指定时间点执行，并开启每天一次
		LogicThread.scheduleTask(new DelayedTask(delayTime, this.battleBaseT.robotCheckInterval * 1000) {
			@Override
			public void run() {
				if (strongholdDatas.isEmpty()) {
					return;
				}

				List<Integer> notifyIds = new ArrayList<Integer>();
				for (Entry<Integer, StrongHold> entry : strongholdDatas.entrySet()) {
					FactionBattleSceneT sceneT = getBattleSceneT(entry.getKey());
					if (sceneT.type == BASE_CAMP) {
						continue;
					}
					Long time = refreshRobotTime.get(entry.getKey());
					if (entry.getValue().isCanOccupy()
							&& (time == null || System.currentTimeMillis() - time > sceneT.robotOccupySecond * 1000)) {
						String[] robotNames = StringUtils.split(sceneT.monsterName, ",");
						for (int i = 0; i < sceneT.monsterNum; i++) {
							if (battleRobotList.isEmpty()) {
								break;// 机器人用完了
							}
							FactionBattleRobot robot = battleRobotList.get(NumberUtil.random(battleRobotList.size()));
							robot.setStrongholdId(entry.getKey());
							robot.setTime(new Date());
							robot.setDebuffLvl(0);

							entry.getValue()
									.addDefendRobotList(robot, robotNames[NumberUtil.random(robotNames.length)]);
							battleRobotList.remove(robot);
						}
						refreshRobotTime.put(entry.getKey(), System.currentTimeMillis());
						// 发滚动公告最多5分钟发一次
						if (refreshRobotDate == null || DateUtil.compareTime(new Date(), refreshRobotDate) > 300000) {
							refreshRobotDate = new Date();
							List<ChatAdT> adTList = XsgChatManager.getInstance().getAdContentMap(
									XsgChatManager.AdContentType.FactionBattleOccupy);
							ChatAdT adT = adTList.get(NumberUtil.random(adTList.size()));
							XsgChatManager.getInstance().sendAnnouncement(adT.content);
						}
						notifyIds.add(entry.getKey());
					}
				}

				sendStrongholdStateNotify(notifyIds.toArray(new Integer[0]));
			}
		});
	}

	/**
	 * 检测公会战开启是否满足开战周期
	 * 
	 * @param openWeekDay
	 * @return
	 */
	private boolean checkFactionBattleWeek(List<String> openWeekDay) {
		if (openWeekDay.isEmpty()) {// 天天畅玩
			return true;
		}
		Calendar now = Calendar.getInstance();
		int weekDay = now.get(Calendar.DAY_OF_WEEK) - 1;
		if (weekDay == 0) {
			weekDay = 7;
		}
		if (!openWeekDay.contains(String.valueOf(weekDay))) {
			return false;
		}
		return true;
	}

	/**
	 * 公会战报名定时器
	 * 
	 * @param openWeekDay
	 */
	private void startFactionBattleEnrollTimer(final List<String> openWeekDay) {
		Calendar enrollStartDate = Calendar.getInstance();
		enrollStartDate.setTime(DateUtil.joinTime(this.battleBaseT.openTime));
		enrollStartDate.add(Calendar.MINUTE, -this.battleBaseT.enrollStartTime);
		// 计算延迟时间
		long delayTime = DateUtil.betweenTaskHourMillis(enrollStartDate.get(Calendar.HOUR_OF_DAY),
				enrollStartDate.get(Calendar.MINUTE));
		// 指定时间点执行，并开启每天一次
		LogicThread.scheduleTask(new DelayedTask(delayTime, TimeUnit.DAYS.toMillis(1)) {
			@Override
			public void run() {
				if (!checkFactionBattleWeek(openWeekDay)) {
					return;
				}
				// 清理上一轮公会战参战数据
				clearFactionBattle2DB();
				// 重置运行时参数数据
				resetFactionBattleConfig();
				// 开启开战公告
				sendFactionBattleEnrollMessage();
				// 公告处理
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("~guild_time~", DateUtil.format(battleBaseT.openTime, DateUtil.HHMM_PATTERN));
				XsgChatManager.getInstance().sendAnnouncement(AdContentType.gvgApply, params);
			}
		});
		// 如果当前时间报名已开启则清除上一轮公会战参战数据
		if (checkFactionBattleWeek(openWeekDay)) {
			if (System.currentTimeMillis() > enrollStartDate.getTimeInMillis()) {// 报名已开启
				if (!joinFactionList.isEmpty()) {// 存在报名
					for (String factionId : joinFactionList) {
						IFaction faction = XsgFactionManager.getInstance().findFaction(factionId);
						FactionBattle fb = faction.getFactionBattle();
						if (enrollStartDate.getTimeInMillis() > fb.getEnrollTime().getTime()) {
							clearFactionBattle2DB();
							break;
						}
					}
				} else {// 不存在 单独清除事件数据
					if (randomEvent != null) {
						if (enrollStartDate.getTimeInMillis() > randomEvent.getEventTime().getTime()) {
							clearFactionBattle2DB();
						}
					}
				}
				// 开启开战公告
				sendFactionBattleEnrollMessage();
				// 处理运行时参数数据
				if (runTimeConfig != null
						&& runTimeConfig.getUpdateTime().getTime() <= enrollStartDate.getTimeInMillis()) {
					resetFactionBattleConfig();
				}
			}
		}
	}

	/**
	 * 公会战开战之前的公告
	 */
	private void sendFactionBattleEnrollMessage() {
		// 开战时间处理
		Date joinTime = DateUtil.joinTime(this.battleBaseT.openTime);
		// 开战公告播放时间间隔
		int[] times = { 15, 10, 5 };
		sendFactionBattleNotice(times, joinTime, AdContentType.GvgAD);
	}

	/**
	 * 公会战开启定时器
	 * 
	 * @param openWeekDay
	 */
	private void startFactionBattleOpenTimer(final List<String> openWeekDay) {
		Calendar openStartDate = Calendar.getInstance();
		openStartDate.setTime(this.battleBaseT.openTime);
		// 计算延迟时间
		long delayTime = DateUtil.betweenTaskHourMillis(openStartDate.get(Calendar.HOUR_OF_DAY),
				openStartDate.get(Calendar.MINUTE));
		// 指定时间点执行，并开启每天一次
		LogicThread.scheduleTask(new DelayedTask(delayTime, TimeUnit.DAYS.toMillis(1)) {
			@Override
			public void run() {
				if (!checkFactionBattleWeek(openWeekDay)) {
					return;
				}
				// 公告处理
				XsgChatManager.getInstance().sendAnnouncement(AdContentType.gvgOpen);
				// 针对参战公会发送公会频道消息
				FactionBattleMessageT fbMsg = battleMessages.get("Sociaty_Open");
				if (fbMsg != null && TextUtil.isNotBlank(fbMsg.content)) {
					for (String factionId : joinFactionList) {
						IFaction faction = XsgFactionManager.getInstance().findFaction(factionId);
						FactionBattle fb = faction.getFactionBattle();
						FactionBattleSceneT t = battleScenes.get(fb.getCampStrongholdId());
						sendFactionBattleFactionMessage(fb.getFactionId(), fbMsg.content, t.name);
					}
				}
				startFactionBattleRandomEventTimer();
				sendFactionBattleCloseMessage();

				resetFactionBattleConfig();
				// 机器人生成
				initFactionBattleRobot();
			}
		});

		// 如果已开战则清除上一轮公会战参战运行时参数数据
		if (checkFactionBattleWeek(openWeekDay)) {
			if (System.currentTimeMillis() > openStartDate.getTimeInMillis()) {// 已开战
				if (runTimeConfig != null && runTimeConfig.getUpdateTime().getTime() <= openStartDate.getTimeInMillis()) {// 存在
					// 清理运行时参数
					clearFactionBattleConfig2DB();
				}
				if (runTimeConfig != null && runTimeConfig.getRobotCreateTime() != null
						&& runTimeConfig.getRobotCreateTime().getTime() < openStartDate.getTimeInMillis()) {
					// 机器人生成
					initFactionBattleRobot();
				}
			}
		}
	}

	/**
	 * 公会战关闭之前的公告
	 */
	private void sendFactionBattleCloseMessage() {
		// 闭战时间处理
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(DateUtil.joinTime(this.battleBaseT.openTime));
		startDate.add(Calendar.MINUTE, this.battleBaseT.continueTime);
		Date joinTime = startDate.getTime();
		// 闭战公告播放时间间隔
		int[] times = { 5, 4, 3, 2, 1 };
		sendFactionBattleNotice(times, joinTime, AdContentType.FactionBattleAd);
	}

	/**
	 * 发送公会战倒计时公告
	 * 
	 * @param times
	 *            倒计时时间间隔
	 * @param endTime
	 *            倒计时结束时间
	 * @param type
	 *            公告类型
	 */
	private void sendFactionBattleNotice(int[] times, Date endTime, final AdContentType type) {
		Calendar startDate = Calendar.getInstance();
		for (final int time : times) {
			startDate.setTime(endTime);
			startDate.add(Calendar.MINUTE, -time);
			long delayTime = startDate.getTimeInMillis() - System.currentTimeMillis();
			if (delayTime > 0) {// 处理公告播放
				LogicThread.scheduleTask(new DelayedTask(delayTime) {
					@Override
					public void run() {
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("~param_1~", time);
						XsgChatManager.getInstance().sendAnnouncement(type, params);
					}
				});
			}
		}
	}

	/**
	 * 初始化机器人数据
	 */
	private void initFactionBattleRobot() {
		if (this.joinFactionList.isEmpty()) {
			return;// 没人玩
		}
		// 查询符合条件的机器人
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				final List<String> roleList = RoleDAO.getFromApplicationContext(ServerLancher.getAc()).findRoleIdList(
						15, 7);
				LogicThread.execute(new Runnable() {
					@Override
					public void run() {
						// 初始化各据点机器人
						List<FactionBattleRobot> robotList = new ArrayList<FactionBattleRobot>();
						for (FactionBattleSceneT t : battleScenes.values()) {
							StrongHold sh = getStrongHold(t.strongholdId, true);
							if (t.monsterNum > 0 && !roleList.isEmpty()) {
								for (int i = 0; i < t.monsterNum; i++) {
									FactionBattleRobot robot = new FactionBattleRobot();
									robot.setId(GlobalDataManager.getInstance().generatePrimaryKey());
									robot.setStrongholdId(t.strongholdId);
									robot.setRobotRoleId(roleList.get(NumberUtil.random(roleList.size())));
									robot.setTime(new Date());

									String[] robotNames = StringUtils.split(t.monsterName, ",");
									sh.addDefendRobotList(robot, robotNames[NumberUtil.random(robotNames.length)]);
									robotList.add(robot);
								}
							}
						}
						saveRobot(robotList);
						saveRuntimeConfig(ConfigParam.InitRobot);
					}
				});
			}
		});
	}

	/**
	 * 批量保存公会战机器人数据
	 * 
	 * @param robotList
	 */
	private void saveRobot(final List<FactionBattleRobot> robotList) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				SimpleDAO.getFromApplicationContext(ServerLancher.getAc()).batchSave(robotList);
			}
		});
	}

	/**
	 * 公会战关闭定时器
	 * 
	 * @param openWeekDay
	 */
	private void startFactionBattleCloseTimer(final List<String> openWeekDay) {
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(this.battleBaseT.openTime);
		startDate.add(Calendar.MINUTE, this.battleBaseT.continueTime);
		// 计算延迟时间
		long delayTime = DateUtil.betweenTaskHourMillis(startDate.get(Calendar.HOUR_OF_DAY),
				startDate.get(Calendar.MINUTE));
		// 指定时间点执行，并开启每天一次
		LogicThread.scheduleTask(new DelayedTask(delayTime, TimeUnit.DAYS.toMillis(1)) {
			@Override
			public void run() {
				if (!checkFactionBattleWeek(openWeekDay)) {
					return;
				}
				// 公告处理
				XsgChatManager.getInstance().sendAnnouncement(AdContentType.gvgOver);
				// 排行榜notify
				sendFactionBattleRankListView();
				// 针对参战公会发送公会频道消息
				if (!rankList.isEmpty()) {
					FactionBattleMessageT top10 = battleMessages.get("Sociaty_Close1");
					FactionBattleMessageT msg = battleMessages.get("Sociaty_Close2");
					int rank = 0;
					for (FactionBattle fb : rankList) {
						rank++;
						if (rank > battleBaseT.maxRankSize) {
							if (msg != null && TextUtil.isNotBlank(msg.content)) {
								sendFactionBattleFactionMessage(fb.getFactionId(), msg.content);
							}
						} else {
							if (top10 != null && TextUtil.isNotBlank(top10.content)) {
								sendFactionBattleFactionMessage(fb.getFactionId(), top10.content, rank);
							}
						}
					}

					// 设置运行时榜首数据
					FactionBattle first = rankList.get(0);
					saveRuntimeConfig(ConfigParam.InitFirstRank, first.getFactionId(), first.getCampStrongholdId());
				} else {
					// 无排行清理运行时数据
					clearFactionBattleConfig2DB();
				}
				// 清理缓存公会战数据
				clearFactionBattle2Cache();
			}
		});
	}

	/**
	 * 公会战关闭时，清理临时缓存数据和过程中生成的数据
	 */
	private void clearFactionBattle2Cache() {
		this.joinRoleList.clear();
		this.strongholdDatas.clear();
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				FactionBattleDAO dao = FactionBattleDAO.getFromApplicationContext(ServerLancher.getAc());
				// 清理当前事件数据
				if (randomEvent != null) {
					dao.clearFactionBattleEvent(randomEvent);
				}
				// 清除事件挖宝概率数据
				if (!factionBattleEventRatios.isEmpty()) {
					dao.clearAllFactionBattleEventRatio();
				}
				// 清理公会战日志
				if (!factionBattleLogs.isEmpty()) {
					dao.clearFactionBattleLog();
				}
				// 清理各据点机器人数据
				SimpleDAO.getFromApplicationContext(ServerLancher.getAc()).deleteAll(
						FactionBattleRobot.class.getAnnotation(Table.class).name());
				// 相应内存数据清理
				LogicThread.execute(new Runnable() {
					@Override
					public void run() {
						randomEvent = null;
						factionBattleEventRatios.clear();
						factionBattleLogs.clear();
						battleRobotList.clear();
					}
				});
			}
		});
	}

	/**
	 * 本次公会战开启报名清理上一轮的公会战DB相关数据
	 */
	private void clearFactionBattle2DB() {
		final boolean isExistJoin = !joinFactionList.isEmpty();
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				FactionBattleDAO dao = FactionBattleDAO.getFromApplicationContext(ServerLancher.getAc());
				if (isExistJoin) {// 有公会参战，立即清理数据
					dao.clearAllFactionBattle();
					dao.clearAllFactionBattleMember();
				}
				// 防止非正常关闭上一轮公会战，导致数据未及时清理，再清理一次
				dao.clearFactionBattleEvent();
				dao.clearAllFactionBattleEventRatio();
				dao.clearFactionBattleLog();
				SimpleDAO.getFromApplicationContext(ServerLancher.getAc()).deleteAll(
						FactionBattleRobot.class.getAnnotation(Table.class).name());
			}
		});
		// 内存数据清理
		for (String factionId : joinFactionList) {
			IFaction faction = XsgFactionManager.getInstance().findFaction(factionId);
			faction.resetFactionBattle();// 通过FACTION的定时器进行数据清理操作
		}
		joinFactionList.clear();
		rankList.clear();
		personalRankList.clear();

		randomEvent = null;
		factionBattleEventRatios.clear();
		factionBattleLogs.clear();
		strongholdDatas.clear();
		battleRobotList.clear();
	}

	/**
	 * 本次公会战开战或者关闭时未出现榜首时清理公会战运行时参数DB相关数据
	 */
	private void clearFactionBattleConfig2DB() {
		if (this.runTimeConfig == null) {
			return;
		}
		// 清理运行时参数
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				SimpleDAO.getFromApplicationContext(ServerLancher.getAc()).delete(runTimeConfig);
				LogicThread.execute(new Runnable() {
					@Override
					public void run() {
						runTimeConfig = null;// 内存数据清理
					}
				});
			}
		});
	}

	/**
	 * 重置运行时参数数据
	 */
	private void resetFactionBattleConfig() {
		if (this.runTimeConfig == null || TextUtil.isBlank(this.runTimeConfig.getEnrollCampId())) {
			return;
		}
		saveRuntimeConfig(ConfigParam.ResetEnroll);
	}

	/**
	 * 公会战随机事件处理
	 */
	private void doFactionBattleRandomEvent() {
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(DateUtil.joinTime(this.battleBaseT.openTime));
		if (System.currentTimeMillis() >= startDate.getTimeInMillis()) {
			// 公会战已开启，检测是否关闭
			startDate.add(Calendar.MINUTE, this.battleBaseT.continueTime);
			if (System.currentTimeMillis() >= startDate.getTimeInMillis()) {
				return;// 已关闭不处理
			}
			// 启动随机事件定时器
			startFactionBattleRandomEventTimer();
		}
	}

	/**
	 * 公会战随机事件处理定时器
	 */
	private void startFactionBattleRandomEventTimer() {
		Date startDate = DateUtil.joinTime(this.battleBaseT.openTime);
		long surplusTime = System.currentTimeMillis() - startDate.getTime();
		long randomEventCdTime = this.battleBaseT.randomEventCdTime * 60 * 1000;// 随机事件间隔时间
		if (surplusTime >= 0 && (surplusTime + randomEventCdTime) < this.battleBaseT.continueTime * 60 * 1000) {
			long value = surplusTime % randomEventCdTime;
			long delayTime = randomEventCdTime - value;
			LogicThread.scheduleTask(new DelayedTask(delayTime) {
				@Override
				public void run() {
					FactionBattleRandomEventT eventT = randomEvent();
					List<String> typeList = TextUtil.stringToList(eventT.strongholdType);
					List<Integer> idList = new ArrayList<Integer>();
					for (String type : typeList) {
						idList.addAll(sceneIdList.get(Byte.parseByte(type)));
					}
					int randomId = idList.get(NumberUtil.random(idList.size()));
					FactionBattleEvent newEvent = new FactionBattleEvent();
					newEvent.setEventId(eventT.eventId);
					newEvent.setEventTime(new Date());
					newEvent.setStrongholdId(randomId);
					// 生成事件奖励,发送事件公告和通知
					calculationEventAward(newEvent);
					// 数据保存
					saveEvent(newEvent);
					// 开启下一个事件处理
					startFactionBattleRandomEventTimer();
				}
			});
		}
	}

	/**
	 * 计算新事件奖励和相应消息处理
	 * 
	 * @param newEvent
	 */
	private void calculationEventAward(FactionBattleEvent newEvent) {
		FactionBattleRandomEventT eventT = this.eventTs.get(newEvent.getEventId());
		if (eventT.eventType == EventType.Item) {// 道具收益
			if (TextUtil.isNotBlank(eventT.tc)) {
				TcResult tcResult = XsgRewardManager.getInstance().doTc(null, eventT.tc);
				for (Entry<String, Integer> item : tcResult) {
					newEvent.setEventItems(item.getKey() + "," + item.getValue());
					break;
				}
			}
		}
		// 发送事件公告
		sendEventMessage(newEvent);
		// 发送客户端事件展示通知
		int strongholdId = randomEvent != null ? randomEvent.getStrongholdId() : 0;
		for (String roleId : joinRoleList) {
			IRole role = XsgRoleManager.getInstance().findRoleById(roleId);
			if (role != null) {
				FactionCallBackPrx callBack = role.getFactionBattleController().getFactionCallBack();
				if (callBack != null) {
					callBack.begin_factionBattleEventNotify(strongholdId, newEvent.getStrongholdId(), eventT.icon);
				}
			}
		}
		// 清理老事件关于道具产出的概率数据
		if (randomEvent != null) {
			clearFactionBattleEventRatio(randomEvent.getStrongholdId());
		}
	}

	/**
	 * 发送特殊事件变更通知
	 * 
	 * @param strongholdId
	 *            需要移除事件的据点
	 * @param newEvent
	 *            新增事件
	 */
	private void sendFactionBattleEventNotify(int strongholdId, FactionBattleEvent newEvent) {
		FactionBattleRandomEventT eventT = newEvent == null ? null : this.eventTs.get(newEvent.getEventId());
		for (String roleId : joinRoleList) {
			IRole role = XsgRoleManager.getInstance().findRoleById(roleId);
			if (role != null) {
				FactionCallBackPrx callBack = role.getFactionBattleController().getFactionCallBack();
				if (callBack != null) {
					if (newEvent == null) {
						callBack.begin_factionBattleEventNotify(strongholdId, 0, "");
					} else {
						callBack.begin_factionBattleEventNotify(strongholdId, newEvent.getStrongholdId(), eventT.icon);
					}
				}
			}
		}
	}

	/**
	 * 清除已有事件
	 */
	public void clearFactionBattleEventNotify() {
		if (randomEvent != null) {
			sendFactionBattleEventNotify(randomEvent.getStrongholdId(), null);
		}
	}

	/**
	 * 公会战随机事件公告
	 * 
	 * @param event
	 */
	private void sendEventMessage(FactionBattleEvent event) {
		FactionBattleRandomEventT eventT = this.eventTs.get(event.getEventId());
		for (AdContentType type : AdContentType.values()) {
			if (type.ordinal() == eventT.noticeype) {
				Map<String, Object> params = new HashMap<String, Object>();
				FactionBattleSceneT scenet = XsgFactionBattleManager.getInstance().getBattleSceneT(
						event.getStrongholdId());
				params.put("~stronghold~", scenet.name);
				if (TextUtil.isNotBlank(event.getEventItems())) {
					String[] items = StringUtils.split(event.getEventItems(), ',');
					AbsItemT itemT = XsgItemManager.getInstance().findAbsItemT(items[0]);
					boolean isHeroItem = false;
					if (itemT instanceof NormalItemT) {
						NormalItemT nt = (NormalItemT) itemT;
						if (nt.itemTypeID == 2) {// 将魂道具
							isHeroItem = true;
						}
					}
					if (isHeroItem) {
						NormalItemT nt = (NormalItemT) itemT;
						HeroT heroT = XsgHeroManager.getInstance().getHeroT(Integer.parseInt(nt.useValue));
						params.put("~hero~", XsgChatManager.getInstance().orgnizeHeroText(null, heroT));
					} else {
						params.put("~item~", XsgChatManager.getInstance().orgnizeItemText("", itemT));
					}
				}
				XsgChatManager.getInstance().sendAnnouncement(type, params);
				return;
			}
		}
	}

	/**
	 * 检测公会战开启是否满足开战周期
	 * 
	 * @param openWeekDay
	 * @return
	 */
	public boolean checkFactionBattleWeek() {
		if (this.battleBaseT == null) {
			return false;
		}
		return checkFactionBattleWeek(TextUtil.stringToList(this.battleBaseT.openWeek));
	}

	/**
	 * 清除指定据点的随机事件概率数据
	 * 
	 * @param strongholdId
	 */
	public void clearFactionBattleEventRatio(final int strongholdId) {
		final Map<String, FactionBattleEventRatio> eventRatios = this.factionBattleEventRatios.get(strongholdId);
		if (eventRatios != null && !eventRatios.isEmpty()) {
			DBThreads.execute(new Runnable() {
				@Override
				public void run() {
					FactionBattleDAO dao = FactionBattleDAO.getFromApplicationContext(ServerLancher.getAc());
					dao.clearFactionBattleEventRatio(eventRatios.values());
					LogicThread.execute(new Runnable() {
						@Override
						public void run() {
							factionBattleEventRatios.remove(strongholdId);
						}
					});
				}
			});
		}
	}

	/**
	 * 公会战中发送公会消息
	 * 
	 * @param factionId
	 *            公会编号
	 * @param content
	 *            消息内容
	 * @param paramValue
	 *            消息参数
	 */
	private void sendFactionBattleFactionMessage(String factionId, String content, Object... paramValue) {
		List<IRole> receiveRoles = XsgRoleManager.getInstance().findChatAcceptorList2Faction(factionId);
		if (receiveRoles != null && !receiveRoles.isEmpty()) {
			ChatRole cRole = new ChatRole();
			cRole.id = "0";
			cRole.name = Messages.getString("XsgFaction.6");
			cRole.chatTime = DateUtil.toString(System.currentTimeMillis(), DateUtil.HHMM_PATTERN);
			String remark = content;
			remark = MessageFormat.format(remark, paramValue);
			remark = XsgChatManager.getInstance().orgnizeColorText(remark);
			TextMessage msg = new TextMessage(ChatChannel.Faction, null, cRole, 1, remark, 0, 0);
			for (IRole r : receiveRoles) {
				r.getChatControler().messageReceived("0", Messages.getString("XsgFaction.6"), msg);
			}
		}
	}

	/**
	 * 向公会频道发送公会战消息
	 * 
	 * @param faction
	 *            公会对象
	 * @param msgId
	 *            消息编号
	 * @param paramValue
	 *            消息参数
	 */
	public void sendFactionBattleFactionMessage(IFaction faction, String msgId, Object... paramValue) {
		FactionBattleMessageT fbMsg = battleMessages.get(msgId);
		if (fbMsg != null && TextUtil.isNotBlank(fbMsg.content)) {
			sendFactionBattleFactionMessage(faction.getId(), fbMsg.content, paramValue);
		}
	}

	/**
	 * 是否是公会战开战时间之内
	 * 
	 * @return
	 */
	public boolean isFactionBattleOpenTime() {
		if (checkFactionBattleWeek()) {
			Date beginDate = DateUtil.joinTime(battleBaseT.openTime);
			long endTime = beginDate.getTime() + battleBaseT.continueTime * 60 * 1000;
			if (System.currentTimeMillis() >= beginDate.getTime() && System.currentTimeMillis() <= endTime) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否已报名参加公会战
	 * 
	 * @param role
	 * @return
	 */
	public boolean isEnrollFactionBattle(IRole role) {
		return joinFactionList.contains(role.getFactionControler().getFactionId());
	}

	/**
	 * 是否进入公会战战场
	 * 
	 * @param role
	 * @return
	 */
	public boolean isEnterFactionBattle(IRole role) {
		return joinRoleList.contains(role.getRoleId());
	}

	/**
	 * 是否初始营地
	 * 
	 * @param strongholdId
	 * @return
	 */
	public boolean isInitCamp(int strongholdId) {
		FactionBattleSceneT sceneT = this.battleScenes.get(strongholdId);
		return sceneT.type == BASE_CAMP;
	}

	/**
	 * 创建排行结果对象视图
	 * 
	 * @param rankMaps
	 *            用于记录各个公会的排名,用于公会战结算使用
	 * @param factionId
	 *            指定工会id获取其名次
	 * @return
	 */
	private FactionBattleRankResultView createFactionBattleRankResultView(Map<String, Integer> rankMaps,
			String factionId) {
		FactionBattleRankResultView view = new FactionBattleRankResultView();
		// 奖励数据
		view.rankAwardResults = rankAwardViews;
		// 排行数据
		int rank = 0;
		int curRank = 0;
		int size = 0;
		for (FactionBattle fb : rankList) {
			rank++;
			String factionName = XsgFactionManager.getInstance().findFaction(fb.getFactionId()).getName();
			view.rankResults = (FactionBattleRankView[]) ArrayUtils.add(view.rankResults, new FactionBattleRankView(
					rank, factionName, fb.getBadge()));
			if (TextUtil.isNotBlank(factionId) && fb.getFactionId().equals(factionId)) {
				curRank = rank;
			}
			if (rankMaps != null) {
				rankMaps.put(fb.getFactionId(), rank);
			}
			if (++size >= this.battleBaseT.maxRankSize) {
				break;
			}
		}
		view.rank = curRank;
		return view;
	}

	/**
	 * 创建角色查看的排行视图信息
	 * 
	 * @param factionId
	 *            玩家所在公会
	 * @return
	 */
	public FactionBattleRankResultView createFactionBattleRankResultView(String factionId) {
		return createFactionBattleRankResultView(null, factionId);
	}

	/**
	 * 发送公会排行视图信息,通知所有参战成员
	 */
	private void sendFactionBattleRankListView() {
		Map<String, Integer> rankMaps = new HashMap<String, Integer>();
		FactionBattleRankResultView view = createFactionBattleRankResultView(rankMaps, null);
		List<String> notifyRoleList = XsgFactionBattleManager.getInstance().getJoinRoleList();
		for (String roleId : notifyRoleList) {
			IRole role = XsgRoleManager.getInstance().findRoleById(roleId);
			if (role != null) {
				String factionId = role.getFactionControler().getFactionId();
				if (rankMaps.containsKey(factionId)) {
					view.rank = rankMaps.get(factionId);
				} else {
					view.rank = 0;
				}
				FactionCallBackPrx callBack = role.getFactionBattleController().getFactionCallBack();
				if (callBack != null) {
					callBack.begin_factionBattleRankResultNotify(LuaSerializer.serialize(view));
				}
			}
		}
		// 计算参战公会战奖励并发送邮件
		for (String factionId : rankMaps.keySet()) {
			int rank = rankMaps.get(factionId);
			FactionBattleRankAwardT rankAwarT = this.battleRankAwards.get(rank);
			if (rankAwarT != null) {
				IFaction faction = XsgFactionManager.getInstance().findFaction(factionId);
				// 组织奖励数据
				String[] items = StringUtils.split(rankAwarT.items, ",");
				Map<String, Integer> rewardMap = new HashMap<String, Integer>();
				for (String itemStr : items) {
					String[] item = itemStr.split(":");
					// faction.putWarehouseItem(item[0],
					// Integer.parseInt(item[1]));
					rewardMap.put(item[0], Integer.parseInt(item[1]));
				}
				// 组织邮件参数数据
				Map<String, String> replaceMap = new HashMap<String, String>();
				replaceMap.put("$x", String.valueOf(rank));
				// 循环给公会参战成员发送奖励邮件
				for (String roleId : faction.getAllFactionBattleMember().keySet()) {
					XsgMailManager.getInstance().sendTemplate(roleId, MailTemplate.FactionBattleRank, rewardMap,
							replaceMap);
				}
				// 继续给公会战参战成员排行邮件奖励
				List<FactionBattleMember> personalRanks = personalRankList.get(factionId);
				if (personalRanks != null) {
					if (TextUtil.isNotBlank(rankAwarT.personalItems)) {
						int rankSize = 0;
						String[] p_items = StringUtils.split(rankAwarT.personalItems, ",");
						for (FactionBattleMember fbm : personalRanks) {
							rankSize++;
							if (this.personalRankTs.containsKey(rankSize)) {
								int scale = this.personalRankTs.get(rankSize);
								Map<String, Integer> rewardMap2Personal = new HashMap<String, Integer>();
								// 奖励参数组织
								for (String itemStr : p_items) {
									String[] item = itemStr.split(":");
									int value = (int) (Integer.parseInt(item[1]) * (scale / 10000F));
									rewardMap2Personal.put(item[0], value);
								}
								// 组织邮件参数数据
								Map<String, String> replaceMap2Personal = new HashMap<String, String>();
								replaceMap2Personal.put("$x", String.valueOf(rankSize));
								XsgMailManager.getInstance()
										.sendTemplate(fbm.getRoleId(), MailTemplate.FactionBattlePersonalRank,
												rewardMap2Personal, replaceMap2Personal);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 获得指定类型的锦囊提示
	 * 
	 * @param type
	 * @return
	 */
	public String getKitsMsg(int type) {
		List<String> msgs = this.battleKitsMessages.get((byte) type);
		if (msgs != null && !msgs.isEmpty()) {
			return msgs.get(NumberUtil.random(msgs.size()));
		}
		return null;
	}

	/**
	 * 随机获取一个锦囊
	 * 
	 * @param kitsId
	 *            需要排除的锦囊编号
	 * @return
	 */
	public FactionBattleKitsT randomKits(int kitsId) {
		List<FactionBattleKitsT> objects = new ArrayList<FactionBattleKitsT>();
		List<FactionBattleKitsT> result = null;
		if (kitsId > 0) {
			int[] weights = null;
			for (FactionBattleKitsT kt : this.battleKitses.values()) {
				if (kt.kitsId != kitsId) {
					objects.add(kt);
					weights = ArrayUtils.add(weights, kt.weight);
				}
			}
			result = FactionBattleUtil.calcRandomPositionByRatio(objects, weights, 1);
		} else {
			objects = new ArrayList<FactionBattleKitsT>(this.battleKitses.values());
			result = FactionBattleUtil.calcRandomPositionByRatio(objects, kitsWeights, 1);
		}
		if (!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	/**
	 * 随机获取一个事件
	 * 
	 * @return
	 */
	private FactionBattleRandomEventT randomEvent() {
		List<FactionBattleRandomEventT> objects = new ArrayList<FactionBattleRandomEventT>(this.eventTs.values());
		List<FactionBattleRandomEventT> result = FactionBattleUtil.calcRandomPositionByRatio(objects, eventWeights, 1);
		if (!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	/**
	 * 特殊随机事件
	 * 
	 * @param newEvent
	 */
	private void saveEvent(final FactionBattleEvent newEvent) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				FactionBattleDAO dao = FactionBattleDAO.getFromApplicationContext(ServerLancher.getAc());
				if (randomEvent != null) {
					dao.clearFactionBattleEvent(randomEvent);
				}
				if (newEvent != null) {
					dao.saveFactionBattleEvent(newEvent);
				}
				LogicThread.execute(new Runnable() {
					@Override
					public void run() {
						randomEvent = newEvent;
					}
				});
			}
		});
	}

	/**
	 * 保存公会战运行时参数数据
	 * 
	 * @param param
	 * @param values
	 */
	public void saveRuntimeConfig(ConfigParam param, Object... values) {
		if (this.runTimeConfig == null) {
			this.runTimeConfig = new FactionBattleConfig();
			this.runTimeConfig.setId(GlobalDataManager.getInstance().generatePrimaryKey());
		}
		switch (param) {
		case InitEnroll:
			this.runTimeConfig.setEnrollCampId(values[0].toString());
			this.runTimeConfig.setUpdateTime(new Date());
			break;
		case ResetEnroll:
			this.runTimeConfig.setEnrollCampId(null);
			this.runTimeConfig.setUpdateTime(new Date());
			break;
		case InitFirstRank:
			this.runTimeConfig.setFirstFactionId(values[0].toString());
			this.runTimeConfig.setFirstCampId(Integer.parseInt(values[1].toString()));
			this.runTimeConfig.setUpdateTime(new Date());
			break;
		case InitRobot:
			this.runTimeConfig.setRobotCreateTime(new Date());
			break;
		default:
			break;
		}
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				SimpleDAO.getFromApplicationContext(ServerLancher.getAc()).attachDirty(runTimeConfig);
			}
		});
	}

	/**
	 * 创建公会战控制器
	 * 
	 * @param r
	 * @param db
	 * @return
	 */
	public FactionBattleController createFactionBattleControler(IRole r, Role db) {
		return new FactionBattleController(r, db);
	}

	/**
	 * @return Returns the runTimeConfig.
	 */
	public FactionBattleConfig getRunTimeConfig() {
		return runTimeConfig;
	}

	/**
	 * @return Returns the battleBaseT.
	 */
	public FactionBattleBaseT getBattleBaseT() {
		return battleBaseT;
	}

	/**
	 * 公会战场景对象
	 * 
	 * @param strongholdId
	 * @return
	 */
	public FactionBattleSceneT getBattleSceneT(int strongholdId) {
		return battleScenes.get(strongholdId);
	}

	/**
	 * @return Returns the battleScenes.
	 */
	public Map<Integer, FactionBattleSceneT> getBattleScenes() {
		return battleScenes;
	}

	/**
	 * @return Returns the camps.
	 */
	public int[] getCamps() {
		return camps;
	}

	/**
	 * @return Returns the battleStrongholds.
	 */
	public Map<Byte, FactionBattleStrongholdT> getBattleStrongholds() {
		return battleStrongholds;
	}

	/**
	 * 获取锦囊
	 * 
	 * @param kitsId
	 * @return
	 */
	public FactionBattleKitsT getBattleKitsT(int kitsId) {
		return battleKitses.get(kitsId);
	}

	/**
	 * @return Returns the battleDiggingTreasures.
	 */
	public Map<Byte, FactionBattleDiggingTreasureT> getBattleDiggingTreasures() {
		return battleDiggingTreasures;
	}

	/**
	 * @return Returns the battleMessages.
	 */
	public Map<String, FactionBattleMessageT> getBattleMessages() {
		return battleMessages;
	}

	/**
	 * @return Returns the battleAddAwards.
	 */
	public Map<Integer, FactionBattleAddAwardT> getBattleAddAwards() {
		return battleAddAwards;
	}

	/**
	 * @return Returns the strongholdDatas.
	 */
	public Map<Integer, StrongHold> getStrongholdDatas() {
		return strongholdDatas;
	}

	/**
	 * 获得据点数据
	 * 
	 * @param strongholdId
	 * @return
	 */
	public StrongHold getStrongHold(int strongholdId) {
		return strongholdDatas.get(strongholdId);
	}

	/**
	 * 获得据点对象数据
	 * 
	 * @param strongholdId
	 * @param isInit
	 *            是否需要初始化
	 * @return
	 */
	public StrongHold getStrongHold(int strongholdId, boolean isInit) {
		StrongHold sh = strongholdDatas.get(strongholdId);
		if (isInit && sh == null) {
			sh = new StrongHold();
			strongholdDatas.put(strongholdId, sh);
		}
		return sh;
	}

	/**
	 * @return Returns the joinRoleList.
	 */
	public List<String> getJoinRoleList() {
		return joinRoleList;
	}

	/**
	 * @return Returns the randomEvent.
	 */
	public FactionBattleEvent getRandomEvent() {
		return randomEvent;
	}

	/**
	 * @return Returns the eventTs.
	 */
	public Map<Integer, FactionBattleRandomEventT> getEventTs() {
		return eventTs;
	}

	/**
	 * @return Returns the debuffs.
	 */
	public Map<Integer, FactionBattleDebuffT> getDebuffs() {
		return debuffs;
	}

	/**
	 * 增加事随机概率数据
	 * 
	 * @param fber
	 */
	public void addEventRatio(FactionBattleEventRatio fber) {
		Map<String, FactionBattleEventRatio> map = this.factionBattleEventRatios.get(fber.getStrongholdId());
		if (map == null) {
			map = new HashMap<String, FactionBattleEventRatio>();
			this.factionBattleEventRatios.put(fber.getStrongholdId(), map);
		}
		map.put(fber.getRoleId(), fber);
	}

	/**
	 * 获取事件随机概率数据
	 * 
	 * @param strongholdId
	 * @param roleId
	 * @return
	 */
	public FactionBattleEventRatio getEventRatio(int strongholdId, String roleId) {
		Map<String, FactionBattleEventRatio> map = this.factionBattleEventRatios.get(strongholdId);
		if (map == null) {
			return null;
		}
		return map.get(roleId);
	}

	/**
	 * @return Returns the maxEvenKillNum.
	 */
	public int getMaxEvenKillNum() {
		return maxEvenKillNum;
	}

	/**
	 * 添加参战公会
	 * 
	 * @param factionId
	 */
	public void addJoinFaction(String factionId) {
		this.joinFactionList.add(factionId);
	}

	/**
	 * 设置排行数据
	 * 
	 * @param fb
	 */
	public void setRankList(FactionBattle fb) {
		for (FactionBattle rank : rankList) {
			if (rank.getFactionId().equals(fb.getFactionId())) {
				rankList.remove(rank);
				break;
			}
		}
		this.rankList.add(fb);
		sortFactionBattleRank();
	}

	/**
	 * 设置个人排行榜
	 * 
	 * @param fbm
	 */
	public void setPersonalRankList(FactionBattleMember fbm) {
		List<FactionBattleMember> rankList = this.personalRankList.get(fbm.getFaction().getId());
		if (rankList == null) {
			rankList = new ArrayList<FactionBattleMember>();
			rankList.add(fbm);
			this.personalRankList.put(fbm.getFaction().getId(), rankList);
			return;
		}
		for (FactionBattleMember rank : rankList) {
			if (rank.getRoleId().equals(fbm.getRoleId())) {
				rankList.remove(rank);
				break;
			}
		}
		rankList.add(fbm);
		sortFactionBattleMemberRank(rankList);
	}

	/**
	 * 创建个人排行结果对象视图
	 * 
	 * @param role
	 *            当事人
	 * @return
	 */
	public FactionBattlePersonalRankResultView createFactionBattlePersonalRankResultView(IRole role) {
		FactionBattlePersonalRankResultView view = new FactionBattlePersonalRankResultView();
		List<FactionBattleMember> prankList = this.personalRankList.get(role.getFactionControler().getFactionId());
		if (prankList != null) {
			int rank = 0;
			view.ranks = new FactionBattlePersonalRankView[prankList.size()];
			for (FactionBattleMember fbm : prankList) {
				IRole temp = XsgRoleManager.getInstance().findRoleById(fbm.getRoleId());
				view.ranks[rank] = new FactionBattlePersonalRankView(++rank, fbm.getRoleId(), temp.getName(),
						temp.getHeadImage(), temp.getVipLevel(), temp.getLevel(), fbm.getBadge());
				if (role.getRoleId().equals(fbm.getRoleId())) {
					view.rank = rank;
					view.badge = fbm.getBadge();
				}
			}
		}
		if (!isFactionBattleOpenTime()) {// 需要显示奖励
			IFaction faction = role.getFactionControler().getMyFaction();
			FactionBattle fb = faction.getFactionBattle();
			int rank = rankList.indexOf(fb) + 1;
			FactionBattleRankAwardT rankAwarT = this.battleRankAwards.get(rank);
			if (rankAwarT != null && TextUtil.isNotBlank(rankAwarT.personalItems)) {
				view.isShowAward = true;
				String[] p_items = StringUtils.split(rankAwarT.personalItems, ",");

				view.rankAwardResults = new FactionBattleRankAwardView[personalRankListTs.size()];
				for (int i = 0; i < personalRankListTs.size(); i++) {
					FactionBattlePersonalRankAwardT awardT = personalRankListTs.get(i);
					view.rankAwardResults[i] = new FactionBattleRankAwardView();
					view.rankAwardResults[i].rank = awardT.rank;

					view.rankAwardResults[i].items = new IntString[p_items.length];
					for (int j = 0; j < p_items.length; j++) {
						String[] item = p_items[j].split(":");
						int value = (int) (Integer.parseInt(item[1]) * (awardT.scale / 10000F));
						view.rankAwardResults[i].items[j] = new IntString(value, item[0]);
					}

					String[] ranks = StringUtils.split(awardT.rank, "-");
					int startRank = Integer.parseInt(ranks[0]);
					int endRank = ranks.length > 1 ? Integer.parseInt(ranks[1]) : startRank;
					if (view.rank >= startRank && view.rank <= endRank) {
						view.selectedIndex = (byte) i;
					}
				}
			}
		}
		return view;
	}

	/**
	 * 获取指定角色的公会战相关日志
	 * 
	 * @param roleId
	 * @return
	 */
	public List<FactionBattleLog> getFactionLogs(String roleId) {
		return this.factionBattleLogs.get(roleId);
	}

	/**
	 * 增加公会战日志
	 * 
	 * @param log
	 */
	public void addFactionBattleLogs(FactionBattleLog log) {
		List<FactionBattleLog> logs = factionBattleLogs.get(log.getRole_id());
		if (logs == null) {
			logs = new ArrayList<FactionBattleLog>();
			factionBattleLogs.put(log.getRole_id(), logs);
		}
		logs.add(log);
	}

	/**
	 * 发送据点状态通知包，通知所有参战的成员
	 * 
	 * @param strongholdIds
	 */
	public void sendStrongholdStateNotify(Integer... strongholdIds) {
		if (strongholdIds.length == 0) {
			return;
		}
		List<String> notifyRoleList = getJoinRoleList();
		for (String roleId : notifyRoleList) {
			IRole role = XsgRoleManager.getInstance().findRoleById(roleId);
			if (role != null) {
				FactionCallBackPrx callBack = role.getFactionBattleController().getFactionCallBack();
				if (callBack != null) {
					StrongHoldState[] views = createStrongHoldStateView(role, strongholdIds);
					callBack.begin_strongholdStateNotify(LuaSerializer.serialize(views));
				}
			}
		}
	}

	/**
	 * 据点状态视图数据
	 * 
	 * @param role
	 * @param strongholdIds
	 * @return
	 */
	public StrongHoldState[] createStrongHoldStateView(IRole role, Integer... strongholdIds) {
		StrongHoldState[] views = null;
		for (int strongholdId : strongholdIds) {
			IFaction faction = role.getFactionControler().getMyFaction();

			StrongHold sh = getStrongHold(strongholdId);
			StrongHoldState sstate = new StrongHoldState(strongholdId, (byte) sh.occupyState(),
					(byte) sh.convertCampId(faction.getFactionBattle().getCampStrongholdId()));
			views = (StrongHoldState[]) ArrayUtils.add(views, sstate);
		}
		return views;
	}

	/**
	 * 保存被打掉的机器人下次刷新时再使用
	 * 
	 * @param robot
	 */
	public void addRemovedRobot(FactionBattleRobot robot) {
		battleRobotList.add(robot);
	}

	/**
	 * 锦囊产出类型
	 * 
	 * @author zwy
	 * @since 2016-1-15
	 * @version 1.0
	 */
	class KitsMsgType {
		/** 首次进入 */
		final static byte First = 1;

		/** 战斗胜利获得 */
		final static byte Win = 2;

		/** 战斗失败复活后获得 */
		final static byte Revive = 3;
	}

	/**
	 * 锦囊
	 * 
	 * @author zwy
	 * @since 2016-1-15
	 * @version 1.0
	 */
	class Kits {
		/** 神偷 */
		final static int Steal = 1001;

		/** 飞驰 */
		final static int Rxql = 1002;

		/** 蓝翔 */
		final static int LanXiang = 1003;

		/** 滋养 */
		final static int ZiYang = 1004;
	}

	/**
	 * 事件类型
	 * 
	 * @author zwy
	 * @since 2016-1-21
	 * @version 1.0
	 */
	class EventType {

		/** 挖宝双倍收益 */
		final static byte DoubleIncome = 1;

		/** 免战 */
		final static byte AvoidWar = 2;

		/** 物品 */
		final static byte Item = 3;
	}

	/**
	 * 配置参数枚举
	 * 
	 * @author weiyi.zhao
	 * @since 2016-5-6
	 * @version 1.0
	 */
	enum ConfigParam {
		InitEnroll, // 报名初始化
		ResetEnroll, // 重置报名数据
		InitRobot, // 机器人初始化
		InitFirstRank// 榜首初始化
	}
}

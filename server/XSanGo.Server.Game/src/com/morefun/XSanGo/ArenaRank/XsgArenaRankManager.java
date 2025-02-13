/**
 * 
 */
package com.morefun.XSanGo.ArenaRank;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.db.game.ArenaAwardLog;
import com.morefun.XSanGo.db.game.ArenaRank;
import com.morefun.XSanGo.db.game.ArenaRankDao;
import com.morefun.XSanGo.db.game.ArenaRankFight;
import com.morefun.XSanGo.db.game.Mail;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleDAO;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.robot.RobotConfig;
import com.morefun.XSanGo.robot.RobotFormationT;
import com.morefun.XSanGo.robot.XsgRobotManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 竞技场 全局管理
 * 
 * @author 吕明涛
 * 
 */
public class XsgArenaRankManager {
	private static XsgArenaRankManager instance = new XsgArenaRankManager();

	private static final Log log = LogFactory.getLog(XsgArenaRankManager.class);

	/** 竞技场排名 等级索引 */
	public Map<Integer, ArenaRank> ArenaRankLevelMap = new HashMap<Integer, ArenaRank>();
	/** 竞技场排名 玩家ID索引 */
	public Map<String, ArenaRank> ArenaRoleIdMap = new HashMap<String, ArenaRank>();

	// 竞技场匹配
	public List<ArenaRankMatchT> rankMatchList = new ArrayList<ArenaRankMatchT>();
	// 购买挑战令、嘲讽模式、商城刷新
	private Map<Integer, ArenaSneerT> SneerMap = new HashMap<Integer, ArenaSneerT>();
	private Map<Integer, ArenaMallRefreshT> MallRefreshMap = new HashMap<Integer, ArenaMallRefreshT>();
	private Map<Integer, ArenaChallengeT> ChallengeMap = new HashMap<Integer, ArenaChallengeT>();
	// 竞技场商城兑换
	private Map<Integer, ArenaMallStoreT> MallStoreMap1 = new HashMap<Integer, ArenaMallStoreT>();
	private Map<Integer, ArenaMallStoreT> MallStoreMap2 = new HashMap<Integer, ArenaMallStoreT>();
	private Map<Integer, ArenaMallStoreT> MallStoreMapAll = new HashMap<Integer, ArenaMallStoreT>();
	// 竞技场 发送奖励
	public List<ArenaRankAwareT> rankAwardList = new ArrayList<ArenaRankAwareT>();
	// 历史排名奖励 和 每日首胜
	public List<ArenaLevelAwardT> levelAwardList = new ArrayList<ArenaLevelAwardT>();
	public List<ArenaFirstWinT> firstWinList = new ArrayList<ArenaFirstWinT>();
	// 清除挑战时间
	public Map<Integer, Integer> ArenaClearCDMap = new HashMap<Integer, Integer>();

	public static XsgArenaRankManager getInstance() {
		return instance;
	}

	private XsgArenaRankManager() {

		// 竞技场 定期发送奖励
		String[] refreshDate = XsgGameParamManager.getInstance().getRefresh()
				.split(":"); //$NON-NLS-1$
		long interval = DateUtil.betweenTaskHourMillis(
				Integer.valueOf(refreshDate[0]),
				Integer.valueOf(refreshDate[1]));
		LogicThread
				.scheduleTask(new DelayedTask(interval, 60 * 60 * 24 * 1000) {
					@Override
					public void run() {
						XsgArenaRankManager.getInstance().rankAward();
					}
				});

		this.setSneerMap(); // 购买挑战令
		this.setMallRefreshMap(); // 嘲讽模式
		this.setChallengeMap(); // 商城刷新
		this.setMallStoreMap1(); // 商城固定道具
		this.setMallStoreMap2(); // 商城随机道具

		MallStoreMapAll.putAll(this.getMallStoreMap1());
		MallStoreMapAll.putAll(this.getMallStoreMap2());

		// 竞技场匹配
		this.rankMatchList = ExcelParser.parse(ArenaRankMatchT.class);
		// 竞技场 发送奖励
		this.rankAwardList = ExcelParser.parse(ArenaRankAwareT.class);
		// 历史排名奖励 和 每日首胜
		this.levelAwardList = ExcelParser.parse(ArenaLevelAwardT.class);
		this.firstWinList = ExcelParser.parse(ArenaFirstWinT.class);

		// 清除挑战时间
		for (ArenaClearCDT ClearCd : ExcelParser.parse(ArenaClearCDT.class)) {
			ArenaClearCDMap.put(ClearCd.time, ClearCd.cost);
		}
	}
	
	/**
	 * 竞技场 控制模块
	 * 
	 * @param roleRt
	 * @param roleDB
	 * @return
	 */
	public IArenaRankControler create(IRole roleRt, Role roleDB) {
		return new ArenaRankControler(roleRt, roleDB);
	}

	public void setArenaRankLevelMap(ArenaRank arenaRank) {
		ArenaRankLevelMap.put(arenaRank.getRank(), arenaRank);
	}

	/**
	 * 初始化 竞技场数据 已经存在的机器人使用
	 * 
	 * @param roleId
	 * @param rank
	 * @param isRobot
	 * @return
	 */
	public ArenaRank initArenaRank(String roleId, int rank, boolean isRobot) {
		ArenaRank arenaRank = new ArenaRank();
		arenaRank.setRoleId(roleId);
		arenaRank.setRank(rank);
		arenaRank.setRobot(isRobot);

		return arenaRank;
	}

	/**
	 * 设置 并且 保存数据库
	 * 
	 * @param arenaRank
	 */
	public void setArenaRankLevelMap_Async(ArenaRank arenaRank) {
		this.setArenaRankLevelMap(arenaRank);
		this.setArenaRoleIdMap(arenaRank);
		this.save2DbAsync(arenaRank);
	}

	public void setArenaRoleIdMap(ArenaRank arenaRank) {
		ArenaRoleIdMap.put(arenaRank.getRoleId(), arenaRank);
	}

	public Map<Integer, ArenaSneerT> getSneerMap() {
		return SneerMap;
	}

	public void setSneerMap() {
		for (ArenaSneerT arenaSneer : ExcelParser.parse(ArenaSneerT.class)) {
			SneerMap.put(arenaSneer.id, arenaSneer);
		}
	}

	public Map<Integer, ArenaMallRefreshT> getMallRefreshMap() {
		return MallRefreshMap;
	}

	public void setMallRefreshMap() {
		for (ArenaMallRefreshT mallRefresh : ExcelParser
				.parse(ArenaMallRefreshT.class)) {
			MallRefreshMap.put(mallRefresh.Time, mallRefresh);
		}
	}

	public ArenaChallengeT getChallengeMap(int id) {
		return ChallengeMap.get(id);
	}

	public void setChallengeMap() {
		for (ArenaChallengeT challenge : ExcelParser
				.parse(ArenaChallengeT.class)) {
			ChallengeMap.put(challenge.Time, challenge);
		}
	}

	public Map<Integer, ArenaMallStoreT> getMallStoreMap1() {
		return MallStoreMap1;
	}

	public void setMallStoreMap1() {
		for (ArenaMallStoreT mallStore : ExcelParser.parse("商城固定刷新", //$NON-NLS-1$
				ArenaMallStoreT.class)) {
			MallStoreMap1.put(mallStore.ID, mallStore);
		}
	}

	public Map<Integer, ArenaMallStoreT> getMallStoreMap2() {
		return MallStoreMap2;
	}

	public void setMallStoreMap2() {
		for (ArenaMallStoreT mallStore : ExcelParser.parse("商城随机刷新", //$NON-NLS-1$
				ArenaMallStoreT.class)) {
			MallStoreMap2.put(mallStore.ID, mallStore);
		}
	}

	public Map<Integer, ArenaMallStoreT> getMallStoreMapAll() {
		return MallStoreMapAll;
	}

	/**
	 * 创建已经存在竞技场数据，机器人的排行
	 * 
	 * @param limitNum
	 * @return
	 */
	// private int initArenaRobotRole(int limitNum) {
	// //查询已经存在的机器人 排行榜数据
	// List<String> arenaRobotRole =
	// RoleDAO.getFromApplicationContext(ServerLancher.getAc())
	// .findArenaRobot(limitNum);
	//
	// for (int i = 0; i < arenaRobotRole.size(); i++) {
	// //异步保存 排行榜 到数据库
	// ArenaRank robotRank = this.initArenaRank(arenaRobotRole.get(i), i + 1,
	// true);
	// this.setArenaRankLevelMap_Async(robotRank);
	// }
	//
	// return arenaRobotRole.size();
	// }

	/**
	 * 初始化竞技场排行榜 人数不足时，增加 竞技场 排名 机器人
	 */
	public void addRobot(Map<Integer, List<RobotFormationT>> robotFormationMap) {

		// 初始化 已有的排名
		ArenaRankDao arenaRankDAO = ArenaRankDao
				.getFromApplicationContext(ServerLancher.getAc());
		for (ArenaRank arenaRank : arenaRankDAO.findAll()) {
			this.setArenaRankLevelMap(arenaRank);
			this.setArenaRoleIdMap(arenaRank);
		}

		// 排名数据不足，设置 机器人
		int initRank = XsgGameParamManager.getInstance().getArenaStartRank();
		if (ArenaRankLevelMap.size() == 0) {

			// 查询已经存在的机器人
			// int robotNum = this.initArenaRobotRole(initRank);
			// int remainNum = initRank - robotNum;

			// 数量不足，生成剩余机器人
			if (initRank > 0) {
				// 已经存在角色的名字
				List<String> roleNameList = RoleDAO.getFromApplicationContext(
						ServerLancher.getAc()).findByNameAll();

				// 随机生成的名字，保证不重复
				List<String> randomNameList = new ArrayList<String>(initRank);
				while (true) {
					String randomName = XsgRoleManager.getInstance()
							.generateRandomName(NumberUtil.random(100));
					if (!randomNameList.contains(randomName)
							&& !roleNameList.contains(randomName)) {
						randomNameList.add(randomName);
					}

					if (randomNameList.size() > initRank)
						break;
				}

				for (int i = 1; i <= initRank; i++) {
					if (ArenaRankLevelMap.get(i) == null) {
						// 随机等级
						int robotLevel = NumberUtil.randomContain(
								XsgGameParamManager.getInstance()
										.getArenaLevel(), XsgGameParamManager
										.getInstance().getArenaLevel() + 5);

						RobotConfig robotConfig = XsgRobotManager.getInstance()
								.createRobot(i, randomNameList.get(i - 1), i % 2, robotFormationMap);
						IRole robot = XsgRobotManager.getInstance().newRobot(
								robotConfig);

						// 异步保存 排行榜 到数据库
						ArenaRank robotRank = robot.getArenaRankControler()
								.initArenaRank(i, robot.isRobot());
						this.setArenaRankLevelMap_Async(robotRank);

						robot.saveAsyn();
					}
				}
			}
		}
	}

	/**
	 * 异步保存到数据库
	 * 
	 * @param message
	 */
	public void save2DbAsync(final ArenaRank arenaRank) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				ArenaRankDao dao = ArenaRankDao
						.getFromApplicationContext(ServerLancher.getAc());
				dao.save(arenaRank);
			}
		});
	}

	/**
	 * 异步 保存 排行榜奖励日志 到数据库
	 * 
	 * @param message
	 */
	public void saveAwardLogAsync(final ArenaAwardLog awardLog) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				ArenaRankDao dao = ArenaRankDao
						.getFromApplicationContext(ServerLancher.getAc());
				dao.saveAwardLog(awardLog);
			}
		});
	}

	/**
	 * 竞技场 定期发送奖励
	 */
	public void rankAward() {
		Map<Integer, ArenaRank> rankLevelMap = XsgArenaRankManager
				.getInstance().ArenaRankLevelMap;

		Map<Integer, Map<String, String>> awardMap = new HashMap<Integer, Map<String, String>>();

		List<Mail> mails = new ArrayList<Mail>();
		for (int rank : rankLevelMap.keySet()) {
			for (ArenaRankAwareT awardT : XsgArenaRankManager.getInstance().rankAwardList) {

				ArenaRank arenaRank = rankLevelMap.get(rank);
				if (!arenaRank.getRobot()) {
					if ((rank >= awardT.start && rank <= awardT.end)
							|| (rank >= awardT.start && 0 == awardT.end)) {

						Map<String, Integer> rewardMap = new HashMap<String, Integer>();
						for (String item : awardT.items.split(",")) { //$NON-NLS-1$
							String[] itemSub = item.split(":"); //$NON-NLS-1$
							rewardMap.put(itemSub[0],
									Integer.valueOf(itemSub[1]));
						}

						// 保存奖励 日志
						Map<String, String> awardSub = new HashMap<String, String>();
						awardSub.put("id", arenaRank.getRoleId()); //$NON-NLS-1$
						awardMap.put(rank, awardSub);

						Map<String, String> replaceMap = new HashMap<String, String>(
								2);
						replaceMap.put("$a", XsgGameParamManager.getInstance() //$NON-NLS-1$
								.getRefresh());
						replaceMap.put("$m", String.valueOf(rank)); //$NON-NLS-1$

						// 获取邮件
						Mail m = XsgMailManager.getInstance().getMailByTemplate(
								arenaRank.getRoleId(),
								MailTemplate.ARENARANK_DAILY, rewardMap,
								replaceMap);
						if (m != null) {
							mails.add(m);
						}
						break;
					}
				}
			}
		}
		
		// 发送邮件
		if (mails.size() > 0) {
			XsgMailManager.getInstance().sendMail(mails);
		}

		// 保存奖励 日志
		final ArenaAwardLog awardlog = new ArenaAwardLog(new Date(), 1,
				TextUtil.GSON.toJson(awardMap));
		this.saveAwardLogAsync(awardlog);
	}
	
	/**
	 * 由于bug引起的坏档数据检查清理
	 * */
	public static void removeBrokenData(Role role) {
		// 战报ID缺失bug引起的无法保存数据库的异常处理
		List<ArenaRankFight> dataList = role.getArenaRankFightList();
		List<ArenaRankFight> removeList = new ArrayList<ArenaRankFight>();
		for (ArenaRankFight data:dataList) {
			if (TextUtil.isBlank(data.getMovieId())) {
				removeList.add(data);
			}
		}
		role.getArenaRankFightList().removeAll(removeList);
		if (removeList.size() > 0) {
			log.warn(TextUtil.format(Messages.getString("XsgArenaRankManager.8"), role.getName(), removeList.size())); //$NON-NLS-1$
		}
	}
}

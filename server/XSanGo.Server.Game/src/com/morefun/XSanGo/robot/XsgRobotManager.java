/**
 * 
 */
package com.morefun.XSanGo.robot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.ArenaRank.ArenaRankRobotT;
import com.morefun.XSanGo.common.HeroSource;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleDAO;
import com.morefun.XSanGo.equip.EquipItem;
import com.morefun.XSanGo.hero.HeroT;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.hero.XsgHeroManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.NewRoleConfig;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.NumberUtil;

/**
 * 碎片的 合成、掠夺、复仇抢回碎片 全局管理
 * 
 * @author 吕明涛
 * 
 */
public class XsgRobotManager {
	private static XsgRobotManager instance = new XsgRobotManager();

	// 阵容位置
	public int post0 = 0;
	public int post3 = 3;
	public int post6 = 6;
	public int post9 = 9;

	// 部队武将数量
	private int formationNum = 5;

	// 竞技场 使用 机器人 部队
	public Map<Integer, List<RobotFormationT>> arenaRobotFormationMap = new HashMap<Integer, List<RobotFormationT>>();
	// 掠夺 使用 机器人 部队
	public Map<Integer, List<RobotFormationT>> robRobotFormationMap = new HashMap<Integer, List<RobotFormationT>>();
	// 机器人配置
	public List<ArenaRankRobotT> arenaRankRobotTList = new ArrayList<ArenaRankRobotT>();

	/**
	 * 群雄争霸机器人模板
	 */
	public Map<Integer, List<LadderRobotT>> ladderRobotMap = new HashMap<Integer, List<LadderRobotT>>();

	/**
	 * 群雄机器人ID
	 */
	public List<String> ladderRobotIds = new ArrayList<String>();

	public static XsgRobotManager getInstance() {
		return instance;
	}

	public XsgRobotManager() {
		// 竞技场 使用 部队 前、中、后 排的位置
		for (RobotFormationT formation : ExcelParser.parse("武将组成", RobotFormationT.class)) {
			List<RobotFormationT> rowFormatonList = arenaRobotFormationMap.get(formation.pos);
			if (rowFormatonList == null) {
				rowFormatonList = new ArrayList<RobotFormationT>();
			}

			rowFormatonList.add(formation);
			arenaRobotFormationMap.put(formation.pos, rowFormatonList);
		}

		// 掠夺 使用 机器人 部队 前、中、后 排的位置
		for (RobotFormationT formation : ExcelParser.parse("掠夺机器人", RobotFormationT.class)) {
			List<RobotFormationT> rowFormatonList = robRobotFormationMap.get(formation.pos);
			if (rowFormatonList == null) {
				rowFormatonList = new ArrayList<RobotFormationT>();
			}

			rowFormatonList.add(formation);
			robRobotFormationMap.put(formation.pos, rowFormatonList);
		}

		// 群雄机器人 部队 前、中、后 排的位置
		for (LadderRobotT formation : ExcelParser.parse(LadderRobotT.class)) {
			formation.parseAll();
			List<LadderRobotT> rowFormatonList = ladderRobotMap.get(formation.pos);
			if (rowFormatonList == null) {
				rowFormatonList = new ArrayList<LadderRobotT>();
				ladderRobotMap.put(formation.pos, rowFormatonList);
			}
			rowFormatonList.add(formation);
		}

		loadAreanRankRobotT();
		loadAllLadderRobot();
	}

	public void loadAreanRankRobotT() {
		arenaRankRobotTList = ExcelParser.parse(ArenaRankRobotT.class);
		Collections.sort(arenaRankRobotTList, new Comparator<ArenaRankRobotT>() {
			@Override
			public int compare(ArenaRankRobotT o1, ArenaRankRobotT o2) {
				return Integer.valueOf(o1.id).compareTo(o2.id);
			}
		});
		for (ArenaRankRobotT robotT : arenaRankRobotTList) {
			robotT.parseLevelArray();
			robotT.parseStarLevelArray();
			robotT.parseQLevelArray();
			robotT.parseRoleLevelArray();
		}
	}

	public ArenaRankRobotT getRandomRobotT(int rank) {
		for (ArenaRankRobotT t : arenaRankRobotTList) {
			if (t.startRank <= rank && rank < t.endRank) {
				return t;
			}
		}

		// 默认返回最低档
		return arenaRankRobotTList.get(0);
	}

	/**
	 * 创建机器人, 用于兼容老接口, level 参数没有用
	 * 
	 * @param name
	 * @param sex
	 * @param level
	 * @return
	 */
	@Deprecated
	public RobotConfig createRobot(String name, int sex, int level,
			Map<Integer, List<RobotFormationT>> robotFormationMap) {
		ArenaRankRobotT robotT = getRandomRobotT(XsgGameParamManager.getInstance().getArenaStartRank());
		return createRobot(robotT, name, sex, robotFormationMap);
	}

	/**
	 * 创建机器人
	 * 
	 * @param name
	 * @param sex
	 * @param level
	 * @return
	 */
	public RobotConfig createRobot(int rank, String name, int sex, Map<Integer, List<RobotFormationT>> robotFormationMap) {
		ArenaRankRobotT robotT = getRandomRobotT(rank);
		return createRobot(robotT, name, sex, robotFormationMap);
	}

	/**
	 * 创建机器人
	 * 
	 * @param name
	 * @param sex
	 * @param level
	 * @return
	 */
	public RobotConfig createRobot(ArenaRankRobotT robotT, String name, int sex,
			Map<Integer, List<RobotFormationT>> robotFormationMap) {
		RobotConfig robot = new RobotConfig();
		robot.name = name;
		robot.sex = sex;
		robot.level = robotT.randomRoleLevel();
		robot.headImage = XsgRoleManager.getInstance().randomHeadImage(sex);
		robot.formationMap = this.createFormation(robotT, robotFormationMap);

		return robot;
	}

	/**
	 * 创建群雄机器人
	 * 
	 * @param name
	 * @param sex
	 * @param level
	 * @return
	 */
	public RobotConfig createLadderRobot(String name, int sex) {
		RobotConfig robot = new RobotConfig();
		robot.robotType = 1;
		robot.name = name;
		robot.sex = sex;
		robot.level = 40;// 写死40级
		robot.vipLevel = NumberUtil.random(3, 8);
		robot.headImage = XsgRoleManager.getInstance().randomHeadImage(sex);

		Map<Integer, RobotHero> formationMap = new HashMap<Integer, RobotHero>(this.formationNum);

		// 随机取得武将
		Map<Integer, LadderRobotT> randomHeroMap = new HashMap<Integer, LadderRobotT>(this.formationNum);

		int totalCount = 0;
		// 前排 武将 随机取得
		List<LadderRobotT> rowList = this.ladderRobotMap.get(this.post0);
		totalCount = rowList.size();
		int rowNum = NumberUtil.random(2) + 1;
		int[] formationIndexArr = NumberUtil.randomArry(rowNum, 0, totalCount);
		for (int i = 0; i < rowNum; i++) {
			randomHeroMap.put(this.post0 + i, rowList.get(formationIndexArr[i]));
		}

		// 中排 武将 随机取得
		rowList = this.ladderRobotMap.get(this.post3);
		totalCount = rowList.size();
		rowNum = NumberUtil.random(2) + 1;
		formationIndexArr = NumberUtil.randomArry(rowNum, 0, totalCount);
		for (int i = 0; i < rowNum; i++) {
			randomHeroMap.put(this.post3 + i, rowList.get(formationIndexArr[i]));
		}

		// 后排排 武将 随机取得
		rowList = this.ladderRobotMap.get(this.post6);
		totalCount = rowList.size();
		rowNum = this.formationNum - randomHeroMap.size();
		formationIndexArr = NumberUtil.randomArry(rowNum, 0, totalCount);
		for (int i = 0; i < rowNum; i++) {
			randomHeroMap.put(this.post6 + i, rowList.get(formationIndexArr[i]));
		}

		// 设置部队中 武将属性
		for (Entry<Integer, LadderRobotT> entry : randomHeroMap.entrySet()) {
			LadderRobotT robotT = entry.getValue();
			RobotHero hero = new RobotHero();
			hero.id = robotT.hId;
			hero.name = robotT.name;
			hero.level = robotT.randomLevel();
			hero.star = robotT.randomStarLevel();
			hero.qLevel = robotT.randomQLevel();
			// 装备
			EquipConfig e = new EquipConfig();
			e.idRange = robotT.equipHead;
			e.starRange = robotT.equipHeadStar;
			e.level = robotT.equipHeadLevel;
			hero.equips = new EquipConfig[] { e };
			formationMap.put(entry.getKey(), hero);
		}

		robot.formationMap = formationMap;

		return robot;
	}

	/**
	 * 创建机器人部队
	 * 
	 * @param level
	 * @return
	 */
	public Map<Integer, RobotHero> createFormation(ArenaRankRobotT robotT,
			Map<Integer, List<RobotFormationT>> robotFormationMap) {
		Map<Integer, RobotHero> formationMap = new HashMap<Integer, RobotHero>(this.formationNum);

		// 随机取得武将
		Map<Integer, RobotFormationT> randomHeroMap = new HashMap<Integer, RobotFormationT>(this.formationNum);

		int totalCount = 0;
		// 前排 武将 随机取得
		List<RobotFormationT> rowList = robotFormationMap.get(this.post0);
		totalCount = rowList.size();
		int rowNum = NumberUtil.random(2) + 1;
		int[] formationIndexArr = NumberUtil.randomArry(rowNum, 0, totalCount);
		for (int i = 0; i < rowNum; i++) {
			randomHeroMap.put(this.post0 + i, rowList.get(formationIndexArr[i]));
		}

		// 中排 武将 随机取得
		rowList = robotFormationMap.get(this.post3);
		totalCount = rowList.size();
		rowNum = NumberUtil.random(2) + 1;
		formationIndexArr = NumberUtil.randomArry(rowNum, 0, totalCount);
		for (int i = 0; i < rowNum; i++) {
			randomHeroMap.put(this.post3 + i, rowList.get(formationIndexArr[i]));
		}

		// 后排排 武将 随机取得
		rowList = robotFormationMap.get(this.post6);
		totalCount = rowList.size();
		rowNum = this.formationNum - randomHeroMap.size();
		formationIndexArr = NumberUtil.randomArry(rowNum, 0, totalCount);
		for (int i = 0; i < rowNum; i++) {
			randomHeroMap.put(this.post6 + i, rowList.get(formationIndexArr[i]));
		}

		// 设置部队中 武将属性
		for (int key : randomHeroMap.keySet()) {
			formationMap.put(key, this.setHero(randomHeroMap.get(key), robotT));
		}

		return formationMap;
	}

	/*
	 * 机器人模板转换成机器人数据
	 */
	private RobotHero setHero(RobotFormationT formationT, ArenaRankRobotT robotT) {
		RobotHero hero = new RobotHero();
		hero.id = formationT.hId;
		hero.name = formationT.name;
		hero.level = robotT.randomLevel();
		hero.star = robotT.randomStarLevel();
		hero.qLevel = robotT.randomQLevel();
		hero.equips = formationT.equips;

		return hero;
	}

	/**
	 * 从一个以英文逗号为分割的字符串中随机取一个,如传入字符串格式：“1,ge,3”，可能返回ge
	 * 
	 * @param randomStr
	 * @return
	 */
	// public static String randomRobotConfig(String randomStr) {
	// String[] strArr = randomStr.split(",");
	// String resStr = strArr[NumberUtil.random(strArr.length)];
	//
	// return resStr;
	// }

	public void loadRobot(final int limit, final LoadRobotCallback cb) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				RoleDAO dao = RoleDAO.getFromApplicationContext(ServerLancher.getAc());
				final List<Role> robots = dao.findByAccountWithLimit(XsgRoleManager.Robot_Account, limit);
				final List<IRole> robotsRoles = new ArrayList<IRole>();
				if (robots != null) {
					for (Role r : robots) {
						IRole ir = XsgRoleManager.getInstance().loadRole(r, false);
						robotsRoles.add(ir);
					}
					LogicThread.execute(new Runnable() {
						@Override
						public void run() {
							cb.onLoadRobot(robotsRoles);
						}
					});
				}
			}
		});
	}

	/**
	 * 加载群雄机器人ID
	 */
	public void loadAllLadderRobot() {
		ladderRobotIds.clear();
		RoleDAO dao = RoleDAO.getFromApplicationContext(ServerLancher.getAc());
		ladderRobotIds = dao.findAllLadderRobot(XsgRoleManager.Robot_Account);

		if (ladderRobotIds.size() < 100) {
			// 已经存在角色的名字
			List<String> roleNameList = dao.findByNameAll();
			// 随机生成的名字，保证不重复
			List<String> randomNameList = new ArrayList<String>();
			while (true) {
				String randomName = XsgRoleManager.getInstance().generateRandomName(NumberUtil.random(100));
				if (!randomNameList.contains(randomName) && !roleNameList.contains(randomName)) {
					randomNameList.add(randomName);
				}
				if (randomNameList.size() > 50) {
					break;
				}
			}
			for (int i = 0; i < 50; i++) {
				RobotConfig robotConfig = createLadderRobot(randomNameList.get(i), i % 2);
				IRole robot = newRobot(robotConfig);
				ladderRobotIds.add(robot.getRoleId());
				robot.saveAsyn();
			}
		}
	}

	/**
	 * 根据机器人配置生成对应角色数据
	 * 
	 * @param robotConfig
	 * @return
	 */
	public IRole newRobot(RobotConfig robotConfig) {
		NewRoleConfig config = new NewRoleConfig();
		config.level = robotConfig.level;
		Role db = XsgRoleManager.getInstance().newRole(XsgRoleManager.Robot_Account, 0, robotConfig.name, config,
				ServerLancher.getServerId());
		// 机器人不放入缓存管理
		IRole result = XsgRoleManager.getInstance().loadRole(db, false);
		String formationId = result.getFormationControler().getDefaultFormation().getId();
		result.setSex(robotConfig.sex);
		result.setHeadImage(robotConfig.headImage);
		result.setRobotType(robotConfig.robotType);
		result.getVipController().setVipLevel(robotConfig.vipLevel);
		for (int pos : robotConfig.formationMap.keySet()) {
			RobotHero heroConfig = robotConfig.formationMap.get(pos);
			HeroT heroT = XsgHeroManager.getInstance().getHeroT(heroConfig.id);
			IHero hero = result.getHeroControler().addHero(heroT, HeroSource.Init);
			hero.setLevel(heroConfig.level);
			hero.setStar(heroConfig.star);
			hero.setColor(heroConfig.qLevel);
			try {
				result.getFormationControler().setFormationPosition(formationId, (byte) pos, hero, true);
			} catch (NoteException e) {
				LogManager.error(e);
			}

			for (EquipConfig ec : heroConfig.equips) {
				if (!ec.randomEquipId().equals("")) {
					EquipItem equip = (EquipItem) result.getItemControler()
							.changeItemByTemplateCode(ec.randomEquipId(), 1).get(0);
					equip.setStar((byte) ec.randomEquipStar());
					equip.setLevel(ec.level);
					try {
						result.getHeroControler().setHeroEquip(hero.getId(), equip);
					} catch (NoteException e) {
						LogManager.error(e);
					}
				}
			}
		}

		return result;
	}

	public static interface LoadRobotCallback {
		void onLoadRobot(List<IRole> list);
	}
}

package com.morefun.XSanGo.sns;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleDAO;
import com.morefun.XSanGo.db.game.RoleSns;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.robot.EquipConfig;
import com.morefun.XSanGo.robot.RobotConfig;
import com.morefun.XSanGo.robot.RobotHero;
import com.morefun.XSanGo.robot.XsgRobotManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.TextUtil;

public class XsgSnsManager {
	
	public static IRole roleOfMsLing = null;
	
	private static XsgSnsManager manager = new XsgSnsManager();

	public final String NAME_OF_MS_LING = Messages.getString("XsgSnsManager.msLingName");

	public static XsgSnsManager getInstance() {
		return manager;
	}

	Map<Integer, SnsT> snsT = new HashMap<Integer, SnsT>();

	Map<Integer, SnsJunLingT> snsJunLingT = new HashMap<Integer, SnsJunLingT>();

	private XsgSnsManager() {
		List<SnsT> snsTable = ExcelParser.parse(SnsT.class);
		for (SnsT snst : snsTable) {
			snsT.put(snst.Lv, snst);
		}
		loadSnsJunLingScript();
		processMsLingRobot();
	}

	public void loadSnsJunLingScript() {
		List<SnsJunLingT> snsJunLingList = ExcelParser.parse(SnsJunLingT.class);
		if (snsJunLingList != null) {
			this.snsJunLingT.clear();

			for (SnsJunLingT sj : snsJunLingList) {
				snsJunLingT.put(sj.vipLv, sj);
			}
		}
	}

	/**
	 * 处理小林志玲机器人
	 * 
	 */
	private void processMsLingRobot() {
		RoleDAO dao = RoleDAO.getFromApplicationContext(ServerLancher.getAc());
		Role role = dao.findByName(NAME_OF_MS_LING);
		if (role != null) {
			roleOfMsLing = XsgRoleManager.getInstance().loadRole(role, false);
			return;
		}
		
		// 生成机器人
		RobotConfig robotConfig = new RobotConfig();
		robotConfig.name = this.NAME_OF_MS_LING;
		robotConfig.sex = 0;
		robotConfig.level = 90;
		robotConfig.headImage = "female009";

		Map<Integer, RobotHero> formationMap = new HashMap<Integer, RobotHero>();
		formationMap.put(1, newHero(2506, Messages.getString("XsgSnsManager.msLingHero1")));
		formationMap.put(3, newHero(1506, Messages.getString("XsgSnsManager.msLingHero2")));
		formationMap.put(5, newHero(4505, Messages.getString("XsgSnsManager.msLingHero3")));
		formationMap.put(6, newHero(3402, Messages.getString("XsgSnsManager.msLingHero4")));
		formationMap.put(8, newHero(2404, Messages.getString("XsgSnsManager.msLingHero5")));
		formationMap.put(9, newHero(3504, Messages.getString("XsgSnsManager.msLingHero6")));
		formationMap.put(10, newHero(3403, Messages.getString("XsgSnsManager.msLingHero7")));
		formationMap.put(11, newHero(3405, Messages.getString("XsgSnsManager.msLingHero8")));
		robotConfig.formationMap = formationMap;

		IRole robot = XsgRobotManager.getInstance().newRobot(robotConfig);
		robot.getVipController().setVipLevel(15);
		for(IHero hero:robot.getHeroControler().getAllHero()){
			hero.setBreakLevel(5);
			hero.setColor(10);
		}
		roleOfMsLing = robot;
		robot.saveAsyn();
	}

	/**
	 * @param collection 玩家的好友，仇人，黑名单存在了一个集合里 用这个方法分类
	 * @param type
	 * @return 好友，仇人，黑名单对应的枚举
	 */
	public Collection<String> grep(Collection<RoleSns> collection, SNSType type) {
		HashSet<String> c = new HashSet<String>();
		for (RoleSns sns : collection) {
			if (sns.getRelationType() == type.getValue()) {
				c.add(sns.getTargetRoleId());
			}
		}
		return c;
	}

	/**
	 * @param collection 玩家的好友，仇人，黑名单存在了一个集合里 用这个方法分类
	 * @param type
	 * @param id
	 * @return 好友，仇人，黑名单对应的枚举
	 */
	public RoleSns grep(Collection<RoleSns> collection, SNSType type, String id) {
		if (!TextUtil.isBlank(id)) {
			for (RoleSns sns : collection) {
				if (id.equals(sns.getTargetRoleId()) && sns.getRelationType() == type.getValue()) {
					return sns;
				}
			}
		}
		return null;
	}

	public SnsJunLingT getSnsJunLingT(int vipLv) {
		return snsJunLingT.get(vipLv);
	}

	private RobotHero newHero(int heroId, String name) {
		RobotHero hero = new RobotHero();
		hero.id = heroId;
		hero.name = name;
		hero.level = 180;
		hero.star = 5;
		hero.qLevel = 1;
		hero.equips = new EquipConfig[0];
		return hero;
	}
}

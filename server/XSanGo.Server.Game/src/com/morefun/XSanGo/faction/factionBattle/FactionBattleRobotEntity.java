/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattleRobotEntity
 * 功能描述：
 * 文件名：FactionBattleRobotEntity.java
 **************************************************
 */
package com.morefun.XSanGo.faction.factionBattle;

import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.db.game.FactionBattleRobot;
import com.morefun.XSanGo.db.game.SimpleDAO;

/**
 * 公会战机器人实体数据
 * 
 * @author weiyi.zhao
 * @since 2016-5-7
 * @version 1.0
 */
public class FactionBattleRobotEntity {

	/** 是否战斗中 */
	private boolean isBattle;

	/** 机器人名称 */
	private String robotName;

	/** 机器人数据 */
	private FactionBattleRobot robot;

	/**
	 * 构造函数
	 * 
	 * @param robot
	 * @param robotName
	 */
	public FactionBattleRobotEntity(FactionBattleRobot robot, String robotName) {
		this.robot = robot;
		this.robotName = robotName;
		this.isBattle = false;
	}

	/**
	 * @return Returns the isBattle.
	 */
	public boolean isBattle() {
		return isBattle;
	}

	/**
	 * @param isBattle The isBattle to set.
	 */
	public void setBattle(boolean isBattle) {
		this.isBattle = isBattle;
	}

	/**
	 * @return Returns the robotName.
	 */
	public String getRobotName() {
		return robotName;
	}

	/**
	 * @param robotName The robotName to set.
	 */
	public void setRobotName(String robotName) {
		this.robotName = robotName;
	}

	/**
	 * @return Returns the robot.
	 */
	public FactionBattleRobot getRobot() {
		return robot;
	}

	/**
	 * @param robot The robot to set.
	 */
	public void setRobot(FactionBattleRobot robot) {
		this.robot = robot;
	}

	/**
	 * 保存机器人数据
	 */
	public void save() {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				SimpleDAO.getFromApplicationContext(ServerLancher.getAc()).attachDirty(robot);
			}
		});
	}

	/**
	 * 清除机器人数据
	 */
	public void delete() {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				SimpleDAO.getFromApplicationContext(ServerLancher.getAc()).delete(robot);
			}
		});
	}
}

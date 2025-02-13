/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: StrongHold
 * 功能描述：
 * 文件名：StrongHold.java
 **************************************************
 */
package com.morefun.XSanGo.faction.factionBattle;

import java.util.ArrayList;
import java.util.List;

import com.morefun.XSanGo.db.game.FactionBattleRobot;

/**
 * 据点对象
 * 
 * @author zwy
 * @since 2016-1-8
 * @version 1.0
 */
public class StrongHold {

	/** 守方角色列表 */
	private List<String> defendRoleList = new ArrayList<String>();

	/** 攻方角色列表 */
	private List<String> attackRoleList = new ArrayList<String>();

	/** 守方机器人列表 */
	private List<FactionBattleRobotEntity> defendRobotList = new ArrayList<FactionBattleRobotEntity>();

	/** 占领的阵营 */
	private int occupyCampId;

	@Override
	public String toString() {
		return FactionBattleUtil.object2string(this);
	}

	/**
	 * @return Returns the defendRoleList.
	 */
	public List<String> getDefendRoleList() {
		return defendRoleList;
	}

	/**
	 * @return Returns the attackRoleList.
	 */
	public List<String> getAttackRoleList() {
		return attackRoleList;
	}

	/**
	 * 添加守方角色
	 * 
	 * @param roleId
	 */
	public void addDefendRole(String roleId) {
		this.defendRoleList.add(roleId);
	}

	/**
	 * 移除守方角色
	 * 
	 * @param roleId
	 */
	public void removeDefendRole(String roleId) {
		this.defendRoleList.remove(roleId);
	}

	/**
	 * 添加攻方角色
	 * 
	 * @param roleId
	 */
	public void addAttackRole(String roleId) {
		this.attackRoleList.add(roleId);
	}

	/**
	 * 移除攻方角色
	 * 
	 * @param roleId
	 */
	public void removeAttackRole(String roleId) {
		this.attackRoleList.remove(roleId);
	}

	/**
	 * @return Returns the defendRobotList.
	 */
	public List<FactionBattleRobotEntity> getDefendRobotList() {
		return defendRobotList;
	}

	/**
	 * 添加机器人
	 * 
	 * @param robot
	 * @param robotName
	 */
	public void addDefendRobotList(FactionBattleRobot robot, String robotName) {
		this.defendRobotList.add(new FactionBattleRobotEntity(robot, robotName));
	}

	/**
	 * 获得占领的阵营编号
	 * 
	 * @return
	 */
	public int getOccupyCampId() {
		return occupyCampId;
	}

	/**
	 * 占领状态
	 * 
	 * @return
	 */
	public int occupyState() {
		if (!defendRobotList.isEmpty()) {
			return 4;// 怪物占领
		}
		return convertCampId(occupyCampId);
	}

	/**
	 * 阵营编号转换
	 * 
	 * @param campId
	 * @return
	 */
	public int convertCampId(int campId) {
		switch (campId) {
			case 1001:
				return 1;// 许昌阵营
			case 1002:
				return 2;// 成都阵营
			case 1003:
				return 3;// 建业阵营
			default:
				return 0;// 无人占领
		}
	}

	/**
	 * 占领
	 * 
	 * @param campId 阵营编号
	 */
	public void occupy(int campId) {
		occupyCampId = campId;
	}

	/**
	 * 重置为无人占领
	 */
	public void resetOccupy() {
		occupyCampId = 0;
	}

	/**
	 * 是否能否进行占领
	 * 
	 * @return
	 */
	public boolean isCanOccupy() {
		return defendRobotList.isEmpty() && defendRoleList.isEmpty();
	}

	/**
	 * 成员数
	 * 
	 * @return
	 */
	public int number() {
		return defendRobotList.size() + defendRoleList.size() + attackRoleList.size();
	}
}

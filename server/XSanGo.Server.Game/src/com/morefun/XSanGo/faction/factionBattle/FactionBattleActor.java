/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattleActor
 * 功能描述：
 * 文件名：FactionBattleActor.java
 **************************************************
 */
package com.morefun.XSanGo.faction.factionBattle;

import java.util.Date;

import com.morefun.XSanGo.db.game.FactionBattleMember;
import com.morefun.XSanGo.faction.IFaction;
import com.morefun.XSanGo.role.IRole;

/**
 * 公会战参战成员信息
 * 
 * @author weiyi.zhao
 * @since 2016-5-7
 * @version 1.0
 */
public class FactionBattleActor {

	/** 自身角色参与者信息 */
	private IRole role;

	/** 是否机器人 */
	private boolean isRobot;

	/** 机器人对象 */
	private FactionBattleRobotEntity robotEntity;

	/**
	 * 构造函数
	 * 
	 * @param role
	 */
	public FactionBattleActor(IRole role) {
		this.role = role;
	}

	/**
	 * 构造函数
	 * 
	 * @param role
	 * @param robotEntity
	 */
	public FactionBattleActor(IRole role, FactionBattleRobotEntity robotEntity) {
		this(role);
		this.robotEntity = robotEntity;
		this.isRobot = robotEntity != null;
	}

	/**
	 * @return Returns the role.
	 */
	public IRole getRole() {
		return role;
	}

	/**
	 * @return Returns the isRobot.
	 */
	public boolean isRobot() {
		return isRobot;
	}

	/**
	 * 公会战处理句柄
	 * 
	 * @return
	 */
	IFactionBattleController getFactionBattleController() {
		return role.getFactionBattleController();
	}

	/**
	 * 是否在线
	 * 
	 * @return
	 */
	public boolean isOnline() {
		return isRobot ? false : role.isOnline();
	}

	/**
	 * 设置战斗中
	 */
	public void setBattle() {
		if (isRobot) {
			robotEntity.setBattle(true);
		} else {
			role.getFactionBattleController().setBattleing(true);
		}
	}

	/**
	 * 取消战斗中
	 */
	public void cancelBattle() {
		if (isRobot) {
			robotEntity.setBattle(false);
		} else {
			role.getFactionBattleController().setBattleing(false);
		}
	}

	/**
	 * 是否战斗中
	 * 
	 * @return
	 */
	public boolean isBattle() {
		if (isRobot) {
			return robotEntity.isBattle();
		}
		return role.getFactionBattleController().isBattleing();
	}

	/**
	 * 返回机器人名称
	 * 
	 * @return
	 */
	public String getRobotName() {
		return isRobot ? robotEntity.getRobotName() : "";
	}

	/**
	 * 参与者角色编号
	 * 
	 * @return
	 */
	public String getActorRoleId() {
		return role.getRoleId();
	}

	/**
	 * 参与者名称
	 * 
	 * @return
	 */
	public String getActorName() {
		return isRobot ? robotEntity.getRobotName() : role.getName();
	}

	/**
	 * 阵营名称
	 * 
	 * @return
	 */
	public String getCampName() {
		if (isRobot) {// 机器人无阵营
			return "";
		}
		int campStrongholdId = role.getFactionControler().getMyFaction().getFactionBattle().getCampStrongholdId();
		FactionBattleSceneT sceneT = XsgFactionBattleManager.getInstance().getBattleSceneT(campStrongholdId);
		return sceneT.name;
	}

	/**
	 * 参与者VIP色值表达式
	 * 
	 * @return
	 */
	public String getVipColor() {
		if (isRobot) {
			return "";
		}
		return "ff" + role.getVipController().getVipColor();
	}

	/**
	 * 参与者公会名称
	 * 
	 * @return
	 */
	public String getFactionName() {
		if (isRobot) {
			return "";
		}
		IFaction faction = role.getFactionControler().getMyFaction();
		return faction.getName();
	}

	/**
	 * 参战数据
	 * 
	 * @return
	 */
	public FactionBattleMember getFbm() {
		if (isRobot) {// 机器人无参战数据
			return null;
		}
		IFaction faction = role.getFactionControler().getMyFaction();
		return faction.getFactionBattleMember(role.getRoleId());
	}

	/**
	 * 设置受击等待时间
	 * 
	 * @param time
	 */
	public void setBeAttackWaitEndTime(Date time) {
		if (isRobot) {
			robotEntity.getRobot().setBeAttackWaitEndTime(time);
			robotEntity.save();
		} else {
			getFbm().setBeAttackWaitEndTime(time);
		}
	}

	/**
	 * 返回参战者debuff等级
	 * 
	 * @return
	 */
	public int getDeBuffLvl() {
		if (isRobot) {
			return robotEntity.getRobot().getDebuffLvl();
		} else {
			return getFbm().getDeBuffLvl();
		}
	}

	/**
	 * 增加DEBUFF
	 * 
	 * @param lvl
	 */
	public void addDebuffLvl(int lvl) {
		if (isRobot) {
			robotEntity.getRobot().setDebuffLvl(getDeBuffLvl() + lvl);
			robotEntity.save();
		} else {
			getFbm().setDeBuffLvl(getDeBuffLvl() + lvl);
		}
	}

	/**
	 * 清除机器人
	 */
	public void clearRobot() {
		if (!isRobot) {
			return;
		}
		int strongholdId = robotEntity.getRobot().getStrongholdId();
		StrongHold sh = XsgFactionBattleManager.getInstance().getStrongHold(strongholdId);
		sh.getDefendRobotList().remove(robotEntity);
		//robotEntity.delete();
		XsgFactionBattleManager.getInstance().addRemovedRobot(robotEntity.getRobot());
	}
}

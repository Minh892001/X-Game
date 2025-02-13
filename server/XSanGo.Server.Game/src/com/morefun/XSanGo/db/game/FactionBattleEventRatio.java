/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattleEventRatio
 * 功能描述：
 * 文件名：FactionBattleEventRatio.java
 **************************************************
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 公会战随机事件挖宝次数数据
 * 
 * @author zwy
 * @since 2016-1-22
 * @version 1.0
 */
@Entity
@Table(name = "faction_battle_event_ratio")
public class FactionBattleEventRatio implements Serializable {
	private static final long serialVersionUID = 7923481044870312336L;

	/** 唯一编号 */
	private String id;

	/** 角色 */
	private String roleId;

	/** 据点 */
	private int strongholdId;

	/** 随机次数 */
	private int randomNum;

	/**
	 * @return Returns the id.
	 */
	@Id
	@Column(name = "id")
	public String getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return Returns the roleId.
	 */
	@Column(name = "role_id")
	public String getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId The roleId to set.
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return Returns the strongholdId.
	 */
	@Column(name = "stronghold_id")
	public int getStrongholdId() {
		return strongholdId;
	}

	/**
	 * @param strongholdId The strongholdId to set.
	 */
	public void setStrongholdId(int strongholdId) {
		this.strongholdId = strongholdId;
	}

	/**
	 * @return Returns the randomNum.
	 */
	@Column(name = "random_num")
	public int getRandomNum() {
		return randomNum;
	}

	/**
	 * @param randomNum The randomNum to set.
	 */
	public void setRandomNum(int randomNum) {
		this.randomNum = randomNum;
	}
}

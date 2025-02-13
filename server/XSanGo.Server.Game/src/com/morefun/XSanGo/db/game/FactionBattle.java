/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattle
 * 功能描述：
 * 文件名：FactionBattle.java
 **************************************************
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * 公会战数据
 * 
 * @author zwy
 * @since 2015-12-31
 * @version 1.0
 */
@Entity
@Table(name = "faction_battle")
public class FactionBattle implements Serializable {
	private static final long serialVersionUID = 4458828088986593038L;

	/** 公会对象 */
	private Faction faction;

	/** 公会编号 */
	private String factionId;

	/** 报名时间 */
	private Date enrollTime;

	/** 分配的阵营据点编号 */
	private int campStrongholdId;

	/** 报名人 */
	private String enrollRoleId;

	/** 本场公会战获得徽章 */
	private int badge;

	/** 粮草 */
	private int forage;

	/** 击杀数 */
	private int killNum;

	/** 更新时间 */
	private Date updateTime;

	/**
	 * @return Returns the faction.
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Faction getFaction() {
		return faction;
	}

	/**
	 * @param faction The faction to set.
	 */
	public void setFaction(Faction faction) {
		this.faction = faction;
	}

	/**
	 * @return Returns the factionId.
	 */
	@Id
	@Column(name = "faction_id")
	public String getFactionId() {
		return factionId;
	}

	/**
	 * @param factionId The factionId to set.
	 */
	public void setFactionId(String factionId) {
		this.factionId = factionId;
	}

	/**
	 * @return Returns the enrollTime.
	 */
	@Column(name = "enroll_time", nullable = false, length = 19)
	public Date getEnrollTime() {
		return enrollTime;
	}

	/**
	 * @param enrollTime The enrollTime to set.
	 */
	public void setEnrollTime(Date enrollTime) {
		this.enrollTime = enrollTime;
	}

	/**
	 * @return Returns the campStrongholdId.
	 */
	@Column(name = "camp_stronghold_id")
	public int getCampStrongholdId() {
		return campStrongholdId;
	}

	/**
	 * @param campStrongholdId The campStrongholdId to set.
	 */
	public void setCampStrongholdId(int campStrongholdId) {
		this.campStrongholdId = campStrongholdId;
	}

	/**
	 * @return Returns the enrollRoleId.
	 */
	@Column(name = "enroll_role_id")
	public String getEnrollRoleId() {
		return enrollRoleId;
	}

	/**
	 * @param enrollRoleId The enrollRoleId to set.
	 */
	public void setEnrollRoleId(String enrollRoleId) {
		this.enrollRoleId = enrollRoleId;
	}

	/**
	 * @return Returns the badge.
	 */
	@Column(name = "badge")
	public int getBadge() {
		return badge;
	}

	/**
	 * @param badge The badge to set.
	 */
	public void setBadge(int badge) {
		this.badge = badge;
	}

	/**
	 * @return Returns the forage.
	 */
	@Column(name = "forage")
	public int getForage() {
		return forage;
	}

	/**
	 * @param forage The forage to set.
	 */
	public void setForage(int forage) {
		this.forage = forage;
	}

	/**
	 * @return Returns the killNum.
	 */
	@Column(name = "kill_num")
	public int getKillNum() {
		return killNum;
	}

	/**
	 * @param killNum The killNum to set.
	 */
	public void setKillNum(int killNum) {
		this.killNum = killNum;
	}

	/**
	 * @return Returns the updateTime.
	 */
	@Column(name = "update_time", nullable = true, length = 19)
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime The updateTime to set.
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}

/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattleMember
 * 功能描述：
 * 文件名：FactionBattleMember.java
 **************************************************
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 公会战参战成员数据
 * 
 * @author zwy
 * @since 2015-12-31
 * @version 1.0
 */
@Entity
@Table(name = "faction_battle_member")
public class FactionBattleMember implements Serializable {
	private static final long serialVersionUID = 8511786552673803752L;

	/** 公会对象 */
	private Faction faction;
	
	/** 角色编号 */
	private String roleId;

	/** 据点编号 */
	private int strongholdId;

	/** 进入据点时间 */
	private Date enterStrongholdTime;

	/** 攻击等待结束时间 */
	private Date attackWaitEndTime;

	/** 被攻击等待结束时间 */
	private Date beAttackWaitEndTime;

	/** 复活结束时间 */
	private Date reliveEndTime;

	/** 行军冷却结束时间 */
	private Date marchingCoolingEndTime;

	/** 立即结束行军冷却CD次数 */
	private int marchingCoolingCDNum;

	/** 挖宝冷却结束时间 */
	private Date diggingTreasureEndTime;

	/** 单次挖宝减CD次数 */
	private int diggingTreasureCDNum;

	/** DEBUFF等级 */
	private int deBuffLvl;

	/** 战场特殊道具 */
	private String items;

	/** 公会战个人累计徽章 */
	private int badge;

	/** 个人累计粮草 */
	private int forage;

	/** 击杀数 */
	private int killNum;

	/** 连杀数 */
	private int evenkillNumm;

	/** 死亡次数 */
	private int deaths;
	
	/** 更新时间 */
	private Date updateTime;

	/** 参战时间 */
	private Date joinTime;

	/**
	 * @return Returns the faction.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "faction_id", nullable = false)
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
	 * @return Returns the roleId.
	 */
	@Id
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
	 * @return Returns the enterStrongholdTime.
	 */
	@Column(name = "enter_stronghold_time", nullable = false, length = 19)
	public Date getEnterStrongholdTime() {
		return enterStrongholdTime;
	}

	/**
	 * @param enterStrongholdTime The enterStrongholdTime to set.
	 */
	public void setEnterStrongholdTime(Date enterStrongholdTime) {
		this.enterStrongholdTime = enterStrongholdTime;
	}

	/**
	 * @return Returns the attackWaitEndTime.
	 */
	@Column(name = "attack_wait_endtime", nullable = true, length = 19)
	public Date getAttackWaitEndTime() {
		return attackWaitEndTime;
	}

	/**
	 * @param attackWaitEndTime The attackWaitEndTime to set.
	 */
	public void setAttackWaitEndTime(Date attackWaitEndTime) {
		this.attackWaitEndTime = attackWaitEndTime;
	}

	/**
	 * @return Returns the beAttackWaitEndTime.
	 */
	@Column(name = "be_attack_wait_endtime", nullable = true, length = 19)
	public Date getBeAttackWaitEndTime() {
		return beAttackWaitEndTime;
	}

	/**
	 * @param beAttackWaitEndTime The beAttackWaitEndTime to set.
	 */
	public void setBeAttackWaitEndTime(Date beAttackWaitEndTime) {
		this.beAttackWaitEndTime = beAttackWaitEndTime;
	}

	/**
	 * @return Returns the reliveEndTime.
	 */
	@Column(name = "relive_endtime", nullable = true, length = 19)
	public Date getReliveEndTime() {
		return reliveEndTime;
	}

	/**
	 * @param reliveEndTime The reliveEndTime to set.
	 */
	public void setReliveEndTime(Date reliveEndTime) {
		this.reliveEndTime = reliveEndTime;
	}

	/**
	 * @return Returns the marchingCoolingEndTime.
	 */
	@Column(name = "marching_cooling_endtime", nullable = true, length = 19)
	public Date getMarchingCoolingEndTime() {
		return marchingCoolingEndTime;
	}

	/**
	 * @param marchingCoolingEndTime The marchingCoolingEndTime to set.
	 */
	public void setMarchingCoolingEndTime(Date marchingCoolingEndTime) {
		this.marchingCoolingEndTime = marchingCoolingEndTime;
	}

	/**
	 * @return Returns the marchingCoolingCDNum.
	 */
	@Column(name = "marching_cooling_cd_num")
	public int getMarchingCoolingCDNum() {
		return marchingCoolingCDNum;
	}

	/**
	 * @param marchingCoolingCDNum The marchingCoolingCDNum to set.
	 */
	public void setMarchingCoolingCDNum(int marchingCoolingCDNum) {
		this.marchingCoolingCDNum = marchingCoolingCDNum;
	}

	/**
	 * @return Returns the diggingTreasureEndTime.
	 */
	@Column(name = "digging_treasure_endtime", nullable = true, length = 19)
	public Date getDiggingTreasureEndTime() {
		return diggingTreasureEndTime;
	}

	/**
	 * @param diggingTreasureEndTime The diggingTreasureEndTime to set.
	 */
	public void setDiggingTreasureEndTime(Date diggingTreasureEndTime) {
		this.diggingTreasureEndTime = diggingTreasureEndTime;
	}

	/**
	 * @return Returns the diggingTreasureCDNum.
	 */
	@Column(name = "digging_treasure_cd_num")
	public int getDiggingTreasureCDNum() {
		return diggingTreasureCDNum;
	}

	/**
	 * @param diggingTreasureCDNum The diggingTreasureCDNum to set.
	 */
	public void setDiggingTreasureCDNum(int diggingTreasureCDNum) {
		this.diggingTreasureCDNum = diggingTreasureCDNum;
	}

	/**
	 * @return Returns the deBuffLvl.
	 */
	@Column(name = "debuff_lvl")
	public int getDeBuffLvl() {
		return deBuffLvl;
	}

	/**
	 * @param deBuffLvl The deBuffLvl to set.
	 */
	public void setDeBuffLvl(int deBuffLvl) {
		this.deBuffLvl = deBuffLvl;
	}

	/**
	 * @return Returns the items.
	 */
	@Column(name = "items")
	public String getItems() {
		return items;
	}

	/**
	 * @param items The items to set.
	 */
	public void setItems(String items) {
		this.items = items;
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
	 * @return Returns the evenkillNumm.
	 */
	@Column(name = "even_kill_num")
	public int getEvenkillNumm() {
		return evenkillNumm;
	}

	/**
	 * @param evenkillNumm The evenkillNumm to set.
	 */
	public void setEvenkillNumm(int evenkillNumm) {
		this.evenkillNumm = evenkillNumm;
	}

	/**
	 * @return Returns the deaths.
	 */
	@Column(name = "deaths")
	public int getDeaths() {
		return deaths;
	}

	/**
	 * @param deaths The deaths to set.
	 */
	public void setDeaths(int deaths) {
		this.deaths = deaths;
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

	/**
	 * @return Returns the joinTime.
	 */
	@Column(name = "join_time", nullable = false, length = 19)
	public Date getJoinTime() {
		return joinTime;
	}

	/**
	 * @param joinTime The joinTime to set.
	 */
	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}
}
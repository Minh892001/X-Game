/**
 * 
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
 * 帮派成员数据
 * 
 * @author BruceSu
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "faction_member")
public class FactionMember implements Serializable {
	// Fields

	private String id;
	private Faction faction;
	private int dutyId;// 职位
	private String roleId;
	private String name;
	private int level;
	private Date participateTime;
	/** 最后一次贡献时间 */
	private Date lastTime;
	/** 帮贡 */
	private int contribution;
	/** 登录时间 */
	private Date offlineTime;
	private int challengeNum;// 副本已挑战次数
	private Date lastChallengeDate;// 最后挑战时间
	private int honor;// 荣誉
	private String buyShopIds;// 已购买的商品ID,分割
	private Date applyDate;// 公会战报名时间
	private Date deathDate;// 公会战死亡时间
	private Date gvgEndDate;// 公会战胜利时间
	private int sumCopyHarm;// 单局公会副打出的伤害
	private String donateLogs = "[]";// DonateLog[]的json
	private String demandItem;// 索要物品

	// Constructors

	/** default constructor */
	public FactionMember() {
	}

	public FactionMember(String id, Faction faction, int duty, String roleId, String name, int level,
			Date participateTime, Date offlineTime) {
		this.id = id;
		this.faction = faction;
		this.dutyId = duty;
		this.roleId = roleId;
		this.name = name;
		this.level = level;
		this.participateTime = participateTime;
		this.lastTime = (Date) participateTime.clone();
		if (offlineTime != null) {
			this.offlineTime = (Date) offlineTime.clone();
		}
	}

	// Property accessors

	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "duty_id", nullable = false)
	public int getDutyId() {
		return dutyId;
	}

	public void setDutyId(int duty) {
		this.dutyId = duty;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "faction_id", nullable = false)
	public Faction getFaction() {
		return faction;
	}

	public void setFaction(Faction faction) {
		this.faction = faction;
	}

	// 这里不设置唯一索引，是因为当玩家帮派变化时，无法简单保证先从数据库清除老帮派数据
	@Column(name = "role_id", nullable = false)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Column(name = "name", nullable = false, length = 32)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "level", nullable = false)
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Column(name = "participate_time", length = 19)
	public Date getParticipateTime() {
		return participateTime;
	}

	public void setParticipateTime(Date participateTime) {
		this.participateTime = participateTime;
	}

	@Column(name = "last_time", length = 19)
	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	@Column(name = "contribution", nullable = false)
	public int getContribution() {
		return contribution;
	}

	public void setContribution(int contribution) {
		this.contribution = contribution;
	}

	@Column(name = "offline_time", length = 19)
	public Date getOfflineTime() {
		return offlineTime;
	}

	public void setOfflineTime(Date offlineTime) {
		this.offlineTime = offlineTime;
	}

	@Column(name = "challenge_num", nullable = false)
	public int getChallengeNum() {
		return challengeNum;
	}

	public void setChallengeNum(int challengeNum) {
		this.challengeNum = challengeNum;
	}

	@Column(name = "last_challenge_date")
	public Date getLastChallengeDate() {
		return lastChallengeDate;
	}

	public void setLastChallengeDate(Date lastChallengeDate) {
		this.lastChallengeDate = lastChallengeDate;
	}

	@Column(name = "honor")
	public int getHonor() {
		return honor;
	}

	public void setHonor(int honor) {
		this.honor = honor;
	}

	@Column(name = "buy_shop_ids")
	public String getBuyShopIds() {
		return buyShopIds;
	}

	public void setBuyShopIds(String buyShopIds) {
		this.buyShopIds = buyShopIds;
	}

	@Column(name = "apply_date")
	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	@Column(name = "death_date")
	public Date getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	@Column(name = "gvg_end_date")
	public Date getGvgEndDate() {
		return gvgEndDate;
	}

	public void setGvgEndDate(Date gvgEndDate) {
		this.gvgEndDate = gvgEndDate;
	}

	@Column(name = "sum_copy_harm")
	public int getSumCopyHarm() {
		return sumCopyHarm;
	}

	public void setSumCopyHarm(int sumCopyHarm) {
		this.sumCopyHarm = sumCopyHarm;
	}

	@Column(name = "donate_logs", columnDefinition = "text")
	public String getDonateLogs() {
		return donateLogs;
	}

	public void setDonateLogs(String donateLogs) {
		this.donateLogs = donateLogs;
	}

	@Column(name = "demand_item")
	public String getDemandItem() {
		return demandItem;
	}

	public void setDemandItem(String demandItem) {
		this.demandItem = demandItem;
	}

}

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
 * 跨服竞技场
 * 
 * @author xiongming.li
 *
 */
@Entity
@Table(name = "role_cross_arena")
public class RoleCrossArena implements Serializable {
	private static final long serialVersionUID = -3912656185186992050L;
	private String roleId;
	private Role role;
	private int challenge; // 挑战次数
	private int challengeBuy; // 挑战次数 购买次数
	private Date challengeDate;// 最后挑战时间
	private Date refreshDate; // 挑战次数刷新时间
	private String crossArenaLogs = "[]";// 战斗记录CrossArenaLog[]的json

	public RoleCrossArena() {

	}

	public RoleCrossArena(Role role, String signature, int attackFightSum, int guardFightSum, int attackWinSum,
			int guardWinSum, int challenge, int challengeBuy, Date refreshDate) {
		this.roleId = role.getId();
		this.role = role;
		this.challenge = challenge;
		this.challengeBuy = challengeBuy;
		this.refreshDate = refreshDate;
	}

	@Id
	@Column(name = "role_id", nullable = false)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "challenge", nullable = false)
	public int getChallenge() {
		return challenge;
	}

	public void setChallenge(int challenge) {
		this.challenge = challenge;
	}

	@Column(name = "challenge_buy", nullable = false)
	public int getChallengeBuy() {
		return challengeBuy;
	}

	public void setChallengeBuy(int challengeBuy) {
		this.challengeBuy = challengeBuy;
	}

	@Column(name = "refresh_date")
	public Date getRefreshDate() {
		return refreshDate;
	}

	public void setRefreshDate(Date refreshDate) {
		this.refreshDate = refreshDate;
	}

	@Column(name = "cross_arena_logs", columnDefinition = "text")
	public String getCrossArenaLogs() {
		return crossArenaLogs;
	}

	public void setCrossArenaLogs(String crossArenaLogs) {
		this.crossArenaLogs = crossArenaLogs;
	}

	@Column(name = "challenge_date")
	public Date getChallengeDate() {
		return challengeDate;
	}

	public void setChallengeDate(Date challengeDate) {
		this.challengeDate = challengeDate;
	}

}

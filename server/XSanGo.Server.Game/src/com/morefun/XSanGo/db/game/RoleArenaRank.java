/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 竞技场排行 角色相关 数据库结构
 * 
 * @author 吕明涛
 * 
 */
@Entity
@Table(name = "role_arena_rank")
public class RoleArenaRank implements Serializable {

	/** */
	private static final long serialVersionUID = 7442243913909461540L;

	private String roleId;;
	private Role role;

	private String guardId; // 防守队伍
	private int sneerId; // 嘲讽类型
	private String sneerStr; // 嘲讽显示文字
	private int attackFightSum; // 进攻 战斗总数量
	private int guardFightSum; // 防守 战斗总数量
	private int attackWinSum; // 进攻胜利 总数量
	private int guardWinSum; // 防守胜利 总数量
	private int guardWinNum; // 防守胜利 数量 计算嘲讽是否到期使用
	private int challenge; // 挑战次数
	private int challengeBuy; // 挑战次数 购买次数
	private Date challengeBuyDate; // 挑战次数 购买时间
	private Date fightDate; // 挑战时间
	private int challengeMoney; // 排名获得的挑战币
	private String exchangeItemStr; // 挑战兑换商城 道具
	private Date exchangeRefreshDate; // 商城 刷新时间
	private int exchangeRefreshNum; // 商城 刷新次数
	private Date clearCdDate; // 清除时间次数 时间
	private int clearCdNum; // 当天清除时间次数
	private int maxRank; // 历史最高排名
	private Date showReportDate;// 查看战报 时间

	public RoleArenaRank() {
	}

	public RoleArenaRank(String roleId, Role role, String guardId, int sneerId,
			String sneerStr, int attackFightSum, int guardFightSum,
			int attackWinSum, int guardWinSum, int guardWinNum, int challenge,
			int challengeBuy, Date challengeBuyDate, Date fightDate,
			int challengeMoney, String exchangeItemStr,
			Date exchangeRefreshDate, int exchangeRefreshNum, int maxRank,
			Date clearCdDate, Date showReportDate) {
		this.roleId = roleId;
		this.role = role;
		this.guardId = guardId;
		this.sneerId = sneerId;
		this.sneerStr = sneerStr;
		this.attackFightSum = attackFightSum;
		this.guardFightSum = guardFightSum;
		this.attackWinSum = attackWinSum;
		this.guardWinSum = guardWinSum;
		this.guardWinNum = guardWinNum;
		this.challenge = challenge;
		this.challengeBuy = challengeBuy;
		this.challengeBuyDate = challengeBuyDate;
		this.fightDate = fightDate;
		this.challengeMoney = challengeMoney;
		this.exchangeItemStr = exchangeItemStr;
		this.exchangeRefreshDate = exchangeRefreshDate;
		this.exchangeRefreshNum = exchangeRefreshNum;
		this.clearCdDate = clearCdDate;
		this.maxRank = maxRank;
		this.showReportDate = showReportDate;
	}

	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "role"))
	@Id
	@GeneratedValue(generator = "generator")
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

	@Column(name = "guard_id")
	public String getGuardId() {
		return guardId;
	}

	public void setGuardId(String guardId) {
		this.guardId = guardId;
	}

	@Column(name = "sneer_id", columnDefinition = "INT default 0")
	public int getSneerId() {
		return sneerId;
	}

	public void setSneerId(int sneerId) {
		this.sneerId = sneerId;
	}

	@Column(name = "sneer_str")
	public String getSneerStr() {
		return sneerStr;
	}

	public void setSneerStr(String sneerStr) {
		this.sneerStr = sneerStr;
	}

	@Column(name = "attack_fight_sum", columnDefinition = "INT default 1")
	public int getAttackFightSum() {
		return attackFightSum;
	}

	public void setAttackFightSum(int attackFightSum) {
		this.attackFightSum = attackFightSum;
	}

	@Column(name = "guard_fight_sum", columnDefinition = "INT default 1")
	public int getGuardFightSum() {
		return guardFightSum;
	}

	public void setGuardFightSum(int guardFightSum) {
		this.guardFightSum = guardFightSum;
	}

	@Column(name = "attack_win_sum", columnDefinition = "INT default 1")
	public int getAttackWinSum() {
		return attackWinSum;
	}

	public void setAttackWinSum(int attackWinSum) {
		this.attackWinSum = attackWinSum;
	}

	@Column(name = "guard_win_sum", columnDefinition = "INT default 1")
	public int getGuardWinSum() {
		return guardWinSum;
	}

	public void setGuardWinSum(int guardWinSum) {
		this.guardWinSum = guardWinSum;
	}

	@Column(name = "guard_win_num", columnDefinition = "INT default 0")
	public int getGuardWinNum() {
		return guardWinNum;
	}

	public void setGuardWinNum(int guardWinNum) {
		this.guardWinNum = guardWinNum;
	}

	@Column(name = "challenge", columnDefinition = "INT default 0")
	public int getChallenge() {
		return challenge;
	}

	public void setChallenge(int challenge) {
		this.challenge = challenge;
	}

	@Column(name = "challenge_buy", columnDefinition = "INT default 0")
	public int getChallengeBuy() {
		return challengeBuy;
	}

	public void setChallengeBuy(int challengeBuy) {
		this.challengeBuy = challengeBuy;
	}

	@Column(name = "challenge_buy_date")
	public Date getChallengeBuyDate() {
		return challengeBuyDate;
	}

	public void setChallengeBuyDate(Date challengeBuyDate) {
		this.challengeBuyDate = challengeBuyDate;
	}

	@Column(name = "fight_date")
	public Date getFightDate() {
		return fightDate;
	}

	public void setFightDate(Date fightDate) {
		this.fightDate = fightDate;
	}

	@Column(name = "challenge_money", columnDefinition = "INT default 0")
	public int getChallengeMoney() {
		return challengeMoney;
	}

	public void setChallengeMoney(int challengeMoney) {
		this.challengeMoney = challengeMoney;
	}

	@Column(name = "exchange_item_str", length = 5000)
	public String getExchangeItemStr() {
		return exchangeItemStr;
	}

	public void setExchangeItemStr(String exchangeItemStr) {
		this.exchangeItemStr = exchangeItemStr;
	}

	@Column(name = "exchange_refresh_date")
	public Date getExchangeRefreshDate() {
		return exchangeRefreshDate;
	}

	public void setExchangeRefreshDate(Date exchangeRefreshDate) {
		this.exchangeRefreshDate = exchangeRefreshDate;
	}

	@Column(name = "exchange_refresh_num", columnDefinition = "INT default 0")
	public int getExchangeRefreshNum() {
		return exchangeRefreshNum;
	}

	public void setExchangeRefreshNum(int exchangeRefreshNum) {
		this.exchangeRefreshNum = exchangeRefreshNum;
	}

	@Column(name = "clear_cd_Date")
	public Date getClearCdDate() {
		return clearCdDate;
	}

	public void setClearCdDate(Date clearCdDate) {
		this.clearCdDate = clearCdDate;
	}

	@Column(name = "clear_cd_num", columnDefinition = "INT default 0")
	public int getClearCdNum() {
		return clearCdNum;
	}

	public void setClearCdNum(int clearCdNum) {
		this.clearCdNum = clearCdNum;
	}

	@Column(name = "max_rank", columnDefinition = "INT default 0")
	public int getMaxRank() {
		return maxRank;
	}

	public void setMaxRank(int maxRank) {
		this.maxRank = maxRank;
	}

	@Column(name = "show_report_date")
	public Date getShowReportDate() {
		return showReportDate;
	}

	public void setShowReportDate(Date showReportDate) {
		this.showReportDate = showReportDate;
	}
}

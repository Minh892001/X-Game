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
 * 群雄争霸 角色相关 数据库结构
 * 
 * @author 吕明涛
 * 
 */
@Entity
@Table(name = "role_ladder")
public class RoleLadder implements Serializable {

	/** */
	private static final long serialVersionUID = -4790185633465996490L;

	private String roleId;;
	private Role role;
	private String guardId; // 防守队伍
	private int ladderLevel; // 群雄争霸 等级
	private int ladderStar; // 群雄争霸 星级
	private int winNum; // 连胜 次数
	private int challengeRemain; // 剩余挑战次数
	private int challengeBuyNum; // 已经购买 挑战次数
	private Date challengeBuyDate; // 挑战次数 购买时间
	private String rewardStr; // 获得的奖励
	private Date showReportDate; // 查看 战报 时间，红点显示使用
	private Date seasonEndDate; // 赛季结束时间
	private Date changeLevelDate; // 等级或者星级 变化的时间
	private int ladderScore;// 积分

	public RoleLadder() {
	}

	public RoleLadder(String roleId, Role role, String guardId,
			int ladderLevel, int ladderStar, int winNum, int challengeRemain,
			int challengeBuyNum, Date challengeBuyDate, String rewardStr,
			Date showReportDate, Date seasonEndDate, Date changeLevelDate) {
		this.roleId = roleId;
		this.role = role;
		this.guardId = guardId;
		this.ladderLevel = ladderLevel;
		this.ladderStar = ladderStar;
		this.winNum = winNum;
		this.challengeRemain = challengeRemain;
		this.challengeBuyNum = challengeBuyNum;
		this.challengeBuyDate = challengeBuyDate;
		this.rewardStr = rewardStr;
		this.showReportDate = showReportDate;
		this.seasonEndDate = seasonEndDate;
		this.changeLevelDate = changeLevelDate;
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

	@Column(name = "challenge_remain", columnDefinition = "INT default 0")
	public int getChallengeRemain() {
		return challengeRemain;
	}

	public void setChallengeRemain(int challengeRemain) {
		this.challengeRemain = challengeRemain;
	}

	@Column(name = "challenge_buy_num", columnDefinition = "INT default 0")
	public int getChallengeBuyNum() {
		return challengeBuyNum;
	}

	public void setChallengeBuyNum(int challengeBuyNum) {
		this.challengeBuyNum = challengeBuyNum;
	}

	@Column(name = "challenge_buy_date")
	public Date getChallengeBuyDate() {
		return challengeBuyDate;
	}

	public void setChallengeBuyDate(Date challengeBuyDate) {
		this.challengeBuyDate = challengeBuyDate;
	}

	@Column(name = "show_report_date")
	public Date getShowReportDate() {
		return showReportDate;
	}

	public void setShowReportDate(Date showReportDate) {
		this.showReportDate = showReportDate;
	}

	@Column(name = "ladder_level", columnDefinition = "INT default 0")
	public int getLadderLevel() {
		return ladderLevel;
	}

	public void setLadderLevel(int ladderLevel) {
		this.ladderLevel = ladderLevel;
	}

	@Column(name = "ladder_star", columnDefinition = "INT default 0")
	public int getLadderStar() {
		return ladderStar;
	}

	public void setLadderStar(int ladderStar) {
		this.ladderStar = ladderStar;
	}

	@Column(name = "win_num", columnDefinition = "INT default 0")
	public int getWinNum() {
		return winNum;
	}

	public void setWinNum(int winNum) {
		this.winNum = winNum;
	}

	@Column(name = "reward_str")
	public String getRewardStr() {
		return rewardStr;
	}

	public void setRewardStr(String rewardStr) {
		this.rewardStr = rewardStr;
	}

	@Column(name = "season_date")
	public Date getSeasonEndDate() {
		return seasonEndDate;
	}

	public void setSeasonEndDate(Date seasonEndDate) {
		this.seasonEndDate = seasonEndDate;
	}

	@Column(name = "change_level_date")
	public Date getChangeLevelDate() {
		return changeLevelDate;
	}

	public void setChangeLevelDate(Date changeLevelDate) {
		this.changeLevelDate = changeLevelDate;
	}

	@Column(name = "ladder_score")
	public int getLadderScore() {
		return ladderScore;
	}

	public void setLadderScore(int ladderScore) {
		this.ladderScore = ladderScore;
	}

}

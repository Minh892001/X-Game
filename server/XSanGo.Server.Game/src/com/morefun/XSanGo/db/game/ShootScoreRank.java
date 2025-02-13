/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 百步穿杨排行 数据库结构
 * 
 * @author zhouming
 * 
 */
@Entity
@Table(name = "shoot_score_rank")
public class ShootScoreRank implements java.lang.Cloneable, Serializable {

	/**
	 * 序列化编号
	 */
	private static final long serialVersionUID = -5487211199956813818L;

	private String roleId;// 角色编号
	private int rank; // 排名
	private String rec; // 已领积分奖励
	private int totalScore; // 总积分
	private int dayFreeCnt; // 每日已经完成免费次数
	private int dayOneCnt; // 当日单射次数
	private int shootOneCnt; // 单射总次数
	private int shootTenCnt; // 十连次数
	private Date shootFreeTime; // 免费单射时间
	private Date shootOneTime; // 单射时间
	private Date shootTime; // 射击时间
	private String roleName; // 角色名称
	private String factionId; // 公会编号
	private int vip; // vip等级
	private int level; // 等级
	private String icon; // 图标
	private int isSendMail;//是否发送过邮件奖励
	private int showMyRecord; // 是否显示我的中奖信息
	private String historyAward; // 历史中奖记录
	private int shootCntSuper;// 超级百步射箭次数(单射，十连射记录没卵意义)

	public ShootScoreRank() {
	}

	@Id
	@Column(name = "role_id", nullable = false)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Column(name = "rank", columnDefinition = "INT default 0")
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	@Column(name = "rec")
	public String getRec() {
		return rec;
	}

	public void setRec(String rec) {
		this.rec = rec;
	}

	@Column(name = "total_score", columnDefinition = "INT default 0")
	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	@Column(name = "day_free_cnt", columnDefinition = "INT default 0")
	public int getDayFreeCnt() {
		return dayFreeCnt;
	}

	public void setDayFreeCnt(int dayFreeCnt) {
		this.dayFreeCnt = dayFreeCnt;
	}

	@Column(name = "day_one_cnt", columnDefinition = "INT default 0")
	public int getDayOneCnt() {
		return dayOneCnt;
	}

	public void setDayOneCnt(int dayOneCnt) {
		this.dayOneCnt = dayOneCnt;
	}

	@Column(name = "shoot_one_cnt", columnDefinition = "INT default 0")
	public int getShootOneCnt() {
		return shootOneCnt;
	}

	public void setShootOneCnt(int shootOneCnt) {
		this.shootOneCnt = shootOneCnt;
	}

	@Column(name = "shoot_ten_cnt", columnDefinition = "INT default 0")
	public int getShootTenCnt() {
		return shootTenCnt;
	}

	public void setShootTenCnt(int shootTenCnt) {
		this.shootTenCnt = shootTenCnt;
	}

	@Column(name = "shoot_free_time")
	public Date getShootFreeTime() {
		return shootFreeTime;
	}

	public void setShootFreeTime(Date shootFreeTime) {
		this.shootFreeTime = shootFreeTime;
	}

	@Column(name = "shoot_ont_time")
	public Date getShootOneTime() {
		return shootOneTime;
	}

	public void setShootOneTime(Date shootOneTime) {
		this.shootOneTime = shootOneTime;
		this.setShootTime(shootOneTime);
	}

	@Column(name = "shoot_time")
	public Date getShootTime() {
		return shootTime;
	}

	public void setShootTime(Date shootTime) {
		this.shootTime = shootTime;
	}

	@Transient
	@Column(updatable = false)
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = "send_mail")
	public int getIsSendMail() {
		return isSendMail;
	}

	public void setIsSendMail(int isSendMail) {
		this.isSendMail = isSendMail;
	}
	
	@Transient
	@Column(updatable = false)
	public String getFactionId() {
		return factionId;
	}

	public void setFactionId(String factionId) {
		this.factionId = factionId;
	}

	@Transient
	@Column(updatable = false)
	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	@Transient
	@Column(updatable = false)
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Transient
	@Column(updatable = false)
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	@Column(name = "show_my_record")
	public int getShowMyRecord() {
		return showMyRecord;
	}

	public void setShowMyRecord(int showMyRecord) {
		this.showMyRecord = showMyRecord;
	}
	
	@Column(name = "histroy_award")
	public String getHistoryAward() {
		return historyAward;
	}

	public void setHistoryAward(String historyAward) {
		this.historyAward = historyAward;
	}
	
	@Column(name = "shoot_cnt_super", nullable = false)
	public int getShootCntSuper() {
		return shootCntSuper;
	}

	public void setShootCntSuper(int shootCntSuper) {
		this.shootCntSuper = shootCntSuper;
	}
}

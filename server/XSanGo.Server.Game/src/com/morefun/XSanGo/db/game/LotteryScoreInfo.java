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
 * 大富温 数据库结构
 * 
 * @author sunjie
 * 
 */
@Entity
@Table(name = "lottery_record")
public class LotteryScoreInfo implements java.lang.Cloneable, Serializable {

	/**
	 * 序列化编号
	 */
	private static final long serialVersionUID = -5487211199956813818L;

	private String roleId;// 角色编号
	private int gridId;// 当前格子数
	private int throwNum; // 投掷次数
	private int dailyThrowNum;//日常投掷次数
	private int autoNum; // 遥控骰子个数
	private int cycleTime; // 循环圈数
	private String shopInfo;// 神秘商店排版信息
	private String gridsInfo;// 界面格子排版信息
	private int score; // 积分
	private int specialScore; // 任性值
	private Date updateTime;
	private Date shopOpenTime;
	private int sendMail;//是否发过邮件排名奖励 0：没 1：发 
	
	private String roleName; // 角色名称
	private String factionId; // 公会编号
	private int vip; // vip等级
	private int level; // 等级
	private String icon; // 图标

	public LotteryScoreInfo() {
	}

	public LotteryScoreInfo(String roleId, int gridId, Date updateTime) {
		super();
		this.roleId = roleId;
		this.gridId = gridId;
		this.updateTime = updateTime;
	}

	@Id
	@Column(name = "role_id", nullable = false)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Column(name = "score", columnDefinition = "INT default 0")
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Column(name = "grid_id", columnDefinition = "INT default 0")
	public int getGridId() {
		return gridId;
	}

	public void setGridId(int gridId) {
		this.gridId = gridId;
	}

	@Column(name = "throw_num", columnDefinition = "INT default 0")
	public int getThrowNum() {
		return throwNum;
	}

	@Column(name = "daily_throw_num", columnDefinition = "INT default 0")
	public int getDailyThrowNum() {
		return dailyThrowNum;
	}

	public void setDailyThrowNum(int dailyThrowNum) {
		this.dailyThrowNum = dailyThrowNum;
	}

	public void setThrowNum(int throwNum) {
		this.throwNum = throwNum;
	}

	@Column(name = "auto_num", columnDefinition = "INT default 0")
	public int getAutoNum() {
		return autoNum;
	}

	public void setAutoNum(int autoNum) {
		this.autoNum = autoNum;
	}

	@Column(name = "cycle_time", columnDefinition = "INT default 0")
	public int getCycleTime() {
		return cycleTime;
	}

	public void setCycleTime(int cycleTime) {
		this.cycleTime = cycleTime;
	}

	@Column(name = "shop_info")
	public String getShopInfo() {
		return shopInfo;
	}

	public void setShopInfo(String shopInfo) {
		this.shopInfo = shopInfo;
	}

	@Column(name = "grids_info")
	public String getGridsInfo() {
		return gridsInfo;
	}

	public void setGridsInfo(String gridsInfo) {
		this.gridsInfo = gridsInfo;
	}

	@Column(name = "special_score", columnDefinition = "INT default 0")
	public int getSpecialScore() {
		return specialScore;
	}

	public void setSpecialScore(int specialScore) {
		this.specialScore = specialScore;
	}
	
	@Column(name = "update_time")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "shop_open_time")
	public Date getShopOpenTime() {
		return shopOpenTime;
	}

	public void setShopOpenTime(Date shopOpenTime) {
		this.shopOpenTime = shopOpenTime;
	}

	@Column(name = "send_mail")
	public int getSendMail() {
		return sendMail;
	}

	public void setSendMail(int sendMail) {
		this.sendMail = sendMail;
	}

	@Transient
	@Column(updatable = false)
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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
	
}

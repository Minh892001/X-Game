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
 * 玩家欧洲杯数据
 * 
 * @author xiongming.li
 *
 */
@Entity
@Table(name = "role_football")
public class RoleFootball implements Serializable {
	private static final long serialVersionUID = 6017792294121228586L;
	private String roleId;
	private Role role;
	private int buyNum; // 购买次数
	private boolean isOpen;// 是否打开过欧洲杯界面
	private boolean isOverOpen;// 结束后是否打开过
	private Date refreshDate;
	private String betLogs = "[]";// 押注日志FootballBetLog[]的json

	public RoleFootball() {

	}

	public RoleFootball(String roleId, Role role) {
		this.roleId = roleId;
		this.role = role;
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

	@Column(name = "buy_num")
	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

	@Column(name = "is_open")
	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	@Column(name = "is_over_open")
	public boolean isOverOpen() {
		return isOverOpen;
	}

	public void setOverOpen(boolean isOverOpen) {
		this.isOverOpen = isOverOpen;
	}

	@Column(name = "refresh_date")
	public Date getRefreshDate() {
		return refreshDate;
	}

	public void setRefreshDate(Date refreshDate) {
		this.refreshDate = refreshDate;
	}

	@Column(name = "bet_logs", columnDefinition = "text")
	public String getBetLogs() {
		return betLogs;
	}

	public void setBetLogs(String betLogs) {
		this.betLogs = betLogs;
	}

}

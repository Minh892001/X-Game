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
 * 
 * 老友召回被召数据
 * 
 * @author zhangwei02.zhang
 * @since 2015年10月28日
 * @version 1.0
 */
@Entity
@Table(name = "role_friends_recalled")
public class RoleFriendsRecalled implements Serializable {

	private static final long serialVersionUID = 5471951178467861492L;

	private String roleId;

	private Role role;

	/** 邀请人id */
	private String inviteRoleId;

	/** 累计签到次数 */
	private int signCount;

	/** 累计充值金额 */
	private int chargeAmount;

	/** 0：已符合离线天数；1: 已打开过界面；2：已被召回 */
	private int state;

	/** 召回时间 */
	private Date recallTime;

	public RoleFriendsRecalled() {
	}

	public RoleFriendsRecalled(Role role, String inviteRoleId, int signCount, int chargeAmount, int state, Date recallTime) {
		super();
		this.role = role;
		this.inviteRoleId = inviteRoleId;
		this.signCount = signCount;
		this.chargeAmount = chargeAmount;
		this.state = state;
		this.recallTime = recallTime;
	}

	@Column(name = "invite_role_id", nullable = true)
	public String getInviteRoleId() {
		return inviteRoleId;
	}

	public void setInviteRoleId(String inviteRoleId) {
		this.inviteRoleId = inviteRoleId;
	}

	@Column(name = "sign_count", nullable = false)
	public int getSignCount() {
		return signCount;
	}

	public void setSignCount(int signCount) {
		this.signCount = signCount;
	}

	@Column(name = "charge_amount", nullable = false)
	public int getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(int chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	@Column(name = "state", nullable = false)
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Column(name = "recall_time", nullable = false)
	public Date getRecallTime() {
		return recallTime;
	}

	public void setRecallTime(Date recallTime) {
		this.recallTime = recallTime;
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


}

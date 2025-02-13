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
 * 幸运大转盘系统
 * 
 * @author qinguofeng
 */
@Entity
@Table(name = "role_fortune_wheel")
public class RoleFortuneWheel implements Serializable {

	private static final long serialVersionUID = -1885804064403978930L;

	private String roleId;
	private Role role;

	// 今日累计获得的次数
	private int totalCount;
	// 今日剩余次数
	private int lastCount;
	// 充值金额
	private int chargeCount;
	// 上次重置时间
	private Date updateTime;
	// 活动开始时间
	private String activityStartTime;

	public RoleFortuneWheel() {
	}

	public RoleFortuneWheel(Role r) {
		this.role = r;
		this.roleId = r.getId();
	}

	/**
	 * @return the roleId
	 */
	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "role"))
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "role_id", nullable = false)
	public String getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId
	 *            the roleId to set
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the role
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Role getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @return the totalCount
	 */
	@Column(name = "total_count", nullable = false)
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount
	 *            the totalCount to set
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @return the lastCount
	 */
	@Column(name = "last_count", nullable = false)
	public int getLastCount() {
		return lastCount;
	}

	/**
	 * @param lastCount
	 *            the lastCount to set
	 */
	public void setLastCount(int lastCount) {
		this.lastCount = lastCount;
	}

	/**
	 * @return the chargeCount
	 */
	@Column(name = "charge_count", nullable = false)
	public int getChargeCount() {
		return chargeCount;
	}

	/**
	 * @param chargeCount
	 *            the chargeCount to set
	 */
	public void setChargeCount(int chargeCount) {
		this.chargeCount = chargeCount;
	}

	/**
	 * @return the updateTime
	 */
	@Column(name = "update_time", nullable = false)
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 *            the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the activityStartTime
	 */
	@Column(name = "activity_start_time", nullable = true)
	public String getActivityStartTime() {
		return activityStartTime;
	}

	/**
	 * @param activityStartTime the activityStartTime to set
	 */
	public void setActivityStartTime(String activityStartTime) {
		this.activityStartTime = activityStartTime;
	}

}

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
 * @author qinguofeng
 */
@Entity
@Table(name = "role_powerreward")
public class RolePowerReward implements Serializable {

	private static final long serialVersionUID = 2432332103185801409L;

	private String roleId;
	private Role role;

	/** 已领取的战力奖励 */
	private String acceptedPowerRewards;
	/** 最后更新时间 */
	private Date lastUpdateTime;

	public RolePowerReward() {
	}

	public RolePowerReward(Role r) {
		this.role = r;
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
	 * @return the lastUpdateTime
	 */
	@Column(name = "last_update_time", nullable = false)
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	/**
	 * @return the acceptedPowerRewards
	 */
	@Column(name = "accepted_powerrewards", nullable = true)
	public String getAcceptedPowerRewards() {
		return acceptedPowerRewards;
	}

	/**
	 * @param acceptedPowerRewards
	 *            the acceptedPowerRewards to set
	 */
	public void setAcceptedPowerRewards(String acceptedPowerRewards) {
		this.acceptedPowerRewards = acceptedPowerRewards;
	}

	/**
	 * @param lastUpdateTime
	 *            the lastUpdateTime to set
	 */
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

}

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
 * 冲级有奖
 * 
 * @author qinguofeng
 */
@Entity
@Table(name = "role_levelreward")
public class RoleLevelReward implements Serializable {

	private static final long serialVersionUID = -5803455102855990585L;

	private String roleId;
	private Role role;

	/** 已领取的等级奖励 */
	private String acceptedLevelRewards;
	/** 最后更新时间 */
	private Date lastUpdateTime;

	public RoleLevelReward() {

	}

	public RoleLevelReward(Role role) {
		this.roleId = role.getId();
		this.role = role;
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
	 * @return the acceptedLevelRewards
	 */
	@Column(name = "accepted_levelrewards", nullable = true)
	public String getAcceptedLevelRewards() {
		return acceptedLevelRewards;
	}

	/**
	 * @param acceptedLevelRewards
	 *            the acceptedLevelRewards to set
	 */
	public void setAcceptedLevelRewards(String acceptedLevelRewards) {
		this.acceptedLevelRewards = acceptedLevelRewards;
	}

	/**
	 * @return the lastUpdateTime
	 */
	@Column(name = "last_update_time", nullable = false)
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	/**
	 * @param lastUpdateTime
	 *            the lastUpdateTime to set
	 */
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

}

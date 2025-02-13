package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Calendar;
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
 * 成长基金
 * 
 * @author qinguofeng
 */
@Entity
@Table(name = "role_fund")
public class RoleFund implements Serializable {

	private static final long serialVersionUID = -2801471869752788138L;

	private String roleId;
	private Role role;
	
	public RoleFund() {
		
	}
	
	public RoleFund(Role r) {
		this.roleId = r.getId();
		this.role = r;
		
		this.buyFund = 0;
		this.acceptedRewards = "";
		this.lastUpdateTime = Calendar.getInstance().getTime();
	}

	/**
	 * 是否购买成长基金
	 * 
	 * 0,没有购买; 1,已购买;
	 * */
	private int buyFund;
	/**
	 * 已领取的奖励,领取过的奖励等级以逗号分割的字符串
	 * */
	private String acceptedRewards;
	/**
	 * 最后更新时间
	 * */
	private Date lastUpdateTime;

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
	 * @return the buyFund
	 */
	@Column(name = "buy_fund", nullable = false, columnDefinition = "int default 0")
	public int getBuyFund() {
		return buyFund;
	}

	/**
	 * @param buyFund
	 *            the buyFund to set
	 */
	public void setBuyFund(int buyFund) {
		this.buyFund = buyFund;
	}

	/**
	 * @return the acceptedRewards
	 */
	@Column(name = "accepted_rewards", nullable = true)
	public String getAcceptedRewards() {
		return acceptedRewards;
	}

	/**
	 * @param acceptedRewards
	 *            the acceptedRewards to set
	 */
	public void setAcceptedRewards(String acceptedRewards) {
		this.acceptedRewards = acceptedRewards;
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

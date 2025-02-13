/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;

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
 * 切磋胜利失败次数统计 数据库结构
 * 
 * @author zhuzhi.yang
 * 
 */
@Entity
@Table(name = "role_challenge_summary")
public class RoleChallengeSummary implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String roleId;
	private Role role;
	private int successTimes;
	private int failTimes;
	
	public RoleChallengeSummary(){
		
	}
	
	public RoleChallengeSummary(Role role) {
		this.role = role;
		this.roleId = role.getId();
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
	
	@Column(name = "success_times", nullable = false)
	public int getSuccessTimes() {
		return successTimes;
	}

	public void setSuccessTimes(int successTimes) {
		this.successTimes = successTimes;
	}

	@Column(name = "fail_count", nullable = false)
	public int getFailTimes() {
		return failTimes;
	}

	public void setFailTimes(int failTimes) {
		this.failTimes = failTimes;
	}

}

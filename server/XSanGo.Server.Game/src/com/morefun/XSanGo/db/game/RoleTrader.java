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
 * 神秘商人数据
 * 
 * @author sulingyun
 * 
 */
@Entity
@Table(name = "role_trader")
public class RoleTrader implements Serializable {
	
	private static final long serialVersionUID = 3944717635227091533L;
	private String roleId;
	private Role role;

	/* 神秘商人相关数据 */
	private int jinbiCount;
	private int yuanbaoCount;
	private Date lastCallTime;
	private String callResult;

	/* 名将相关数据 */
	private int heroJinbiCount;
	private int heroYuanbaoCount;
	private Date lastHeroCallTime;
	private String heroCallResult;

	public RoleTrader() {
	}

	public RoleTrader(Role role) {
		this.role = role;
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

	@Column(name = "jinbi_count", nullable = false)
	public int getJinbiCount() {
		return jinbiCount;
	}

	public void setJinbiCount(int jinbiCount) {
		this.jinbiCount = jinbiCount;
	}

	@Column(name = "yuanbao_count", nullable = false)
	public int getYuanbaoCount() {
		return yuanbaoCount;
	}

	public void setYuanbaoCount(int yuanbaoCount) {
		this.yuanbaoCount = yuanbaoCount;
	}

	@Column(name = "last_call_time", nullable = true)
	public Date getLastCallTime() {
		return lastCallTime;
	}

	public void setLastCallTime(Date lastCallTime) {
		this.lastCallTime = lastCallTime;
	}

	@Column(name = "call_result", nullable = true, columnDefinition = "text")
	public String getCallResult() {
		return callResult;
	}

	public void setCallResult(String callResult) {
		this.callResult = callResult;
	}

	@Column(name = "hero_jinbi_count", nullable = false, columnDefinition = "int default 0")
	public int getHeroJinbiCount() {
		return heroJinbiCount;
	}

	public void setHeroJinbiCount(int heroJinbiCount) {
		this.heroJinbiCount = heroJinbiCount;
	}

	@Column(name = "hero_yuanbao_count", nullable = false, columnDefinition = "int default 0")
	public int getHeroYuanbaoCount() {
		return heroYuanbaoCount;
	}

	public void setHeroYuanbaoCount(int heroYuanbaoCount) {
		this.heroYuanbaoCount = heroYuanbaoCount;
	}

	@Column(name = "last_hero_time", nullable = true)
	public Date getLastHeroCallTime() {
		return lastHeroCallTime;
	}

	public void setLastHeroCallTime(Date lastHeroCallTime) {
		this.lastHeroCallTime = lastHeroCallTime;
	}

	@Column(name = "hero_call_result", nullable = true, columnDefinition = "text")
	public String getHeroCallResult() {
		return heroCallResult;
	}

	public void setHeroCallResult(String heroCallResult) {
		this.heroCallResult = heroCallResult;
	}
}

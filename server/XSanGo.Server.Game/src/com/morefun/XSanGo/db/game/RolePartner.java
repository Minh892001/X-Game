package com.morefun.XSanGo.db.game;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * 伙伴数据
 * @author xiaojun.zhang
 *
 */
@Entity
@Table(name = "role_partner")
public class RolePartner implements Serializable {

	private static final long serialVersionUID = -832383796693437762L;
	private String id;
	private Role role;
	/**已开启的最大伙伴位置*/
	private int maxPos =0;
	/**包含武将id-伙伴位置,以及位置和对应属性id信息*/
	private String configs;
	
	public RolePartner() {}


	public RolePartner(String id, Role role, int maxPos, String configs) {
		super();
		this.id = id;
		this.role = role;
		this.maxPos = maxPos;
		this.configs = configs;
	}


	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	@Column(name = "config", nullable = true)
	public String getConfigs() {
		return configs;
	}

	public void setConfigs(String configs) {
		this.configs = configs;
	}
	@Column(name = "max_pos", nullable = false)
	public int getMaxPos() {
		return maxPos;
	}

	public void setMaxPos(int maxPos) {
		this.maxPos = maxPos;
	}

	
	
}

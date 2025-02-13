/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 军团数据
 * 
 * @author Su LingYun
 * 
 */
@Entity
@Table(name = "role_formation")
public class RoleFormation implements Serializable {
	private static final long serialVersionUID = 5969134862303380018L;

	private String id;
	private Role role;
	private byte index;
	private String config;
	private int advancedType;// 当前选择的阵法进阶类型
	private String advanceds = "[]";// 阵法进阶，IntIntPair[]的json

	public RoleFormation() {
	}

	public RoleFormation(String id, Role role, byte index, String config) {
		this.id = id;
		this.role = role;
		this.index = index;
		this.config = config;
	}

	// Property accessors
	@Id
	@Column(name = "id", nullable = false, length = 64)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "group_index", nullable = false)
	public byte getIndex() {
		return index;
	}

	public void setIndex(byte index) {
		this.index = index;
	}

	@Column(name = "config", nullable = false)
	public String getConfig() {
		return config;
	}

	public void setConfig(String postionConfig) {
		this.config = postionConfig;
	}

	@Column(name = "advanced_type", nullable = false)
	public int getAdvancedType() {
		return advancedType;
	}

	public void setAdvancedType(int advancedType) {
		this.advancedType = advancedType;
	}

	@Column(name = "advanceds")
	public String getAdvanceds() {
		return advanceds;
	}

	public void setAdvanceds(String advanceds) {
		this.advanceds = advanceds;
	}

}

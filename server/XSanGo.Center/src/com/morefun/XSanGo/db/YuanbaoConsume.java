/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.db;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * YunbaoTransferLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "yuanbao_consume")
public class YuanbaoConsume implements java.io.Serializable {

	// Fields

	private int id;
	private int serverId;
	private String account;
	private int yuanbao;
	private String roleId;
	private String roleName;
	private int state;
	private Timestamp completeTime;

	// Constructors

	/** default constructor */
	public YuanbaoConsume() {
	}

	/** minimal constructor */
	public YuanbaoConsume(int serverId, String account, int yuanbao,
			String roleId, String roleName, int state) {
		this.serverId = serverId;
		this.account = account;
		this.yuanbao = yuanbao;
		this.roleId = roleId;
		this.roleName = roleName;
		this.state = state;
	}

	/** full constructor */
	public YuanbaoConsume(int serverId, String account, int yuanbao,
			String roleId, String roleName, int state, Timestamp completeTime) {
		this.serverId = serverId;
		this.account = account;
		this.yuanbao = yuanbao;
		this.roleId = roleId;
		this.roleName = roleName;
		this.state = state;
		this.completeTime = completeTime;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "server_id", nullable = false)
	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	@Column(name = "account", nullable = false)
	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "yuanbao", nullable = false)
	public int getYuanbao() {
		return this.yuanbao;
	}

	public void setYuanbao(int yuanbao) {
		this.yuanbao = yuanbao;
	}

	@Column(name = "role_id", nullable = false)
	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Column(name = "role_name", nullable = false, length = 32)
	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = "state", nullable = false)
	public int getState() {
		return this.state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Column(name = "complete_time", length = 19)
	public Timestamp getCompleteTime() {
		return this.completeTime;
	}

	public void setCompleteTime(Timestamp completeTime) {
		this.completeTime = completeTime;
	}

}
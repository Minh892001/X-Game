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
 * 老友召回邀请
 * 
 * @author zhangwei02.zhang
 * @since 2015年10月28日
 * @version 1.0
 */
@Entity
@Table(name = "role_friends_invitation")
public class RoleFriendsInvitation implements Serializable {

	private static final long serialVersionUID = 4378127117804611840L;

	private String roleId;

	private Role role;

	/** 邀请码 */
	private String recallCode;

	/** 被邀请者累计消耗的元宝 */
	private int consumeYuanBao;

	/** 被邀请者达到的最高的等级 */
	private int maxLevel;
	
	private Date createTime;

	public RoleFriendsInvitation() {
	}

	public RoleFriendsInvitation(Role role, String recallCode, int consumeYuanBao, int maxLevel, Date createTime) {
		super();
		this.role = role;
		this.recallCode = recallCode;
		this.consumeYuanBao = consumeYuanBao;
		this.maxLevel = maxLevel;
		this.createTime = createTime;
	}

	@Column(name = "recall_code", nullable = false)
	public String getRecallCode() {
		return recallCode;
	}

	public void setRecallCode(String recallCode) {
		this.recallCode = recallCode;
	}

	@Column(name = "consume_amount", nullable = false)
	public int getConsumeYuanBao() {
		return consumeYuanBao;
	}

	public void setConsumeYuanBao(int consumeYuanBao) {
		this.consumeYuanBao = consumeYuanBao;
	}

	@Column(name = "create_time", nullable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "max_level", nullable = false)
	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
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
}

package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "role_sns")
public class RoleSns implements Serializable {
	private static final long serialVersionUID = -848340874658969306L;

	private String id;
	private Role role;
	private String targetRoleId;
	private int relationType;
	private int friendPoint; // 友情点
	private int acceptJunLingNum; // 收到的军令个数
	private Date acceptJunLingTime;

	public RoleSns() {
	}

	public RoleSns(String id, Role role, String targetRoleId, int relationType, int friendPoint,
			int acceptJunLingNum, Date acceptJunLingTime) {
		super();
		this.id = id;
		this.role = role;
		this.targetRoleId = targetRoleId;
		this.relationType = relationType;
		this.friendPoint = friendPoint;
		this.acceptJunLingNum = acceptJunLingNum;
		this.acceptJunLingTime = acceptJunLingTime;
	}

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

	@Column(name = "target", nullable = false, length = 64)
	public String getTargetRoleId() {
		return targetRoleId;
	}

	public void setTargetRoleId(String targetRoleId) {
		this.targetRoleId = targetRoleId;
	}

	@Column(name = "type", nullable = false, length = 64)
	public int getRelationType() {
		return relationType;
	}

	public void setRelationType(int relationType) {
		this.relationType = relationType;
	}

	/**
	 * @return the acceptJunLingNum
	 */
	@Column(name = "accept_junling_num", nullable = false)
	public int getAcceptJunLingNum() {
		return acceptJunLingNum;
	}

	/**
	 * @param acceptJunLingNum the acceptJunLingNum to set
	 */
	public void setAcceptJunLingNum(int acceptJunLingNum) {
		this.acceptJunLingNum = acceptJunLingNum;
	}

	/**
	 * @return the acceptJunLingTime
	 */
	@Column(name = "accept_junling_time", nullable = true)
	public Date getAcceptJunLingTime() {
		return acceptJunLingTime;
	}

	/**
	 * @param acceptJunLingTime the acceptJunLingTime to set
	 */
	public void setAcceptJunLingTime(Date acceptJunLingTime) {
		this.acceptJunLingTime = acceptJunLingTime;
	}

	/**
	 * @return the friendPoint
	 */
	@Column(name = "friend_point", nullable = false)
	public int getFriendPoint() {
		return friendPoint;
	}

	/**
	 * @param friendPoint the friendPoint to set
	 */
	public void setFriendPoint(int friendPoint) {
		this.friendPoint = friendPoint;
	}

}

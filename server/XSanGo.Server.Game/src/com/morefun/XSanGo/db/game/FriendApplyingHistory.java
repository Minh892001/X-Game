package com.morefun.XSanGo.db.game;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "friend_applying_history")
public class FriendApplyingHistory implements Serializable {
	private static final long serialVersionUID = 1257078714303996835L;

	private String id;
	private String applicantRoleId;
	private Role target;
	private int relationType;
	private String date;

	public FriendApplyingHistory() {
	}

	public FriendApplyingHistory(String id, String applicantRoleId,
			Role target, int relationType, String date) {
		super();
		this.id = id;
		this.applicantRoleId = applicantRoleId;
		this.target = target;
		this.relationType = relationType;
		this.date = date;
	}

	@Id
	@Column(name = "id", nullable = false, length = 64)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "player", nullable = false, length = 64)
	public String getApplicantRoleId() {
		return applicantRoleId;
	}

	public void setApplicantRoleId(String roleId) {
		this.applicantRoleId = roleId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "target", nullable = false)
	public Role getTarget() {
		return target;
	}

	public void setTarget(Role target) {
		this.target = target;
	}

	@Column(name = "type", nullable = false, length = 64)
	public int getRelationType() {
		return relationType;
	}

	public void setRelationType(int relationType) {
		this.relationType = relationType;
	}

	@Column(name = "date", length = 64)
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}

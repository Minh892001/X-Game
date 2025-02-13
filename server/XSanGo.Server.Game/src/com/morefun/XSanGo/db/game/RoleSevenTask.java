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
@Table(name = "role_seven_task")
public class RoleSevenTask implements Serializable {
	private static final long serialVersionUID = -9181468973881831470L;
	private String id;
	private Role role;
	private int starCount;//总星级
	private String threeStarRecs;//三星奖励信息
	private String starAwardRecs;//星级奖励信息
	private Date joinDate;//首次参与时间

	public RoleSevenTask() {

	}

	public RoleSevenTask(String id, Role role,int starCount,String threeStarRecs,String starAwardRecs,Date joinDate) {
		super();
		this.id = id;
		this.role = role;
		this.starCount=starCount;
		this.threeStarRecs = threeStarRecs;
		this.starAwardRecs = starAwardRecs;
		this.joinDate = joinDate;
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
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "total_star")
	public int getStarCount() {
		return starCount;
	}

	public void setStarCount(int starCount) {
		this.starCount = starCount;
	}

	@Column(name = "three_star_rec")
	public String getThreeStarRecs() {
		return threeStarRecs;
	}

	public void setThreeStarRecs(String threeStarRecs) {
		this.threeStarRecs = threeStarRecs;
	}

	@Column(name = "star_award_rec")
	public String getStarAwardRecs() {
		return starAwardRecs;
	}

	public void setStarAwardRecs(String starAwardRecs) {
		this.starAwardRecs = starAwardRecs;
	}

	@Column(name = "join_time")
	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}
	
}

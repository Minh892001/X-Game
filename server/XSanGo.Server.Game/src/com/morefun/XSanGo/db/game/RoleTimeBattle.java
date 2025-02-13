/**
 * 
 */
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

/**
 * 时空战役可挑战次数
 */
@Entity
@Table(name = "role_time_battle")
public class RoleTimeBattle implements Serializable {
	private static final long serialVersionUID = -3061448434419238772L;
	private String id;
	private Role role;
	private int passId;// 脚本上的关卡ID
	private int challengeTimes;// 已挑战次数
	private Date lastPassDate;// 最后挑战成功时间
	private int maxStar;//最大过关星级

	public RoleTimeBattle() {

	}

	public RoleTimeBattle(String id, Role role, int passId, int challengeTimes) {
		this.id = id;
		this.role = role;
		this.passId = passId;
		this.challengeTimes = challengeTimes;
		this.lastPassDate = new Date();
	}

	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
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

	@Column(name = "pass_id", nullable = false)
	public int getPassId() {
		return passId;
	}

	public void setPassId(int passId) {
		this.passId = passId;
	}

	@Column(name = "challenge_times", nullable = false)
	public int getChallengeTimes() {
		return challengeTimes;
	}

	public void setChallengeTimes(int challengeTimes) {
		this.challengeTimes = challengeTimes;
	}

	@Column(name = "last_pass_date", nullable = false)
	public Date getLastPassDate() {
		return lastPassDate;
	}

	public void setLastPassDate(Date lastPassDate) {
		this.lastPassDate = lastPassDate;
	}

	@Column(name = "max_star", nullable = false)
	public int getMaxStar() {
		return maxStar;
	}

	public void setMaxStar(int maxStar) {
		this.maxStar = maxStar;
	}

}

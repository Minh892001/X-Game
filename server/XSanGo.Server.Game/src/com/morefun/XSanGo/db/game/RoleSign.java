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
@Table(name = "role_sign")
public class RoleSign implements Serializable {
	private static final long serialVersionUID = -848340874658969306L;

	private String id;
	private Role role;
	private String date;
	private int signTimes = 0;
	private int rouletteTimes = 0;
	private int resignFlag = 0;

	public RoleSign() {
	}

	public RoleSign(String id, Role role, String date, int signTimes,
			int rouletteTimes) {
		super();
		this.id = id;
		this.role = role;
		this.date = date;
		this.signTimes = signTimes;
		this.rouletteTimes = rouletteTimes;
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

	@Column(name = "date", nullable = false, length = 64)
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Column(name = "sign_times", nullable = false, length = 64, columnDefinition = "INT default 0")
	public int getSignTimes() {
		return signTimes;
	}

	public void setSignTimes(int signTimes) {
		this.signTimes = signTimes;
	}

	@Column(name = "roulette_times", nullable = false, length = 64, columnDefinition = "INT default 0")
	public int getRouletteTimes() {
		return rouletteTimes;
	}

	public void setRouletteTimes(int rouletteTimes) {
		this.rouletteTimes = rouletteTimes;
	}

	@Column(name = "resign_flag", nullable = false, length = 64, columnDefinition = "INT default 0")
	public int getResignFlag() {
		return resignFlag;
	}

	public void setResignFlag(int resignFlag) {
		this.resignFlag = resignFlag;
	}

}

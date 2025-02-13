package com.morefun.XSanGo.db.game;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 欧洲杯押注信息
 * 
 * @author xiongming.li
 *
 */
@Entity
@Table(name = "football_bet")
public class FootballBet implements Serializable {
	private static final long serialVersionUID = 2649218617960539802L;

	private String id;
	private int groupId;
	private String roleId;
	private int betCountryId;
	private int betNum;
	private boolean isAward;

	public FootballBet() {
	}

	public FootballBet(String id, int groupId, String roleId, int betCountryId, int betNum) {
		this.id = id;
		this.groupId = groupId;
		this.roleId = roleId;
		this.betCountryId = betCountryId;
		this.betNum = betNum;
	}

	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "group_id")
	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	@Column(name = "role_id")
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Column(name = "bet_country_id")
	public int getBetCountryId() {
		return betCountryId;
	}

	public void setBetCountryId(int betCountryId) {
		this.betCountryId = betCountryId;
	}

	@Column(name = "bet_num")
	public int getBetNum() {
		return betNum;
	}

	public void setBetNum(int betNum) {
		this.betNum = betNum;
	}

	@Column(name = "is_award")
	public boolean isAward() {
		return isAward;
	}

	public void setAward(boolean isAward) {
		this.isAward = isAward;
	}

}

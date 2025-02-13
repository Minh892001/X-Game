package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * 玩家世界BOSS
 * 
 * @author lixiongming
 * 
 */
@Entity
@Table(name = "role_world_boss")
public class RoleWorldBoss implements Serializable {
	private static final long serialVersionUID = 1601581546545959607L;
	private String id;
	private Role role;
	private Date challengeDate;
	private int inspireType;// 鼓舞类型
	private int inspireValue;// 鼓舞值
	private int gainItemNum;
	private Date gainItemDate;
	private String trustItems = "[]";// 托管获得物品ItemView[]的json
	private int trustYuanbao;// 托管需要元宝
	private int trustNum;// 已使用托管次数
	private Date trustRefreshDate;// 托管次数刷新时间

	public RoleWorldBoss() {

	}

	public RoleWorldBoss(String id, Role role, Date challengeDate, int inspireType, int inspireValue) {
		super();
		this.id = id;
		this.role = role;
		this.challengeDate = challengeDate;
		this.inspireType = inspireType;
		this.inspireValue = inspireValue;
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

	@Column(name = "challenge_date")
	public Date getChallengeDate() {
		return challengeDate;
	}

	public void setChallengeDate(Date challengeDate) {
		this.challengeDate = challengeDate;
	}

	@Column(name = "inspire_type")
	public int getInspireType() {
		return inspireType;
	}

	public void setInspireType(int inspireType) {
		this.inspireType = inspireType;
	}

	@Column(name = "inspire_value")
	public int getInspireValue() {
		return inspireValue;
	}

	public void setInspireValue(int inspireValue) {
		this.inspireValue = inspireValue;
	}

	@Column(name = "gain_item_num", nullable = false)
	public int getGainItemNum() {
		return gainItemNum;
	}

	public void setGainItemNum(int gainItemNum) {
		this.gainItemNum = gainItemNum;
	}

	@Column(name = "gain_item_date")
	public Date getGainItemDate() {
		return gainItemDate;
	}

	public void setGainItemDate(Date gainItemDate) {
		this.gainItemDate = gainItemDate;
	}

	@Column(name = "trust_items")
	public String getTrustItems() {
		return trustItems;
	}

	public void setTrustItems(String trustItems) {
		this.trustItems = trustItems;
	}

	@Column(name = "trust_yuanbao")
	public int getTrustYuanbao() {
		return trustYuanbao;
	}

	public void setTrustYuanbao(int trustYuanbao) {
		this.trustYuanbao = trustYuanbao;
	}

	@Column(name = "trust_num")
	public int getTrustNum() {
		return trustNum;
	}

	public void setTrustNum(int trustNum) {
		this.trustNum = trustNum;
	}

	@Column(name = "trust_refresh_date")
	public Date getTrustRefreshDate() {
		return trustRefreshDate;
	}

	public void setTrustRefreshDate(Date trustRefreshDate) {
		this.trustRefreshDate = trustRefreshDate;
	}

}

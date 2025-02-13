package com.morefun.XSanGo.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 跨服竞技场排行
 * 
 * @author xiongming.li
 *
 */
@Entity
public class CrossArenaRank implements Serializable {

	private static final long serialVersionUID = 1550699439073767525L;

	private int id;
	private int rangeId;// 跨服区间ID
	private String roleId;
	private int rank;
	private String signature; // 个性签名
	private int attackFightSum; // 进攻 战斗总数量
	private int guardFightSum; // 防守 战斗总数量
	private int attackWinSum; // 进攻胜利 总数量
	private int guardWinSum; // 防守胜利 总数量
	private String rivalRank;// RivalRank的json
	private String formationView;// PvpOpponentFormationView的json

	public CrossArenaRank() {

	}

	public CrossArenaRank(int rangeId, String roleId, int rank, String signature, int attackFightSum,
			int guardFightSum, int attackWinSum, int guardWinSum, String rivalRank, String formationView) {
		this.rangeId = rangeId;
		this.roleId = roleId;
		this.rank = rank;
		this.signature = signature;
		this.attackFightSum = attackFightSum;
		this.guardFightSum = guardFightSum;
		this.attackWinSum = attackWinSum;
		this.guardWinSum = guardWinSum;
		this.rivalRank = rivalRank;
		this.formationView = formationView;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getRivalRank() {
		return rivalRank;
	}

	public void setRivalRank(String rivalRank) {
		this.rivalRank = rivalRank;
	}

	@Column(columnDefinition = "text")
	public String getFormationView() {
		return formationView;
	}

	public void setFormationView(String formationView) {
		this.formationView = formationView;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public int getAttackFightSum() {
		return attackFightSum;
	}

	public void setAttackFightSum(int attackFightSum) {
		this.attackFightSum = attackFightSum;
	}

	public int getGuardFightSum() {
		return guardFightSum;
	}

	public void setGuardFightSum(int guardFightSum) {
		this.guardFightSum = guardFightSum;
	}

	public int getAttackWinSum() {
		return attackWinSum;
	}

	public void setAttackWinSum(int attackWinSum) {
		this.attackWinSum = attackWinSum;
	}

	public int getGuardWinSum() {
		return guardWinSum;
	}

	public void setGuardWinSum(int guardWinSum) {
		this.guardWinSum = guardWinSum;
	}

	public int getRangeId() {
		return rangeId;
	}

	public void setRangeId(int rangeId) {
		this.rangeId = rangeId;
	}

}

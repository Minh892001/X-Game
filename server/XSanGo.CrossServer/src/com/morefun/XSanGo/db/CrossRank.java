package com.morefun.XSanGo.db;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.XSanGo.Protocol.PvpOpponentFormationView;

/**
 * 排行榜
 * 
 * @author xiongming.li
 *
 */
@Entity
public class CrossRank implements Serializable {

	private static final long serialVersionUID = 1550699439073767525L;

	private int id;
	private String roleId;
	private String roleView;
	private Date createDate = new Date();
	private int score;
	private String pvpView;// 部队阵容PvpOpponentFormationView的json
	private Date updateDate = new Date();// 上榜时间
	private int winNum;// 资格赛胜场数
	private int failNum;// 资格赛失败数
	private int toastNum;// 敬酒数量
	private int intoStage;// 资格赛进入的阶段
	private int crossId;// 跨服ID
	
	private PvpOpponentFormationView formationView;

	public CrossRank() {

	}

	public CrossRank(String roleId, String roleView, int score, String pvpView, int crossId) {
		this.roleId = roleId;
		this.roleView = roleView;
		this.score = score;
		this.pvpView = pvpView;
		this.crossId = crossId;
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

	@Column(length = 1000)
	public String getRoleView() {
		return roleView;
	}

	public void setRoleView(String roleView) {
		this.roleView = roleView;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Column(columnDefinition = "text")
	public String getPvpView() {
		return pvpView;
	}

	public void setPvpView(String pvpView) {
		this.pvpView = pvpView;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public int getWinNum() {
		return winNum;
	}

	public void setWinNum(int winNum) {
		this.winNum = winNum;
	}

	public int getFailNum() {
		return failNum;
	}

	public void setFailNum(int failNum) {
		this.failNum = failNum;
	}

	public int getToastNum() {
		return toastNum;
	}

	public void setToastNum(int toastNum) {
		this.toastNum = toastNum;
	}

	public int getIntoStage() {
		return intoStage;
	}

	public void setIntoStage(int intoStage) {
		this.intoStage = intoStage;
	}

	public int getCrossId() {
		return crossId;
	}

	public void setCrossId(int crossId) {
		this.crossId = crossId;
	}

	@Transient
	public PvpOpponentFormationView getFormationView() {
		return formationView;
	}

	public void setFormationView(PvpOpponentFormationView formationView) {
		this.formationView = formationView;
	}

}

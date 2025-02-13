package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author qinguofeng
 * @date Apr 9, 2015
 */
@Table(name = "fight_movie")
@Entity
public class FightMovie implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	/**
	 * 战斗类型
	 * 
	 * 0,竞技场; 1,群雄争霸; 2,切磋; 3,挑战他;
	 * */
	private int type;
	/** 己方RoleId */
	private String selfId;
	/** 对方RoleId */
	private String opponentId;
	/** 战斗结果 */
	private int result;
	/** 战斗结束后活着的武将数量 */
	private int remainHero;
	/** 己方阵容 */
	private String selfFormation;
	/** 对方阵容 */
	private String opponentFormation;
	/** 数据 */
	private String data;
	/** 战报开始时间 */
	private Date startDate;
	/** 战报结束时间 */
	private Date endDate;
	/**
	 * 验证状态
	 * 
	 * -1,不完整战报;0,未验证;1,验证正常;2,验证不正常
	 * */
	private int validated;

	public FightMovie() {

	}

	public FightMovie(String id, String data) {
		super();
		this.id = id;
		this.data = data;
	}

	/**
	 * @return the id
	 */
	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the data
	 */
	@Column(name = "data", nullable = false)
	public String getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return the type
	 */
	@Column(name = "type", nullable = false)
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the selfId
	 */
	@Column(name = "self_role_id", nullable = false)
	public String getSelfId() {
		return selfId;
	}

	/**
	 * @param selfId
	 *            the selfId to set
	 */
	public void setSelfId(String selfId) {
		this.selfId = selfId;
	}

	/**
	 * @return the opponentId
	 */
	@Column(name = "opponent_role_id", nullable = false)
	public String getOpponentId() {
		return opponentId;
	}

	/**
	 * @param opponentId
	 *            the opponentId to set
	 */
	public void setOpponentId(String opponentId) {
		this.opponentId = opponentId;
	}

	/**
	 * @return the result
	 */
	@Column(name = "result", nullable = false)
	public int getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(int result) {
		this.result = result;
	}

	/**
	 * @return the remainHero
	 */
	@Column(name = "remain_hero", nullable = false)
	public int getRemainHero() {
		return remainHero;
	}

	/**
	 * @param remainHero
	 *            the remainHero to set
	 */
	public void setRemainHero(int remainHero) {
		this.remainHero = remainHero;
	}

	/**
	 * @return the selfFormation
	 */
	@Column(name = "self_formation_json", nullable = false, columnDefinition = "text")
	public String getSelfFormation() {
		return selfFormation;
	}

	/**
	 * @param selfFormation
	 *            the selfFormation to set
	 */
	public void setSelfFormation(String selfFormation) {
		this.selfFormation = selfFormation;
	}

	/**
	 * @return the opponentFormation
	 */
	@Column(name = "opponent_formation_json", nullable = false, columnDefinition = "text")
	public String getOpponentFormation() {
		return opponentFormation;
	}

	/**
	 * @param opponentFormation
	 *            the opponentFormation to set
	 */
	public void setOpponentFormation(String opponentFormation) {
		this.opponentFormation = opponentFormation;
	}

	/**
	 * @return the startDate
	 */
	@Column(name = "start_time", nullable = false)
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	@Column(name = "end_time", nullable = true)
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the validated
	 */
	@Column(name="validated", nullable = false)
	public int getValidated() {
		return validated;
	}

	/**
	 * @param validated the validated to set
	 */
	public void setValidated(int validated) {
		this.validated = validated;
	}

}

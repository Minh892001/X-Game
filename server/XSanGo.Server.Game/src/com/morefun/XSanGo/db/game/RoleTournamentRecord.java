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
 * @author guofeng.qin
 */
@Entity
@Table(name = "role_tournament_record")
public class RoleTournamentRecord implements Serializable {

	private static final long serialVersionUID = -9077764368319218894L;

	private String id;

	private Role role;

	private int local; // 战报是否存储在本地
	private String roleViewData; // CrossRoleView序列化数据
	private int win; // 战斗结果
	private int addScore; // 增加积分
	private int score; // 增加积分
	private int power; // 战力
	private String movieId; // 战报ID
	private Date createDate; // 创建时间

	public RoleTournamentRecord() {
		super();
	}

	public RoleTournamentRecord(String id, Role role, int local, String roleViewData, int win, int addScore, int score, int power,
			String movieId, Date createDate) {
		super();
		this.id = id;
		this.role = role;
		this.local = local;
		this.roleViewData = roleViewData;
		this.win = win;
		this.addScore = addScore;
		this.score = score;
		this.power = power;
		this.movieId = movieId;
		this.createDate = createDate;
	}

	/**
	 * @return the id
	 */
	@Id
	@Column(name = "id", nullable = false, length = 64)
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
	 * @return the role
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	public Role getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @return the local
	 */
	@Column(name = "local", nullable = false)
	public int getLocal() {
		return local;
	}

	/**
	 * @param local
	 *            the local to set
	 */
	public void setLocal(int local) {
		this.local = local;
	}

	/**
	 * @return the roleViewData
	 */
	@Column(name = "view_data", nullable = false)
	public String getRoleViewData() {
		return roleViewData;
	}

	/**
	 * @param roleViewData
	 *            the roleViewData to set
	 */
	public void setRoleViewData(String roleViewData) {
		this.roleViewData = roleViewData;
	}

	/**
	 * @return the win
	 */
	@Column(name = "win", nullable = false)
	public int getWin() {
		return win;
	}

	/**
	 * @param win
	 *            the win to set
	 */
	public void setWin(int win) {
		this.win = win;
	}

	/**
	 * @return the movieId
	 */
	@Column(name = "movie_id", nullable = false)
	public String getMovieId() {
		return movieId;
	}

	/**
	 * @return the score
	 */
	@Column(name = "score", nullable = false)
	public int getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * @return the addScore
	 */
	@Column(name = "add_score", nullable = false)
	public int getAddScore() {
		return addScore;
	}

	/**
	 * @param addScore the addScore to set
	 */
	public void setAddScore(int addScore) {
		this.addScore = addScore;
	}

	/**
	 * @return the power
	 */
	@Column(name = "power", nullable = false)
	public int getPower() {
		return power;
	}

	/**
	 * @param power the power to set
	 */
	public void setPower(int power) {
		this.power = power;
	}

	/**
	 * @param movieId
	 *            the movieId to set
	 */
	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	/**
	 * @return the createDate
	 */
	@Column(name = "create_date", nullable = false)
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate
	 *            the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}

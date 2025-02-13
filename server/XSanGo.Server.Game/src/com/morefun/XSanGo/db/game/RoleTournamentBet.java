package com.morefun.XSanGo.db.game;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.morefun.XSanGo.util.TextUtil;

/**
 * 比武大会，押注
 * 
 * @author guofeng.qin
 */
@Entity
@Table(name = "role_tournament_bet")
public class RoleTournamentBet {
	private String id;
	private String roleId;
	private int num; // 第几届比武大会
	private int stage; // 第几轮
	private int rmby; // 押注金额
	private int fightId; // 对阵的ID
	private String betRoleId; // 压的玩家ID
	private int winornot; // 0压失败， 1压胜利
	private int result; // 结果: 0,未开结果;1,压中;2,未押中
	private Date createDate;

	public RoleTournamentBet() {
		super();
	}

	public RoleTournamentBet(String id, String roleId, int num, int stage, int rmby, int fightId, String betRoleId,
			int winornot, int result, Date createDate) {
		super();
		this.id = id;
		this.roleId = roleId;
		this.num = num;
		this.stage = stage;
		this.rmby = rmby;
		this.fightId = fightId;
		this.betRoleId = betRoleId;
		this.winornot = winornot;
		this.result = result;
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
	 * @return the roleId
	 */
	@Column(name = "role_id", nullable = false)
	public String getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId
	 *            the roleId to set
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the num
	 */
	@Column(name = "num", nullable = false)
	public int getNum() {
		return num;
	}

	/**
	 * @param num
	 *            the num to set
	 */
	public void setNum(int num) {
		this.num = num;
	}

	/**
	 * @return the fightId
	 */
	@Column(name = "fight_id", nullable = false)
	public int getFightId() {
		return fightId;
	}

	/**
	 * @param fightId
	 *            the fightId to set
	 */
	public void setFightId(int fightId) {
		this.fightId = fightId;
	}

	/**
	 * @return the betRoleId
	 */
	@Column(name = "bet_role_id", nullable = false)
	public String getBetRoleId() {
		return betRoleId;
	}

	/**
	 * @param betRoleId
	 *            the betRoleId to set
	 */
	public void setBetRoleId(String betRoleId) {
		this.betRoleId = betRoleId;
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
	 * @return the createDate
	 */
	@Column(name = "creat_date", nullable = false)
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

	/**
	 * @return the stage
	 */
	@Column(name = "stage", nullable = false)
	public int getStage() {
		return stage;
	}

	/**
	 * @param stage
	 *            the stage to set
	 */
	public void setStage(int stage) {
		this.stage = stage;
	}

	/**
	 * @return the rmby
	 */
	@Column(name = "rmby", nullable = false)
	public int getRmby() {
		return rmby;
	}

	/**
	 * @param rmby
	 *            the rmby to set
	 */
	public void setRmby(int rmby) {
		this.rmby = rmby;
	}

	/**
	 * @return the winornot
	 */
	@Column(name = "win_or_not", nullable = false)
	public int getWinornot() {
		return winornot;
	}

	/**
	 * @param winornot
	 *            the winornot to set
	 */
	public void setWinornot(int winornot) {
		this.winornot = winornot;
	}

	@Override
	public String toString() {
		return TextUtil.GSON.toJson(this);
	}

}

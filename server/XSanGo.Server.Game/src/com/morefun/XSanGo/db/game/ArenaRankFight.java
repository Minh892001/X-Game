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
 * 竞技场排行榜 战报信息
 * 
 * @author lvmingtao
 *
 */
@Entity
@Table(name = "role_arena_rank_fight")
public class ArenaRankFight implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6337865597960080127L;

	private String id;
	private String fightId; // 战报信息ID
	private String movieId; // 战报录像ID
	private int rankCurrent; // 战斗之后的当前排名
	private int rankChange; // 排名变化, 负数：下降名次
	private int sneerId; // 炫耀ID,是否炫耀
	private String reward; // 获得奖励数量
	private int flag; // 输赢标记 0：输，1：赢
	private String rivalId; // 对手的ID
	private Date fightTime; // 发生战报的时间
	private int type; // 战报类型 0：主动发起，1：被动接受挑战, 2: 主动复仇, 3: 被复仇
	private Role role;

	/** default constructor */
	public ArenaRankFight() {

	}

	/** full constructor */
	public ArenaRankFight(String id, Role role, String fightId, String movieId,
			int rankCurrent, int rankChange, int sneerId, String reward,
			int flag, String rivalId, Date fightTime, int type) {

		this.id = id;
		this.role = role;
		this.fightId = fightId;
		this.movieId = movieId;
		this.rankCurrent = rankCurrent;
		this.rankChange = rankChange;
		this.sneerId = sneerId;
		this.reward = reward;
		this.flag = flag;
		this.rivalId = rivalId;
		this.type = type;
		this.fightTime = fightTime;
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

	@Column(name = "fight_id")
	public String getFightId() {
		return fightId;
	}

	public void setFightId(String fightId) {
		this.fightId = fightId;
	}

	/**
	 * @return the movieId
	 */
	@Column(name = "movie_id", nullable = false)
	public String getMovieId() {
		return movieId;
	}

	/**
	 * @param movieId the movieId to set
	 */
	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	@Column(name = "rank_current", columnDefinition = "INT default 0")
	public int getRankCurrent() {
		return rankCurrent;
	}

	public void setRankCurrent(int rankCurrent) {
		this.rankCurrent = rankCurrent;
	}

	@Column(name = "rank_change")
	public int getRankChange() {
		return rankChange;
	}

	public void setRankChange(int rankChange) {
		this.rankChange = rankChange;
	}

	@Column(name = "sneer_id")
	public int getSneerId() {
		return sneerId;
	}

	public void setSneerId(int sneerId) {
		this.sneerId = sneerId;
	}

	@Column(name = "reward")
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	@Column(name = "flag")
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@Column(name = "rival_id")
	public String getRivalId() {
		return rivalId;
	}

	public void setRivalId(String rivalId) {
		this.rivalId = rivalId;
	}

	/**
	 * 战报类型 0：主动发起，1：被动接受挑战
	 * 
	 * @return
	 */
	@Column(name = "type", columnDefinition = "INT default 0")
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Column(name = "fight_time")
	public Date getFightTime() {
		return fightTime;
	}

	public void setFightTime(Date fightTime) {
		this.fightTime = fightTime;
	}
}

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
 * 群雄争霸 战报 数据库结构
 * 
 * @author 吕明涛
 * 
 */
@Entity
@Table(name = "role_ladder_report")
public class RoleLadderReport implements Serializable {
	/** */
	private static final long serialVersionUID = 6669155281976392335L;

	private String id;
	private String fightId; // 战报信息ID
	private int levelChange; // 等级 变化 ，负数代表降级
	private int starChange; // 星级 变化 ，负数代表降星
	private int state; // 战斗结果，0：败，1：胜
	private String rivalId; // 对手的ID
	private int rivalLevel; // 对手 等级
	private Date fightTime; // 发生战报的时间
	private Role role;

	/** default constructor */
	public RoleLadderReport() {

	}

	/** full constructor */
	public RoleLadderReport(String id, Role role, String fightId,
			int levelChange, int starChange, int state, String rivalId,
			int rivalLevel, Date fightTime) {
		this.id = id;
		this.role = role;
		this.fightId = fightId;
		this.levelChange = levelChange;
		this.starChange = starChange;
		this.state = state;
		this.rivalId = rivalId;
		this.rivalLevel = rivalLevel;
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

	@Column(name = "fight_id", nullable = false)
	public String getFightId() {
		return fightId;
	}

	public void setFightId(String fightId) {
		this.fightId = fightId;
	}

	@Column(name = "level_change", columnDefinition = "INT default 0")
	public int getLevelChange() {
		return levelChange;
	}

	public void setLevelChange(int levelChange) {
		this.levelChange = levelChange;
	}

	@Column(name = "star_change", columnDefinition = "INT default 0")
	public int getStarChange() {
		return starChange;
	}

	public void setStarChange(int starChange) {
		this.starChange = starChange;
	}

	@Column(name = "state", columnDefinition = "INT default 0")
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Column(name = "rival_id", nullable = false)
	public String getRivalId() {
		return rivalId;
	}

	public void setRivalId(String rivalId) {
		this.rivalId = rivalId;
	}

	@Column(name = "rival_level", columnDefinition = "INT default 0")
	public int getRivalLevel() {
		return rivalLevel;
	}

	public void setRivalLevel(int rivalLevel) {
		this.rivalLevel = rivalLevel;
	}

	@Column(name = "fight_time", nullable = false)
	public Date getFightTime() {
		return fightTime;
	}

	public void setFightTime(Date fightTime) {
		this.fightTime = fightTime;
	}
}

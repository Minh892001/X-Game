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
 * 寻宝
 * 
 * @author lixiongming
 *
 */
@Entity
@Table(name = "role_treasure")
public class RoleTreasure implements Serializable {
	private static final long serialVersionUID = -1276736796434995439L;
	private String id;
	private Role role;
	private String heroIds;// ,分割
	private String recommendHeroJson;// RecommendHero[]的json
	private Date departDate;// 出发时间
	private Date refreshDate;// 推荐武将刷新时间
	private int recommendHeroNum;// 出发寻宝上阵推荐武将个数
	private int groupNum;// 队伍编号
	private int activityHeroNum;// 出发寻宝上阵活动武将个数

	private int speedNum;// 使用加速次数
	private String eventIds;// 触发的事件ID
	private String accidentIds;// 矿难ID

	public RoleTreasure() {

	}

	public RoleTreasure(String id, Role role, String heroIds, String recommendHeroJson, Date departDate,
			Date refreshDate, int recommendHeroNum, int groupNum) {
		super();
		this.id = id;
		this.role = role;
		this.heroIds = heroIds;
		this.recommendHeroJson = recommendHeroJson;
		this.departDate = departDate;
		this.refreshDate = refreshDate;
		this.recommendHeroNum = recommendHeroNum;
		this.groupNum = groupNum;
	}

	@Id
	@Column(name = "id", nullable = false, length = 64)
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

	@Column(name = "hero_ids")
	public String getHeroIds() {
		return heroIds;
	}

	public void setHeroIds(String heroIds) {
		this.heroIds = heroIds;
	}

	@Column(name = "recommend_hero_json")
	public String getRecommendHeroJson() {
		return recommendHeroJson;
	}

	public void setRecommendHeroJson(String recommendHeroJson) {
		this.recommendHeroJson = recommendHeroJson;
	}

	@Column(name = "depart_date")
	public Date getDepartDate() {
		return departDate;
	}

	public void setDepartDate(Date departDate) {
		this.departDate = departDate;
	}

	@Column(name = "refresh_date")
	public Date getRefreshDate() {
		return refreshDate;
	}

	public void setRefreshDate(Date refreshDate) {
		this.refreshDate = refreshDate;
	}

	@Column(name = "recommend_hero_num")
	public int getRecommendHeroNum() {
		return recommendHeroNum;
	}

	public void setRecommendHeroNum(int recommendHeroNum) {
		this.recommendHeroNum = recommendHeroNum;
	}

	@Column(name = "group_num", nullable = false)
	public int getGroupNum() {
		return groupNum;
	}

	public void setGroupNum(int groupNum) {
		this.groupNum = groupNum;
	}

	@Column(name = "activity_hero_num", nullable = false)
	public int getActivityHeroNum() {
		return activityHeroNum;
	}

	public void setActivityHeroNum(int activityHeroNum) {
		this.activityHeroNum = activityHeroNum;
	}

	@Column(name = "speed_num", nullable = false)
	public int getSpeedNum() {
		return speedNum;
	}

	public void setSpeedNum(int speedNum) {
		this.speedNum = speedNum;
	}

	@Column(name = "event_ids")
	public String getEventIds() {
		return eventIds;
	}

	public void setEventIds(String eventIds) {
		this.eventIds = eventIds;
	}

	@Column(name = "accident_ids")
	public String getAccidentIds() {
		return accidentIds;
	}

	public void setAccidentIds(String accidentIds) {
		this.accidentIds = accidentIds;
	}

}

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
 * 名将召唤数据
 * 
 * @author qinguofeng
 */
@Entity
@Table(name = "role_collect_soul")
public class RoleCollectHeroSoul implements Serializable {
	private static final long serialVersionUID = -6147714998187650556L;

	private String id;
	private Role role;
	private int type; // 召唤类型
	private int collectCount; // 转盘次数
	private int dayGoldCollectCount; // 今日元宝转盘次数
	private Date lastRefreshTime; // 上次刷新时间
	private Date lastDayRefreshTime; // 上次每日充值的时间
	private String viewJsonStr; // CollectHeroSoulView 的 json 序列化数据

	public RoleCollectHeroSoul() {

	}

	public RoleCollectHeroSoul(Role role, String id, int type, int collectCount, int dayGoldCollectCount,
			Date lastRefreshTime, Date lastDayRefreshTime, String viewJsonStr) {
		this.id = id;
		this.role = role;
		this.type = type;
		this.collectCount = collectCount;
		this.dayGoldCollectCount = dayGoldCollectCount;
		this.lastRefreshTime = lastRefreshTime;
		this.lastDayRefreshTime = lastDayRefreshTime;
		this.viewJsonStr = viewJsonStr;
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

	@Column(name = "collect_type", nullable = false)
	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Column(name = "collect_count", nullable = false, columnDefinition = "int default 0")
	public int getCollectCount() {
		return collectCount;
	}

	public void setCollectCount(int count) {
		this.collectCount = count;
	}

	@Column(name = "last_refresh_time", nullable = false)
	public Date getLastRefreshTime() {
		return lastRefreshTime;
	}

	/**
	 * @return the dayGoldCollectCount
	 */
	@Column(name = "day_gold_collect_count", nullable = false, columnDefinition = "int default 0")
	public int getDayGoldCollectCount() {
		return dayGoldCollectCount;
	}

	/**
	 * @param dayGoldCollectCount
	 *            the dayGoldCollectCount to set
	 */
	public void setDayGoldCollectCount(int dayGoldCollectCount) {
		this.dayGoldCollectCount = dayGoldCollectCount;
	}

	public void setLastRefreshTime(Date time) {
		this.lastRefreshTime = time;
	}


	@Column(name = "view_json", nullable = false, columnDefinition = "text")
	public String getViewJsonStr() {
		return this.viewJsonStr;
	}

	public void setViewJsonStr(String str) {
		this.viewJsonStr = str;
	}

	/**
	 * @return the lastDayRefreshTime
	 */
	@Column(name = "last_day_refresh_time", nullable = true)
	public Date getLastDayRefreshTime() {
		return lastDayRefreshTime;
	}
	
	/**
	 * @param lastDayRefreshTime the lastDayRefreshTime to set
	 */
	public void setLastDayRefreshTime(Date lastDayRefreshTime) {
		this.lastDayRefreshTime = lastDayRefreshTime;
	}
}

/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 限时武将
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "limit_hero")
public class LimitHero implements Serializable {
	private String id;
	private int weekScriptId;// 周武将ID
	private String todayScriptIds;//今日武将ID,分割
	private Date weekRefreshDate;//周武将刷新时间
	private Date todayRefreshDate;//今日武将刷新时间
	
	public LimitHero(){
		
	}
	
	public LimitHero(String id, int weekScriptId, String todayScriptIds,
			Date weekRefreshDate, Date todayRefreshDate) {
		super();
		this.id = id;
		this.weekScriptId = weekScriptId;
		this.todayScriptIds = todayScriptIds;
		this.weekRefreshDate = weekRefreshDate;
		this.todayRefreshDate = todayRefreshDate;
	}

	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name="week_script_id")
	public int getWeekScriptId() {
		return weekScriptId;
	}

	public void setWeekScriptId(int weekScriptId) {
		this.weekScriptId = weekScriptId;
	}

	@Column(name="today_script_ids")
	public String getTodayScriptIds() {
		return todayScriptIds;
	}

	public void setTodayScriptIds(String todayScriptIds) {
		this.todayScriptIds = todayScriptIds;
	}

	@Column(name="week_refresh_date")
	public Date getWeekRefreshDate() {
		return weekRefreshDate;
	}

	public void setWeekRefreshDate(Date weekRefreshDate) {
		this.weekRefreshDate = weekRefreshDate;
	}

	@Column(name="today_refresh_date")
	public Date getTodayRefreshDate() {
		return todayRefreshDate;
	}

	public void setTodayRefreshDate(Date todayRefreshDate) {
		this.todayRefreshDate = todayRefreshDate;
	}

}

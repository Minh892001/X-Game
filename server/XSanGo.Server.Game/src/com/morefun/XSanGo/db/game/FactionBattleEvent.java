/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattleEvent
 * 功能描述：
 * 文件名：FactionBattleEvent.java
 **************************************************
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 公会战随机事件数据
 * 
 * @author zwy
 * @since 2016-1-20
 * @version 1.0
 */
@Entity
@Table(name = "faction_battle_event")
public class FactionBattleEvent implements Serializable {
	private static final long serialVersionUID = -1847780827764854499L;

	/** 据点 */
	private int strongholdId;

	/** 事件编号 */
	private int eventId;

	/** 事件物品 */
	private String eventItems;

	/** 奖励领取状态 1领取 */
	private byte drawState;

	/** 事件时间 */
	private Date eventTime;

	/**
	 * @return Returns the strongholdId.
	 */
	@Id
	@Column(name = "stronghold_id")
	public int getStrongholdId() {
		return strongholdId;
	}

	/**
	 * @param strongholdId The strongholdId to set.
	 */
	public void setStrongholdId(int strongholdId) {
		this.strongholdId = strongholdId;
	}

	/**
	 * @return Returns the eventId.
	 */
	@Column(name = "event_id")
	public int getEventId() {
		return eventId;
	}

	/**
	 * @param eventId The eventId to set.
	 */
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	/**
	 * @return Returns the eventItems.
	 */
	@Column(name = "event_items")
	public String getEventItems() {
		return eventItems;
	}

	/**
	 * @param eventItems The eventItems to set.
	 */
	public void setEventItems(String eventItems) {
		this.eventItems = eventItems;
	}

	/**
	 * @return Returns the drawState.
	 */
	@Column(name = "draw_state")
	public byte getDrawState() {
		return drawState;
	}

	/**
	 * @param drawState The drawState to set.
	 */
	public void setDrawState(byte drawState) {
		this.drawState = drawState;
	}

	/**
	 * @return Returns the eventTime.
	 */
	@Column(name = "event_time", nullable = true, length = 19)
	public Date getEventTime() {
		return eventTime;
	}

	/**
	 * @param eventTime The eventTime to set.
	 */
	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}
}

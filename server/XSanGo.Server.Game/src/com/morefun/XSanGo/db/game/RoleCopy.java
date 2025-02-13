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
 * @author sulingyun
 * 
 */
@Entity
@Table(name = "role_copy")
public class RoleCopy implements Serializable {
	private static final long serialVersionUID = 6975789040641177418L;
	private String id;
	private Role role;
	private int templateId;
	private byte star;
	private short countToday;
	private int countTotal;
	private Date lastTime;
	private int hudongCount;// 膜拜次数
	private Date lastHudongDate;// 膜拜时间
	/** 关卡重置次数 */
	private int buyResetCount;
	/** 最后一次重置关卡时间 */
	private Date lastBuyResetTime;
	private Date levyDate;// 征收时间
	private Date occupyDate;// 占领时间

	public RoleCopy() {
	}

	public RoleCopy(String id, Role role, int templateId, Date lastTime) {
		this.id = id;
		this.role = role;
		this.templateId = templateId;
		this.star = 0;
		this.countToday = 0;
		this.countTotal = 0;
		this.lastTime = lastTime;
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

	@Column(name = "template_id", nullable = false)
	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	@Column(name = "star", nullable = false)
	public byte getStar() {
		return star;
	}

	public void setStar(byte star) {
		this.star = star;
	}

	@Column(name = "count_today", nullable = false)
	public short getCountToday() {
		return countToday;
	}

	public void setCountToday(short countToday) {
		this.countToday = countToday;
	}

	@Column(name = "last_time", nullable = false)
	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	@Column(name = "count_total", nullable = false)
	public int getCountTotal() {
		return countTotal;
	}

	public void setCountTotal(int countTotal) {
		this.countTotal = countTotal;
	}

	@Column(name = "hudong_count", nullable = false, columnDefinition = "int default 0")
	public int getHudongCount() {
		return hudongCount;
	}

	public void setHudongCount(int hudongCount) {
		this.hudongCount = hudongCount;
	}

	@Column(name = "last_hudong_date")
	public Date getLastHudongDate() {
		return lastHudongDate;
	}

	public void setLastHudongDate(Date lastHudongDate) {
		this.lastHudongDate = lastHudongDate;
	}

	@Column(name = "buy_reset_count", nullable = false, columnDefinition = "int default 0")
	public int getCopyResetCount() {
		return buyResetCount;
	}

	public void setCopyResetCount(int copyResetCount) {
		this.buyResetCount = copyResetCount;
	}

	@Column(name = "last_buy_reset_time")
	public Date getLastCopyResetTime() {
		return lastBuyResetTime;
	}

	public void setLastCopyResetTime(Date lastCopyResetTime) {
		this.lastBuyResetTime = lastCopyResetTime;
	}

	@Column(name = "levy_date")
	public Date getLevyDate() {
		return levyDate;
	}

	public void setLevyDate(Date levyDate) {
		this.levyDate = levyDate;
	}

	@Column(name = "occupy_date")
	public Date getOccupyDate() {
		return occupyDate;
	}

	public void setOccupyDate(Date occupyDate) {
		this.occupyDate = occupyDate;
	}

}

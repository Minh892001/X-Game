/**
 * 
 */
package com.morefun.XSanGo.db.stat;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author sulingyun
 *
 */
@Entity
@Table(name = "level_spread")
public class LevelSpread implements Serializable {
	private long id;
	private Date statDate;
	private int serverId;
	private int level;
	private int num;

	public LevelSpread(Date statDate, int serverId, int level, int num) {
		this.statDate = statDate;
		this.serverId = serverId;
		this.level = level;
		this.num = num;
	}

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "stat_date", nullable = false, columnDefinition = "date not null")
	public Date getStatDate() {
		return statDate;
	}

	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}

	@Column(name = "server_id", nullable = false)
	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	@Column(name = "level", nullable = false)
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Column(name = "num", nullable = false)
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}

/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * 服务器开关日志
 * 
 * @author sulingyun
 *
 */
@Entity
@Table(name = "server_open_close_log")
public class ServerOpenCloseLog implements Serializable {

	private static final long serialVersionUID = -2153049896368030105L;
	private int id;
	private Date openTime;
	private Date closeTime;
	private Set<ServerPerformance> performances = new HashSet<ServerPerformance>();

	public ServerOpenCloseLog() {

	}

	public ServerOpenCloseLog(Date openTime, Date closeTime) {
		this.openTime = openTime;
		this.closeTime = closeTime;

	}

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "open_time", nullable = false)
	public Date getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}

	@Column(name = "close_time")
	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "parent")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public Set<ServerPerformance> getPerformances() {
		return performances;
	}

	public void setPerformances(Set<ServerPerformance> performances) {
		this.performances = performances;
	}
}

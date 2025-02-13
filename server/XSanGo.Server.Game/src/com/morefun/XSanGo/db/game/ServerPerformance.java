/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author sulingyun
 * 
 */
@Entity
@Table(name = "server_performance")
public class ServerPerformance implements Serializable {

	private static final long serialVersionUID = -323259257999357599L;
	private int id;
	private ServerOpenCloseLog parent;
	private String name;
	/** 执行次数 */
	private int processCount;
	/** 总执行时间 */
	private long totalCostTime;
	private int maxCostTime;
	private int avgCost;

	public ServerPerformance() {
	}

	public ServerPerformance(ServerOpenCloseLog parent, String name, int processCount, long totalCostTime,
			int maxCostTime, int avgCost) {
		this.parent = parent;
		this.name = name;
		this.processCount = processCount;
		this.totalCostTime = totalCostTime;
		this.maxCostTime = maxCostTime;
		this.avgCost = avgCost;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "open_close_id", nullable = false)
	public ServerOpenCloseLog getParent() {
		return parent;
	}

	public void setParent(ServerOpenCloseLog parent) {
		this.parent = parent;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "process_count", nullable = false)
	public int getProcessCount() {
		return processCount;
	}

	public void setProcessCount(int processCount) {
		this.processCount = processCount;
	}

	@Column(name = "total_cost", nullable = false)
	public long getTotalCostTime() {
		return totalCostTime;
	}

	public void setTotalCostTime(long totalCostTime) {
		this.totalCostTime = totalCostTime;
	}

	@Column(name = "max_cost", nullable = false)
	public int getMaxCostTime() {
		return maxCostTime;
	}

	public void setMaxCostTime(int maxCostTime) {
		this.maxCostTime = maxCostTime;
	}

	@Column(name = "avg_cost", nullable = false)
	public int getAvgCost() {
		return avgCost;
	}

	public void setAvgCost(int avgCost) {
		this.avgCost = avgCost;
	}
}

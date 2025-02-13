package com.morefun.XSanGo.db;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 跨服冠军记录
 * 
 * @author xiongming.li
 *
 */
@Entity
public class CrossLog implements Serializable {

	private static final long serialVersionUID = 8245049653190636367L;
	private int id;
	private int periodNum;// 第几届比武大会
	private int championServerId;// 冠军服务器ID
	private String championId;// 冠军ID
	private Date createDate = new Date();

	public CrossLog() {

	}

	public CrossLog(int periodNum, int championServerId, String championId) {
		this.periodNum = periodNum;
		this.championServerId = championServerId;
		this.championId = championId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPeriodNum() {
		return periodNum;
	}

	public void setPeriodNum(int periodNum) {
		this.periodNum = periodNum;
	}

	public int getChampionServerId() {
		return championServerId;
	}

	public void setChampionServerId(int championServerId) {
		this.championServerId = championServerId;
	}

	public String getChampionId() {
		return championId;
	}

	public void setChampionId(String championId) {
		this.championId = championId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}

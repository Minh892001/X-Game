/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.db;

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
 * 兑换码批次
 * 
 * @author BruceSu
 * 
 */
@Entity
@Table(name = "cdk_group")
public class CDKGroup implements Serializable {
	private int id;
	private String name;
	private String category;
	private String channels;
	private String servers;
	private int minLevel;
	private int maxLevel;
	private String factionName;
	private int chargeMoney;
	private Date beginTime;
	private Date endTime;
	private String content;
	private Date createTime;
	private String remark;

	private Set<CDKDetail> cdks = new HashSet<CDKDetail>(0);

	public CDKGroup() {

	}

	public CDKGroup(String name, String category, String channels, String servers, int minLevel, int maxLevel,
			String factionName, int chargeMoney, Date beginTime, Date endTime, String content, Date createTime,
			String remark) {
		this.name = name;
		this.category = category;
		this.channels = channels;
		this.servers = servers;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
		this.factionName = factionName;
		this.chargeMoney = chargeMoney;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.content = content;
		this.createTime = createTime;
		this.remark = remark;
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

	@Column(name = "remark", nullable = false)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "content", nullable = false)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "group")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public Set<CDKDetail> getCdks() {
		return cdks;
	}

	public void setCdks(Set<CDKDetail> cdks) {
		this.cdks = cdks;
	}

	@Column(name = "create_time", nullable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "category", nullable = false, unique = true)
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "channels", nullable = false)
	public String getChannels() {
		return channels;
	}

	public void setChannels(String channels) {
		this.channels = channels;
	}

	@Column(name = "min_level", nullable = false)
	public int getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}

	@Column(name = "max_level", nullable = false)
	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	@Column(name = "faction_name", nullable = false)
	public String getFactionName() {
		return factionName;
	}

	public void setFactionName(String factionName) {
		this.factionName = factionName;
	}

	@Column(name = "charge_money", nullable = false)
	public int getChargeMoney() {
		return chargeMoney;
	}

	public void setChargeMoney(int chargeMoney) {
		this.chargeMoney = chargeMoney;
	}

	@Column(name = "begin_time", nullable = false)
	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	@Column(name = "end_time", nullable = false)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "servers", nullable = false)
	public String getServers() {
		return servers;
	}

	public void setServers(String servers) {
		this.servers = servers;
	}
}

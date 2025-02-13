/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * 兑换码
 * 
 * @author BruceSu
 * 
 */
@Entity
@Table(name = "cdk_detail")
public class CDKDetail implements Serializable {
	private String cdk;
	private CDKGroup group;
	private int useTime;
	private int maxUseTime;

	public CDKDetail() {

	}

	public CDKDetail(String cdk, CDKGroup group, int useTime, int maxUseTime) {
		this.cdk = cdk;
		this.group = group;
		this.useTime = useTime;
		this.maxUseTime = maxUseTime;
	}

	@Id
	@Column(name = "cdk", nullable = false)
	public String getCdk() {
		return cdk;
	}

	public void setCdk(String cdk) {
		this.cdk = cdk;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "group_id", nullable = false)
	@Fetch(FetchMode.SELECT)
	public CDKGroup getGroup() {
		return group;
	}

	public void setGroup(CDKGroup group) {
		this.group = group;
	}

	@Column(name = "use_time", nullable = false)
	public int getUseTime() {
		return useTime;
	}

	public void setUseTime(int useTime) {
		this.useTime = useTime;
	}

	@Column(name = "max_use_time", nullable = false)
	public int getMaxUseTime() {
		return maxUseTime;
	}

	public void setMaxUseTime(int maxUseTime) {
		this.maxUseTime = maxUseTime;
	}
}

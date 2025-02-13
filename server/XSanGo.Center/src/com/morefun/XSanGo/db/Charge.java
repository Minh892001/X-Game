/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Charge entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "charge")
public class Charge implements java.io.Serializable {
	public static final int State_Init = 0;//未通知游戏服务器加值的订单或是超时未处理的订单
	public static final int State_Sucess = 1;//处理成功的订单
	// Fields

	private String orderId;
	private int state;
	private int serverId;
	private String account;
	private String roleId;
	/** 充值金额(单位为分) */
	private int cent;
	/** 支付通道编号 */
	private int pmId;
	/** 充值卡类型 */
	private int cardType;
	/** 合作商渠道编号 */
	private int channel;
	private Date createTime;
	private String params;
	/** 游戏服处理成功时间 */
	private Date completeTime;

	/**
	 * 货币类型
	 */
	private String currency;

	// Constructors

	/** default constructor */
	public Charge() {
	}

	/** full constructor */
	public Charge(String orderId, int channel, String account, int serverId, String roleId, int cent, int pmId,
			int cardType, Date createTime, int state, String params, String currency) {
		this.orderId = orderId;
		this.channel = channel;
		this.account = account;
		this.cent = cent;
		this.pmId = pmId;
		this.cardType = cardType;
		this.createTime = createTime;
		this.state = state;
		this.serverId = serverId;
		this.roleId = roleId;
		this.params = params;
		this.currency = currency;
	}

	// Property accessors
	@Id
	@Column(name = "order_id", nullable = false)
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Column(name = "channel", nullable = false)
	public int getChannel() {
		return this.channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	@Column(name = "account", nullable = false)
	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "cent", nullable = false)
	public int getCent() {
		return this.cent;
	}

	public void setCent(int cent) {
		this.cent = cent;
	}

	@Column(name = "create_time", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "state", nullable = false)
	public int getState() {
		return this.state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Column(name = "server_id", nullable = false)
	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	@Column(name = "role_id", nullable = false)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Column(name = "params")
	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	@Column(name = "complete_time", length = 19)
	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	@Column(name = "pm_id")
	public int getPmId() {
		return pmId;
	}

	public void setPmId(int pmId) {
		this.pmId = pmId;
	}

	@Column(name = "card_type")
	public int getCardType() {
		return cardType;
	}

	public void setCardType(int cardType) {
		this.cardType = cardType;
	}

	@Column(name = "currency")
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
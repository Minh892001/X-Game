/**
 * 
 */
package com.morefun.XSanGo.db.stat;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author sulingyun
 *
 */
@Entity
@Table(name = "charge_log")
public class ChargeLog implements Serializable {
	private String orderId;
	private String account;
	private int cent;
	private int serverId;
	private int registerToday;
	private int chargeCount;
	private int channel;
	/** 支付通道编号 */
	private int pmId;
	/** 充值卡类型 */
	private int cardType;
	private Date createTime;

	public ChargeLog(String orderId, String account, int cent, int serverId,
			int registerToday, int chargeCount, int channel, int pmId,
			int cardType) {
		this.orderId = orderId;
		this.account = account;
		this.cent = cent;
		this.serverId = serverId;
		this.registerToday = registerToday;
		this.chargeCount = chargeCount;
		this.channel = channel;
		this.pmId = pmId;
		this.cardType = cardType;
		this.createTime = Calendar.getInstance().getTime();
	}

	@Id
	@Column(name = "order_id", nullable = false)
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Column(name = "account", nullable = false)
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "cent", nullable = false)
	public int getCent() {
		return cent;
	}

	public void setCent(int cent) {
		this.cent = cent;
	}

	@Column(name = "register_today", nullable = false)
	public int getRegisterToday() {
		return registerToday;
	}

	public void setRegisterToday(int registerToday) {
		this.registerToday = registerToday;
	}

	@Column(name = "charge_count", nullable = false)
	public int getChargeCount() {
		return chargeCount;
	}

	public void setChargeCount(int chargeCount) {
		this.chargeCount = chargeCount;
	}

	@Column(name = "create_time", nullable = false, length = 19)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "pm_id", nullable = false, columnDefinition = "int default 0")
	public int getPmId() {
		return pmId;
	}

	public void setPmId(int pmId) {
		this.pmId = pmId;
	}

	@Column(name = "card_type", nullable = false, columnDefinition = "int default 0")
	public int getCardType() {
		return cardType;
	}

	public void setCardType(int cardType) {
		this.cardType = cardType;
	}

	@Column(name = "channel", nullable = false, columnDefinition = "int default 0")
	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	@Column(name = "server_id", nullable = false, columnDefinition = "int default 0")
	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
}

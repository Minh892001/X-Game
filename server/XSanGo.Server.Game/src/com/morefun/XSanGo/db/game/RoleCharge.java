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

@Entity
@Table(name = "role_charge")
public class RoleCharge implements Serializable {
	
	private static final long serialVersionUID = 4335804951245496100L;
	private String id;
	private RoleVip vip;
	private int chargeTemplateId;
	private Date createTime;
	private int chargeMoney;

	public RoleCharge() {
	}

	public RoleCharge(String id, RoleVip vip, int chargeTemplateId,
			Date createTime, int chargeMoney) {
		this.id = id;
		this.vip = vip;
		this.chargeTemplateId = chargeTemplateId;
		this.createTime = createTime;
		this.chargeMoney = chargeMoney;
	}

	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	public RoleVip getVip() {
		return vip;
	}

	public void setVip(RoleVip vip) {
		this.vip = vip;
	}

	@Column(name = "charge_template_id", nullable = false)
	public int getChargeTemplateId() {
		return chargeTemplateId;
	}

	public void setChargeTemplateId(int chargeTemplateId) {
		this.chargeTemplateId = chargeTemplateId;
	}

	@Column(name = "create_time", nullable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "charge_money", nullable = false)
	public int getChargeMoney() {
		return chargeMoney;
	}
	
	public void setChargeMoney(int chargeMoney) {
		this.chargeMoney = chargeMoney;
	}
}

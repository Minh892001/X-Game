package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author qinguofeng
 * @date Apr 6, 2015
 */
@Entity
@Table(name = "auction_house_log")
public class AuctionHouseLog implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	/** 用户ID */
	private String roleId;
	/** 其他用户ID */
	private String otherId;
	/** 物品模版ID */
	private String templateId;
	/** 操作类型 */
	private int operationType;
	/** 发生时间 */
	private Date updateTime;
	/** 金额 */
	private long money;
	/** 数量 */
	private int num;
	/** 拍卖ID */
	private String auctionId;

	/**
	 * @return the id
	 */
	@GenericGenerator(name = "generator", strategy = "identity")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the roleId
	 */
	@Column(name = "role_id", nullable = false)
	public String getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId
	 *            the roleId to set
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the otherId
	 */
	@Column(name = "other_id", nullable = true)
	public String getOtherId() {
		return otherId;
	}

	/**
	 * @param otherId
	 *            the otherId to set
	 */
	public void setOtherId(String otherId) {
		this.otherId = otherId;
	}

	/**
	 * @return the templateId
	 */
	@Column(name = "template_id", nullable = false)
	public String getTemplateId() {
		return templateId;
	}

	/**
	 * @param templateId
	 *            the templateId to set
	 */
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	/**
	 * @return the operationType
	 */
	@Column(name = "operation_type", nullable = false)
	public int getOperationType() {
		return operationType;
	}

	/**
	 * @param operationType
	 *            the operationType to set
	 */
	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}

	/**
	 * @return the updateTime
	 */
	@Column(name = "update_time", nullable = false)
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 *            the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the money
	 */
	@Column(name = "money", nullable = false)
	public long getMoney() {
		return money;
	}

	/**
	 * @param money
	 *            the money to set
	 */
	public void setMoney(long money) {
		this.money = money;
	}

	/**
	 * @return the num
	 */
	@Column(name = "num", nullable = true)
	public int getNum() {
		return num;
	}

	/**
	 * @param num
	 *            the num to set
	 */
	public void setNum(int num) {
		this.num = num;
	}

	/**
	 * @return the auctionId
	 */
	@Column(name = "auction_id", nullable = true)
	public String getAuctionId() {
		return auctionId;
	}

	/**
	 * @param auctionId
	 *            the auctionId to set
	 */
	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
	}

}

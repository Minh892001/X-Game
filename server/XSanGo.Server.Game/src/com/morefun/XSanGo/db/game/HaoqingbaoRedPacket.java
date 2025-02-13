package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 豪情宝，红包
 * 
 * @author guofeng.qin
 */
@Entity
@Table(name = "haoqingbao_item_packet")
public class HaoqingbaoRedPacket implements Serializable {
	private static final long serialVersionUID = 6716204190381918371L;

	private String id;
	/** 发红包的玩家ID */
	private String senderId;
	/** 领红包玩家的ID */
	private String receiverId;
	/** 关联item id */
	private String itemId;
	/** 元宝数量 */
	private int num;
	/** 运气王:0,不是；1,是 */
	private int luckyStar;
	/** 发红包事件 */
	private Date sendDate;
	/** 抢红包时间 */
	private Date receiveDate;

	public HaoqingbaoRedPacket() {
		super();
	}

	public HaoqingbaoRedPacket(String id, String senderId, String receiverId,
			String itemId, int num, int luckyStar, Date sendDate,
			Date receiveDate) {
		super();
		this.id = id;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.itemId = itemId;
		this.num = num;
		this.luckyStar = luckyStar;
		this.sendDate = sendDate;
		this.receiveDate = receiveDate;
	}

	/**
	 * @return the id
	 */
	@Id
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
	 * @return the senderId
	 */
	@Column(name = "sender_id", nullable = false)
	public String getSenderId() {
		return senderId;
	}

	/**
	 * @param senderId
	 *            the senderId to set
	 */
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	/**
	 * @return the receiverId
	 */
	@Column(name = "receiver_id", nullable = true)
	public String getReceiverId() {
		return receiverId;
	}

	/**
	 * @param receiverId
	 *            the receiverId to set
	 */
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	/**
	 * @return the itemId
	 */
	@Column(name = "item_id", nullable = false)
	public String getItemId() {
		return itemId;
	}

	/**
	 * @param itemId
	 *            the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the num
	 */
	@Column(name = "num", nullable = false)
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
	 * @return the luckyStar
	 */
	@Column(name = "lucky_star", nullable = false)
	public int getLuckyStar() {
		return luckyStar;
	}

	/**
	 * @param luckyStar
	 *            the luckyStar to set
	 */
	public void setLuckyStar(int luckyStar) {
		this.luckyStar = luckyStar;
	}

	/**
	 * @return the sendDate
	 */
	@Column(name = "send_date", nullable = false)
	public Date getSendDate() {
		return sendDate;
	}

	/**
	 * @param sendDate
	 *            the sendDate to set
	 */
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	/**
	 * @return the receiveDate
	 */
	@Column(name = "receive_date", nullable = true)
	public Date getReceiveDate() {
		return receiveDate;
	}

	/**
	 * @param receiveDate
	 *            the receiveDate to set
	 */
	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

}

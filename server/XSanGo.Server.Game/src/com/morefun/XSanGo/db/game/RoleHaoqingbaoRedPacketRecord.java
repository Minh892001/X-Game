package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 豪情宝，玩家抢红包的记录
 * 
 * @author guofeng.qin
 */
@Entity
@Table(name = "role_haoqingbao_redpacket_record")
public class RoleHaoqingbaoRedPacketRecord implements Serializable {
	private static final long serialVersionUID = 3202711649917803417L;

	private long id;
	private Role role;

	/** 关联红包ID */
	private String redPacketId;
	/** 发红包玩家ID */
	private String senderId;
	/** 红包金额 */
	private int num;
	/** 是否是运气王 */
	private int luckyStar;
	/** 抢到的时间 */
	private Date receiveDate;

	public RoleHaoqingbaoRedPacketRecord() {
		super();
	}

	public RoleHaoqingbaoRedPacketRecord(Role role, String redPacketId,
			String senderId, int num, int luckyStar, Date receiveDate) {
		super();
		this.role = role;
		this.redPacketId = redPacketId;
		this.senderId = senderId;
		this.num = num;
		this.luckyStar = luckyStar;
		this.receiveDate = receiveDate;
	}

	/**
	 * @return the id
	 */
	@GenericGenerator(name = "generator", strategy = "identity")
	@GeneratedValue(generator = "generator")
	@Id
	@Column(name = "id", nullable = false)
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the role
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	public Role getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @return the redPacketId
	 */
	@Column(name = "redpacket_id", nullable = false)
	public String getRedPacketId() {
		return redPacketId;
	}

	/**
	 * @param redPacketId
	 *            the redPacketId to set
	 */
	public void setRedPacketId(String redPacketId) {
		this.redPacketId = redPacketId;
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
	 * @return the receiveDate
	 */
	@Column(name = "recv_date", nullable = false)
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

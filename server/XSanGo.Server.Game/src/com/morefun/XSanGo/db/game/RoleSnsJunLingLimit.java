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
 * 好友送军令限制
 * 
 * @author guofeng.qin
 */
@Entity
@Table(name = "role_sns_junling_limit")
public class RoleSnsJunLingLimit implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private Role role;
	private String targetId;
	private int sendNum;
	private Date sendTime;
	private int recvNum;
	private Date recvTime;

	public RoleSnsJunLingLimit() {
		super();
	}

	public RoleSnsJunLingLimit(long id, Role role, String targetId,
			int sendNum, Date sendTime, int recvNum, Date recvTime) {
		super();
		this.id = id;
		this.role = role;
		this.targetId = targetId;
		this.sendNum = sendNum;
		this.sendTime = sendTime;
		this.recvNum = recvNum;
		this.recvTime = recvTime;
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
	 * @return the targetId
	 */
	@Column(name = "target_id", nullable = false)
	public String getTargetId() {
		return targetId;
	}

	/**
	 * @param targetId
	 *            the targetId to set
	 */
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	/**
	 * @return the sendNum
	 */
	@Column(name = "send_num", nullable = true)
	public int getSendNum() {
		return sendNum;
	}

	/**
	 * @param sendNum
	 *            the sendNum to set
	 */
	public void setSendNum(int sendNum) {
		this.sendNum = sendNum;
	}

	/**
	 * @return the sendTime
	 */
	@Column(name = "send_time", nullable = true)
	public Date getSendTime() {
		return sendTime;
	}

	/**
	 * @param sendTime
	 *            the sendTime to set
	 */
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	/**
	 * @return the recvNum
	 */
	@Column(name = "recv_num", nullable = true)
	public int getRecvNum() {
		return recvNum;
	}

	/**
	 * @param recvNum
	 *            the recvNum to set
	 */
	public void setRecvNum(int recvNum) {
		this.recvNum = recvNum;
	}

	/**
	 * @return the recvTime
	 */
	@Column(name = "recv_time", nullable = true)
	public Date getRecvTime() {
		return recvTime;
	}

	/**
	 * @param recvTime
	 *            the recvTime to set
	 */
	public void setRecvTime(Date recvTime) {
		this.recvTime = recvTime;
	}

}

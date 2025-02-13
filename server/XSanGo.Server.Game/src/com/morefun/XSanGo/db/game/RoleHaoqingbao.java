package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 豪情宝
 * 
 * @author guofeng.qin
 */
@Entity
@Table(name = "role_haoqingbao")
public class RoleHaoqingbao implements Serializable {
	private static final long serialVersionUID = 499471445040404411L;

	private String roleId;
	private Role role;

	/** 元宝数量 */
	private int yuanbaoNum;
	/** 当日抢红包的次数 */
	private int receiveRedPacketNum;
	/** 当日发红包的次数 */
	private int sendRedPacketNum;
	/** 总的抢红包次数 */
	private long totalRecvCount;
	/** 总的抢红包元宝之和 */
	private long totalRecvNum;
	/** 上次抢红包的时间 */
	private Date lastReceiveTime;
	/** 最后更新时间 */
	private Date updateTime;
	/** 运气王次数 */
	private int luckyStarCount;
	/** 总的发红包次数之和 */
	private long totalSendCount;
	/** 总的发红包金额之和 */
	private long totalSendSum;
	/** 上次充值状态的时间 */
	private long lastChargeStatusTime;

	public RoleHaoqingbao() {
		super();
	}

	public RoleHaoqingbao(String roleId, Role role, int yuanbaoNum,
			int receiveRedPacketNum, int sendRedPacketNum, long totalRecvCount,
			long totalRecvNum, Date lastReceiveTime, Date updateTime,
			int luckyStarCount, long totalSendCount, long totalSendSum) {
		super();
		this.roleId = roleId;
		this.role = role;
		this.yuanbaoNum = yuanbaoNum;
		this.receiveRedPacketNum = receiveRedPacketNum;
		this.sendRedPacketNum = sendRedPacketNum;
		this.totalRecvCount = totalRecvCount;
		this.totalRecvNum = totalRecvNum;
		this.lastReceiveTime = lastReceiveTime;
		this.updateTime = updateTime;
		this.luckyStarCount = luckyStarCount;
		this.totalSendCount = totalSendCount;
		this.totalSendSum = totalSendSum;
	}

	/**
	 * @return the roleId
	 */
	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "role"))
	@Id
	@GeneratedValue(generator = "generator")
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
	 * @return the role
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
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
	 * @return the yuanbaoNum
	 */
	@Column(name = "yuanbao_num", nullable = false)
	public int getYuanbaoNum() {
		return yuanbaoNum;
	}

	/**
	 * @param yuanbaoNum
	 *            the yuanbaoNum to set
	 */
	public void setYuanbaoNum(int yuanbaoNum) {
		this.yuanbaoNum = yuanbaoNum;
	}

	/**
	 * @return the receiveRedPacketNum
	 */
	@Column(name = "recv_num", nullable = false)
	public int getReceiveRedPacketNum() {
		return receiveRedPacketNum;
	}

	/**
	 * @return the totalRecvCount
	 */
	@Column(name = "total_count", nullable = false)
	public long getTotalRecvCount() {
		return totalRecvCount;
	}

	/**
	 * @param totalRecvCount
	 *            the totalRecvCount to set
	 */
	public void setTotalRecvCount(long totalRecvCount) {
		this.totalRecvCount = totalRecvCount;
	}

	/**
	 * @return the totalRecvNum
	 */
	@Column(name = "total_num", nullable = false)
	public long getTotalRecvNum() {
		return totalRecvNum;
	}

	/**
	 * @param totalRecvNum
	 *            the totalRecvNum to set
	 */
	public void setTotalRecvNum(long totalRecvNum) {
		this.totalRecvNum = totalRecvNum;
	}

	/**
	 * @param receiveRedPacketNum
	 *            the receiveRedPacketNum to set
	 */
	public void setReceiveRedPacketNum(int receiveRedPacketNum) {
		this.receiveRedPacketNum = receiveRedPacketNum;
	}

	/**
	 * @return the sendRedPacketNum
	 */
	@Column(name = "send_num", nullable = false)
	public int getSendRedPacketNum() {
		return sendRedPacketNum;
	}

	/**
	 * @param sendRedPacketNum
	 *            the sendRedPacketNum to set
	 */
	public void setSendRedPacketNum(int sendRedPacketNum) {
		this.sendRedPacketNum = sendRedPacketNum;
	}

	/**
	 * @return the lastReceiveTime
	 */
	@Column(name = "last_recv_time", nullable = true)
	public Date getLastReceiveTime() {
		return lastReceiveTime;
	}

	/**
	 * @param lastReceiveTime
	 *            the lastReceiveTime to set
	 */
	public void setLastReceiveTime(Date lastReceiveTime) {
		this.lastReceiveTime = lastReceiveTime;
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
	 * @return the luckyStarCount
	 */
	@Column(name = "lucky_star_count", nullable = false)
	public int getLuckyStarCount() {
		return luckyStarCount;
	}

	/**
	 * @param luckyStarCount
	 *            the luckyStarCount to set
	 */
	public void setLuckyStarCount(int luckyStarCount) {
		this.luckyStarCount = luckyStarCount;
	}

	/**
	 * @return the totalSendCount
	 */
	@Column(name = "total_send_count", nullable = false)
	public long getTotalSendCount() {
		return totalSendCount;
	}

	/**
	 * @param totalSendCount
	 *            the totalSendCount to set
	 */
	public void setTotalSendCount(long totalSendCount) {
		this.totalSendCount = totalSendCount;
	}

	/**
	 * @return the totalSendSum
	 */
	@Column(name = "total_send_sum", nullable = false)
	public long getTotalSendSum() {
		return totalSendSum;
	}

	/**
	 * @param totalSendSum
	 *            the totalSendSum to set
	 */
	public void setTotalSendSum(long totalSendSum) {
		this.totalSendSum = totalSendSum;
	}

	/**
	 * @return the lastChargeStatusTime
	 */
	@Column(name = "last_charge_status_time", nullable = false)
	public long getLastChargeStatusTime() {
		return lastChargeStatusTime;
	}

	/**
	 * @param lastChargeStatusTime the lastChargeStatusTime to set
	 */
	public void setLastChargeStatusTime(long lastChargeStatusTime) {
		this.lastChargeStatusTime = lastChargeStatusTime;
	}

}

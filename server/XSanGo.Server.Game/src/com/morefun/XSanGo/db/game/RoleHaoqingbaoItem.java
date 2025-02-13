package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 豪情宝，一次发红包行为, 关联一组HaoqingbaoItemRecord(红包)
 * 
 * @author guofeng.qin
 */
@Entity
@Table(name = "role_haoqingbao_item")
public class RoleHaoqingbaoItem implements Serializable {
	private static final long serialVersionUID = -4150698237208209051L;

	private String id;

	/** 用户ID, 发送者ID */
	private String roleId;
	/**
	 * 类型: 1,工会；2,好友；3,全服
	 * */
	private int type;
	/**
	 * 红包派发范围: 0;1;2 在线成员;会长和长老;所有成员 在线好友;所有好友
	 * */
	private int range;
	/** 最低等级 */
	private int minLevel;
	/** 最低vip等级 */
	private int minVipLevel;
	/** 最低友情点数 */
	private int minFriendPoint;
	/** 所有红包总额 */
	private int totalNum;
	/** 红包数量 */
	private int redPacketNum;
	/** 已经被抢的红包数量 */
	private int receivedNum;
	/** 红包留言 */
	private String msg;
	/** 工会红包，工会ID */
	private String factionId;
	/** 开始时间 */
	private Date startTime;
	/** 最后一次抢红包的时间 */
	private Date lastRecvTime;
	/** 是否已经结束 */
	private int finished;

	public RoleHaoqingbaoItem() {
		super();
	}

	public RoleHaoqingbaoItem(String id, String roleId, int type, int range,
			int minLevel, int minVipLevel, int minFriendPoint, int totalNum,
			int redPacketNum, int receivedNum, String msg, String factionId,
			Date startTime, Date lastRecvTime, int finished) {
		super();
		this.id = id;
		this.roleId = roleId;
		this.type = type;
		this.range = range;
		this.minLevel = minLevel;
		this.minVipLevel = minVipLevel;
		this.minFriendPoint = minFriendPoint;
		this.totalNum = totalNum;
		this.redPacketNum = redPacketNum;
		this.receivedNum = receivedNum;
		this.msg = msg;
		this.factionId = factionId;
		this.startTime = startTime;
		this.lastRecvTime = lastRecvTime;
		this.finished = finished;
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
	 * @return the range
	 */
	@Column(name = "send_range", nullable = false)
	public int getRange() {
		return range;
	}

	/**
	 * @param range
	 *            the range to set
	 */
	public void setRange(int range) {
		this.range = range;
	}

	/**
	 * @return the type
	 */
	@Column(name = "type", nullable = false)
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the minLevel
	 */
	@Column(name = "min_level", nullable = false)
	public int getMinLevel() {
		return minLevel;
	}

	/**
	 * @param minLevel
	 *            the minLevel to set
	 */
	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}

	/**
	 * @return the minVipLevel
	 */
	@Column(name = "min_vip_level", nullable = false)
	public int getMinVipLevel() {
		return minVipLevel;
	}

	/**
	 * @param minVipLevel
	 *            the minVipLevel to set
	 */
	public void setMinVipLevel(int minVipLevel) {
		this.minVipLevel = minVipLevel;
	}

	/**
	 * @return the minFriendPoint
	 */
	@Column(name = "min_friend_point", nullable = false)
	public int getMinFriendPoint() {
		return minFriendPoint;
	}

	/**
	 * @param minFriendPoint
	 *            the minFriendPoint to set
	 */
	public void setMinFriendPoint(int minFriendPoint) {
		this.minFriendPoint = minFriendPoint;
	}

	/**
	 * @return the totalNum
	 */
	@Column(name = "total_num", nullable = false)
	public int getTotalNum() {
		return totalNum;
	}

	/**
	 * @param totalNum
	 *            the totalNum to set
	 */
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	/**
	 * @return the redPacketNum
	 */
	@Column(name = "redpacket_num", nullable = false)
	public int getRedPacketNum() {
		return redPacketNum;
	}

	/**
	 * @return the receivedNum
	 */
	@Column(name = "received_num", nullable = false)
	public int getReceivedNum() {
		return receivedNum;
	}

	/**
	 * @param receivedNum
	 *            the receivedNum to set
	 */
	public void setReceivedNum(int receivedNum) {
		this.receivedNum = receivedNum;
	}

	/**
	 * @param redPacketNum
	 *            the redPacketNum to set
	 */
	public void setRedPacketNum(int redPacketNum) {
		this.redPacketNum = redPacketNum;
	}

	/**
	 * @return the msg
	 */
	@Column(name = "msg", nullable = true)
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the factionId
	 */
	@Column(name = "faction_id", nullable = true)
	public String getFactionId() {
		return factionId;
	}

	/**
	 * @param factionId
	 *            the factionId to set
	 */
	public void setFactionId(String factionId) {
		this.factionId = factionId;
	}

	/**
	 * @return the startTime
	 */
	@Column(name = "start_time", nullable = true)
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the lastRecvTime
	 */
	@Column(name = "last_recv_time", nullable = true)
	public Date getLastRecvTime() {
		return lastRecvTime;
	}

	/**
	 * @param lastRecvTime
	 *            the lastRecvTime to set
	 */
	public void setLastRecvTime(Date lastRecvTime) {
		this.lastRecvTime = lastRecvTime;
	}

	/**
	 * @return the finished
	 */
	@Column(name = "finished", nullable = false)
	public int getFinished() {
		return finished;
	}

	/**
	 * @param finished
	 *            the finished to set
	 */
	public void setFinished(int finished) {
		this.finished = finished;
	}

}

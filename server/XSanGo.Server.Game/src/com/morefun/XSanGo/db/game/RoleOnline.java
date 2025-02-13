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

/**
 * 角色的在线时长
 * 
 * @author lvmingtao
 *
 */
@Entity
@Table(name = "role_online")
public class RoleOnline implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7941195973642810744L;

	private String id;
	private Role role;
	private int onlineGiftId; // 在线礼包模板ID
	private Date starDate; // 领取奖励开始时间
	private int reqTime; // 领取奖励所需要的间隔秒数
	private int rewardNum; // 领取奖励的次数
	private String itemView; // 在线礼包随机得到物品

	/** default constructor */
	public RoleOnline() {

	}

	/** full constructor */
	public RoleOnline(String id, Role role, int onlineGiftId, Date starDate,
			int reqTime, int rewardNum, String itemView) {
		this.id = id;
		this.role = role;
		this.onlineGiftId = onlineGiftId;
		this.starDate = starDate;
		this.reqTime = reqTime;
		this.rewardNum = rewardNum;
		this.itemView = itemView;
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
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "online_id", nullable = false)
	public int getOnlineGiftId() {
		return onlineGiftId;
	}

	public void setOnlineGiftId(int onlineGiftId) {
		this.onlineGiftId = onlineGiftId;
	}

	@Column(name = "star_date", nullable = false)
	public Date getStarDate() {
		return starDate;
	}

	public void setStarDate(Date starDate) {
		this.starDate = starDate;
	}

	@Column(name = "req_time", nullable = false)
	public int getReqTime() {
		return reqTime;
	}

	public void setReqTime(int reqTime) {
		this.reqTime = reqTime;
	}

	@Column(name = "reward_num", nullable = false)
	public int getRewardNum() {
		return rewardNum;
	}

	public void setRewardNum(int rewardNum) {
		this.rewardNum = rewardNum;
	}

	@Column(name = "item_view")
	public String getItemView() {
		return itemView;
	}

	public void setItemView(String itemView) {
		this.itemView = itemView;
	}

}

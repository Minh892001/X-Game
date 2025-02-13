package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * 玩家公会信息
 * 
 * @author lixiongming
 * 
 */
@Entity
@Table(name = "role_faction")
public class RoleFaction implements Serializable {
	private static final long serialVersionUID = 2744591238943014546L;
	private String id;
	private Role role;
	private Date shopRefreshDate;// 商城物品刷新时间
	private String buyShopIds;// 已购买的商品ID,分割
	private int copyChallengeNum;// 公会副本已挑战次数
	private Date refreshChallengeDate;// 挑战次数刷新时间
	private int copyAwardNum;// 公会副本通关奖励已发送次数
	private int limitTimeAwardNum;// 公会副本限时通关奖励已发送次数
	private Date receiveAwardDate;// 公会副本通关奖励刷新时间

	private String canYuanbaoDonate;// 是否可元宝捐赠的科技ID,分割
	private int allotItemNum;// 被分配物品数据
	private Date refreshAllotDate;// 分配物品数量刷新时间
	private int donateWeizhang;// 总捐献微章数
	private int donateYuanbao;// 总捐献元宝数
	private int inviteNum;// 发送邀请次数
	private String awardFactionId;// 从哪个公会领取了副本奖励

	public RoleFaction() {

	}

	public RoleFaction(String id, Role role, Date shopRefreshDate, String buyShopIds, int copyAwardNum) {
		super();
		this.id = id;
		this.role = role;
		this.shopRefreshDate = shopRefreshDate;
		this.buyShopIds = buyShopIds;
		this.copyAwardNum = copyAwardNum;
	}

	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "shop_refresh_date")
	public Date getShopRefreshDate() {
		return shopRefreshDate;
	}

	public void setShopRefreshDate(Date shopRefreshDate) {
		this.shopRefreshDate = shopRefreshDate;
	}

	@Column(name = "buy_shop_ids")
	public String getBuyShopIds() {
		return buyShopIds;
	}

	public void setBuyShopIds(String buyShopIds) {
		this.buyShopIds = buyShopIds;
	}

	@Column(name = "copy_award_num", nullable = false)
	public int getCopyAwardNum() {
		return copyAwardNum;
	}

	public void setCopyAwardNum(int copyAwardNum) {
		this.copyAwardNum = copyAwardNum;
	}

	@Column(name = "limit_time_award_num", nullable = false)
	public int getLimitTimeAwardNum() {
		return limitTimeAwardNum;
	}

	public void setLimitTimeAwardNum(int limitTimeAwardNum) {
		this.limitTimeAwardNum = limitTimeAwardNum;
	}

	@Column(name = "copy_challenge_num", nullable = false)
	public int getCopyChallengeNum() {
		return copyChallengeNum;
	}

	public void setCopyChallengeNum(int copyChallengeNum) {
		this.copyChallengeNum = copyChallengeNum;
	}

	@Column(name = "refresh_challenge_date")
	public Date getRefreshChallengeDate() {
		return refreshChallengeDate;
	}

	public void setRefreshChallengeDate(Date refreshChallengeDate) {
		this.refreshChallengeDate = refreshChallengeDate;
	}

	@Column(name = "receive_award_date")
	public Date getReceiveAwardDate() {
		return receiveAwardDate;
	}

	public void setReceiveAwardDate(Date receiveAwardDate) {
		this.receiveAwardDate = receiveAwardDate;
	}

	@Column(name = "can_yuanbao_donate")
	public String getCanYuanbaoDonate() {
		return canYuanbaoDonate;
	}

	public void setCanYuanbaoDonate(String canYuanbaoDonate) {
		this.canYuanbaoDonate = canYuanbaoDonate;
	}

	@Column(name = "allot_item_num", nullable = false)
	public int getAllotItemNum() {
		return allotItemNum;
	}

	public void setAllotItemNum(int allotItemNum) {
		this.allotItemNum = allotItemNum;
	}

	@Column(name = "refresh_allot_date")
	public Date getRefreshAllotDate() {
		return refreshAllotDate;
	}

	public void setRefreshAllotDate(Date refreshAllotDate) {
		this.refreshAllotDate = refreshAllotDate;
	}

	@Column(name = "donate_weizhang", nullable = false)
	public int getDonateWeizhang() {
		return donateWeizhang;
	}

	public void setDonateWeizhang(int donateWeizhang) {
		this.donateWeizhang = donateWeizhang;
	}

	@Column(name = "donate_yuanbao", nullable = false)
	public int getDonateYuanbao() {
		return donateYuanbao;
	}

	public void setDonateYuanbao(int donateYuanbao) {
		this.donateYuanbao = donateYuanbao;
	}

	@Column(name = "invite_num")
	public int getInviteNum() {
		return inviteNum;
	}

	public void setInviteNum(int inviteNum) {
		this.inviteNum = inviteNum;
	}

	@Column(name = "award_faction_id")
	public String getAwardFactionId() {
		return awardFactionId;
	}

	public void setAwardFactionId(String awardFactionId) {
		this.awardFactionId = awardFactionId;
	}

}

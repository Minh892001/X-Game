/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: RoleDreamland
 * 功能描述：
 * 文件名：RoleDreamland.java
 **************************************************
 */
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
 * 南华幻境 数据结构
 * 
 * @author weiyi.zhao
 * @since 2016-4-21
 * @version 1.0
 */
@Entity
@Table(name = "role_dreamland")
public class RoleDreamland implements Serializable {
	private static final long serialVersionUID = -9054244032650131255L;

	/** 角色编号 */
	private String role_id;

	/** 角色对象 */
	private Role role;

	/** 最后挑战的关卡编号 */
	private int lastChallengeSceneId;

	/** 各关卡的状态明细 (关卡编号，星级) */
	private String scenePlan;

	/** 每日关卡进度 */
//	private String todayScenePlan;

	/** 挑战次数 */
	private int challengeNum;

	/** 购买次数 */
	private int buyNum;

	/** 南华令数 */
	private int nanHuaLing;

	/** 手动刷新次数 */
	private int shopRefreshNum;

	/** 商店明细 (唯一id，是否兑换) */
	private String shopItems;

	/** 星数奖励领取明细 (星数) */
	private String starAward;

	/** 总星级 */
	private int starNum;

	/** 通关层数 */
	private int layerNum;

	/** 关卡最后刷新时间 */
	private Date lastRefreshTime;

	/** 商店最后刷新时间 */
	private Date lastShopRefreshTime;

	/** 更新时间 */
	private Date updateTime;

	/**
	 * 构造函数
	 */
	public RoleDreamland() {
	}

	/**
	 * @return Returns the role_id.
	 */
	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "role"))
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "role_id", nullable = false)
	public String getRole_id() {
		return role_id;
	}

	/**
	 * @param role_id The role_id to set.
	 */
	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	/**
	 * @return Returns the role.
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Role getRole() {
		return role;
	}

	/**
	 * @param role The role to set.
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @return Returns the lastChallengeSceneId.
	 */
	@Column(name = "last_challenge_scene_id")
	public int getLastChallengeSceneId() {
		return lastChallengeSceneId;
	}

	/**
	 * @param lastChallengeSceneId The lastChallengeSceneId to set.
	 */
	public void setLastChallengeSceneId(int lastChallengeSceneId) {
		this.lastChallengeSceneId = lastChallengeSceneId;
	}

	/**
	 * @return Returns the scenePlan.
	 */
	@Column(name = "scene_plan")
	public String getScenePlan() {
		return scenePlan;
	}

	/**
	 * @param scenePlan The scenePlan to set.
	 */
	public void setScenePlan(String scenePlan) {
		this.scenePlan = scenePlan;
	}

	// /**
	// * @return Returns the todayScenePlan.
	// */
	// @Column(name = "today_scene_plan")
	// public String getTodayScenePlan() {
	// return todayScenePlan;
	// }
	//
	// /**
	// * @param todayScenePlan The todayScenePlan to set.
	// */
	// public void setTodayScenePlan(String todayScenePlan) {
	// this.todayScenePlan = todayScenePlan;
	// }

	/**
	 * @return Returns the challengeNum.
	 */
	@Column(name = "challenge_num")
	public int getChallengeNum() {
		return challengeNum;
	}

	/**
	 * @param challengeNum The challengeNum to set.
	 */
	public void setChallengeNum(int challengeNum) {
		this.challengeNum = challengeNum;
	}

	/**
	 * @return Returns the buyNum.
	 */
	@Column(name = "buy_num")
	public int getBuyNum() {
		return buyNum;
	}

	/**
	 * @param buyNum The buyNum to set.
	 */
	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

	/**
	 * @return Returns the nanHuaLing.
	 */
	@Column(name = "nan_hua")
	public int getNanHuaLing() {
		return nanHuaLing;
	}

	/**
	 * @param nanHuaLing The nanHuaLing to set.
	 */
	public void setNanHuaLing(int nanHuaLing) {
		this.nanHuaLing = nanHuaLing;
	}

	/**
	 * @return Returns the shopRefreshNum.
	 */
	@Column(name = "shop_refresh_num")
	public int getShopRefreshNum() {
		return shopRefreshNum;
	}

	/**
	 * @param shopRefreshNum The shopRefreshNum to set.
	 */
	public void setShopRefreshNum(int shopRefreshNum) {
		this.shopRefreshNum = shopRefreshNum;
	}

	/**
	 * @return Returns the shopItems.
	 */
	@Column(name = "shop_items")
	public String getShopItems() {
		return shopItems;
	}

	/**
	 * @param shopItems The shopItems to set.
	 */
	public void setShopItems(String shopItems) {
		this.shopItems = shopItems;
	}

	/**
	 * @return Returns the starAward.
	 */
	@Column(name = "star_award")
	public String getStarAward() {
		return starAward;
	}

	/**
	 * @param starAward The starAward to set.
	 */
	public void setStarAward(String starAward) {
		this.starAward = starAward;
	}

	/**
	 * @return Returns the starNum.
	 */
	@Column(name = "star_num")
	public int getStarNum() {
		return starNum;
	}

	/**
	 * @param starNum The starNum to set.
	 */
	public void setStarNum(int starNum) {
		this.starNum = starNum;
	}

	/**
	 * @return Returns the layerNum.
	 */
	@Column(name = "layer_num")
	public int getLayerNum() {
		return layerNum;
	}

	/**
	 * @param layerNum The layerNum to set.
	 */
	public void setLayerNum(int layerNum) {
		this.layerNum = layerNum;
	}

	/**
	 * @return Returns the lastRefreshTime.
	 */
	@Column(name = "last_refresh_time", nullable = true, length = 19)
	public Date getLastRefreshTime() {
		return lastRefreshTime;
	}

	/**
	 * @param lastRefreshTime The lastRefreshTime to set.
	 */
	public void setLastRefreshTime(Date lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	/**
	 * @return Returns the lastShopRefreshTime.
	 */
	@Column(name = "last_shop_refresh_time", nullable = true, length = 19)
	public Date getLastShopRefreshTime() {
		return lastShopRefreshTime;
	}

	/**
	 * @param lastShopRefreshTime The lastShopRefreshTime to set.
	 */
	public void setLastShopRefreshTime(Date lastShopRefreshTime) {
		this.lastShopRefreshTime = lastShopRefreshTime;
	}

	/**
	 * @return Returns the updateTime.
	 */
	@Column(name = "update_time", nullable = true, length = 19)
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime The updateTime to set.
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}

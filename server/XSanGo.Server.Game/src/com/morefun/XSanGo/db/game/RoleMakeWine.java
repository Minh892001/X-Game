/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 煮酒论英雄 数据库结构
 * 
 * @author zhuzhi.yang
 * 
 */
@Entity
@Table(name = "role_makewine")
public class RoleMakeWine implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String roleId;
	private String name;
	private int vip;
	private int level;
	private String headImg;
	private int composeScore;			// 酿酒积分
	private int shareScore;				// 分享积分
	private int exchangeUsedScore;		// 兑换用掉的积分
	private Date receiveMaterialDate; 	//领取材料的日期
	private int receiveSocre; // 领取的积分档次
	private String itemComposedCount; // 已经合成的次数
	private Date receiveShareDate;		// 领取别人分享酒的时间
	private int topedTimes;			// 每日已置顶的次数

	private Date resetDate; // 最后重置日期
	
	public RoleMakeWine(){
		
	}
	
	public RoleMakeWine(String roleId, String name, int vip, int level, String headImg){
		this.roleId = roleId;
		this.name = name;
		this.vip = vip;
		this.level = level;
		this.headImg = headImg;
	}

	@Id
	@Column(name = "role_id", nullable = false)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	@Column(name = "name", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "vip", nullable = false)
	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	@Column(name = "level", nullable = false)
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Column(name = "head_img", nullable = true)
	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	@Column(name = "compose_score", nullable = false)
	public int getComposeScore() {
		return composeScore;
	}

	public void setComposeScore(int composeScore) {
		this.composeScore = composeScore;
	}
	
	@Column(name = "share_score", nullable = false)
	public int getShareScore() {
		return shareScore;
	}

	public void setShareScore(int shareScore) {
		this.shareScore = shareScore;
	}

	@Column(name = "exchange_used_socre", nullable = false)
	public int getExchangeUsedScore() {
		return exchangeUsedScore;
	}

	public void setExchangeUsedScore(int exchangeUsedScore) {
		this.exchangeUsedScore = exchangeUsedScore;
	}

	@Column(name = "receive_material_date", nullable = true)
	public Date getReceiveMaterialDate() {
		return receiveMaterialDate;
	}

	public void setReceiveMaterialDate(Date receiveMaterialDate) {
		this.receiveMaterialDate = receiveMaterialDate;
	}

	@Column(name = "receive_socre", nullable = false)
	public int getReceiveSocre() {
		return receiveSocre;
	}

	public void setReceiveSocre(int receiveSocre) {
		this.receiveSocre = receiveSocre;
	}

	@Column(name = "item_composed_count", nullable = false)
	public String getItemComposedCount() {
		return itemComposedCount;
	}

	public void setItemComposedCount(String itemComposedCount) {
		this.itemComposedCount = itemComposedCount;
	}
	
	@Column(name = "receive_share_date", nullable = true)
	public Date getReceiveShareDate() {
		return receiveShareDate;
	}

	public void setReceiveShareDate(Date receiveShareDate) {
		this.receiveShareDate = receiveShareDate;
	}
	
	@Column(name = "toped_times", nullable = false)
	public int getTopedTimes() {
		return topedTimes;
	}

	public void setTopedTimes(int topedTimes) {
		this.topedTimes = topedTimes;
	}

	@Column(name = "reset_date", nullable = true)
	public Date getResetDate() {
		return resetDate;
	}

	public void setResetDate(Date resetDate) {
		this.resetDate = resetDate;
	}
}

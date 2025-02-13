/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: DreamlandRank
 * 功能描述：
 * 文件名：DreamlandRank.java
 **************************************************
 */
package com.morefun.XSanGo.dreamland;

import java.util.Date;

import com.XSanGo.Protocol.DreamlandRankUnit;
import com.morefun.XSanGo.role.IRole;

/**
 * 南华幻境排行数据
 * 
 * @author weiyi.zhao
 * @since 2016-4-23
 * @version 1.0
 */
public class DreamlandRank {

	/** 排行单元对象 */
	private DreamlandRankUnit rankUnit;

	/** 排行时间 */
	private Date rankUpdateTime;

	/**
	 * @return Returns the rankUnit.
	 */
	public DreamlandRankUnit getRankUnit() {
		return rankUnit;
	}

	/**
	 * 初始化排行单元
	 * 
	 * @param role
	 */
	public void setRankUnit(IRole role) {
		if (this.rankUnit == null) {
			this.rankUnit = new DreamlandRankUnit();
		}
		this.rankUnit.roleId = role.getRoleId();
		this.rankUnit.roleName = role.getName();
		this.rankUnit.headIcon = role.getHeadImage();
		this.rankUnit.vipLevel = role.getVipLevel();
		this.rankUnit.roleLevel = role.getLevel();
	}

	/**
	 * 初始化排行单元
	 * 
	 * @param roleId
	 * @param roleName
	 * @param headIcon
	 * @param roleLevel
	 * @param vipLevel
	 * @param starNum
	 * @param layerNum
	 */
	public void setRankUnit(String roleId, String roleName, String headIcon, int roleLevel, int vipLevel, int starNum, int layerNum) {
		this.rankUnit = new DreamlandRankUnit();
		this.rankUnit.roleId = roleId;
		this.rankUnit.roleName = roleName;
		this.rankUnit.headIcon = headIcon;
		this.rankUnit.roleLevel = roleLevel;
		this.rankUnit.vipLevel = vipLevel;
		this.rankUnit.starNum = starNum;
		this.rankUnit.layerNum = layerNum;
	}

	public void setRank(int rank) {
		this.rankUnit.rank = rank;
	}

	public int getRank() {
		return this.rankUnit.rank;
	}

	public void setStarNum(int starNum) {
		this.rankUnit.starNum = starNum;
	}

	public int getStarNum() {
		return this.rankUnit.starNum;
	}

	public void setLayerNum(int layerNum) {
		this.rankUnit.layerNum = layerNum;
	}

	public int getLayerNum() {
		return this.rankUnit.layerNum;
	}

	public String getRoleId() {
		return rankUnit.roleId;
	}

	/**
	 * @return Returns the rankUpdateTime.
	 */
	public Date getRankUpdateTime() {
		return rankUpdateTime;
	}

	/**
	 * @param rankUpdateTime The rankUpdateTime to set.
	 */
	public void setRankUpdateTime(Date rankUpdateTime) {
		this.rankUpdateTime = rankUpdateTime;
	}
}

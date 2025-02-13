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
 * 北伐数据
 * 
 * @author qinguofeng
 * @date Feb 4, 2015
 */
@Entity
@Table(name = "role_attack_castle")
public class RoleAttackCastle implements Serializable {

	private static final long serialVersionUID = -3394828225612089683L;

	private String roleId;
	private Role role;

	private int currentNodeId; // 当前关卡ID
	private int attackCastleCoinCount; // 竞技币数量
	private int resetNodeCount; // 今日重置关卡次数
	private int refreshShopCount; // 今日刷新商城次数
	private Date lastResetShopDate; // 上次重置商城时间
	private Date lastResetNodeDate; // 上次重置关卡时间
	private String extraJsonStr; // InnerDBWarper 的json字符串

	public RoleAttackCastle() {

	}

	public RoleAttackCastle(Role r) {
		this.role = r;
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
	 * @param id
	 *            the id to set
	 */
	public void setRoleId(String id) {
		this.roleId = id;
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
	 * @return the currentNodeId
	 */
	@Column(name = "current_node_id", nullable = false, columnDefinition = "int default 0")
	public int getCurrentNodeId() {
		return currentNodeId;
	}

	/**
	 * @param currentNodeId
	 *            the currentNodeId to set
	 */
	public void setCurrentNodeId(int currentNodeId) {
		this.currentNodeId = currentNodeId;
	}

	/**
	 * @return the cttackCastleCoinCount
	 */
	@Column(name = "coin_count", nullable = false, columnDefinition = "int default 0")
	public int getAttackCastleCoinCount() {
		return attackCastleCoinCount;
	}

	/**
	 * @param attackCastleCoinCount
	 *            the attackCastleCoinCount to set
	 */
	public void setAttackCastleCoinCount(int attackCastleCoinCount) {
		this.attackCastleCoinCount = attackCastleCoinCount;
	}

	/**
	 * @return the resetNodeCount
	 */
	@Column(name = "reset_node_count", nullable = false, columnDefinition = "int default 0")
	public int getResetNodeCount() {
		return resetNodeCount;
	}

	/**
	 * @param resetNodeCount
	 *            the resetNodeCount to set
	 */
	public void setResetNodeCount(int resetNodeCount) {
		this.resetNodeCount = resetNodeCount;
	}

	/**
	 * @return the refreshShopCount
	 */
	@Column(name = "refresh_shop_count", nullable = false, columnDefinition = "int default 0")
	public int getRefreshShopCount() {
		return refreshShopCount;
	}

	/**
	 * @param refreshShopCount
	 *            the refreshShopCount to set
	 */
	public void setRefreshShopCount(int refreshShopCount) {
		this.refreshShopCount = refreshShopCount;
	}

	/**
	 * @return the lastResetShopDate
	 */
	@Column(name = "last_reset_shop_date", nullable = false)
	public Date getLastResetShopDate() {
		return lastResetShopDate;
	}

	/**
	 * @param lastResetShopDate
	 *            the lastResetShopDate to set
	 */
	public void setLastResetShopDate(Date lastResetShopDate) {
		this.lastResetShopDate = lastResetShopDate;
	}

	/**
	 * @return the lastResetNodeDate
	 */
	@Column(name = "last_reset_node_date", nullable = true)
	public Date getLastResetNodeDate() {
		return lastResetNodeDate;
	}

	/**
	 * @param lastResetNodeDate
	 *            the lastResetNodeDate to set
	 */
	public void setLastResetNodeDate(Date lastResetNodeDate) {
		this.lastResetNodeDate = lastResetNodeDate;
	}

	/**
	 * @return the extraJsonStr
	 */
	@Column(name = "extra_json", nullable = false, columnDefinition = "text")
	public String getExtraJsonStr() {
		return extraJsonStr;
	}

	/**
	 * @param extraJsonStr
	 *            the extraJsonStr to set
	 */
	public void setExtraJsonStr(String extraJsonStr) {
		this.extraJsonStr = extraJsonStr;
	}

}

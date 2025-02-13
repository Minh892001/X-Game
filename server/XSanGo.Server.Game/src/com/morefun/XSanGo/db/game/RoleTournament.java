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
 * 比武大会
 * 
 * @author guofeng.qin
 */
@Entity
@Table(name = "role_tournament")
public class RoleTournament implements Serializable {

	private static final long serialVersionUID = -1173414158004319385L;

	private String roleId;
	private Role role; // 关联role对象

	private int num; // 第几届比武大会
	private int hasSignup; // 是否已经报名
	private Date startDate; // 赛季开始日期
	private int buyRefreshCount; // 购买刷新次数
	private int refreshCount; // 刷新次数
	private int buyFightCount; // 购买挑战次数
	private int fightCount; // 挑战次数
	private String myFormation; // 我的布阵
	private int maxHistoryRank; // 历史最高排名
	private int lastChampionNum; // 上次冠军届数（最近的一次冠军是第几届)
	private Date lastResetDate; // 上次重置日期
	private Date updateDate; // 上次更新日期
	private int winNum;// 当日胜利次数
	private String shopBuyCount; // 商城购买次数
	private int coin; // 至尊币数量
	private int ybcoin; // 至尊银币数量

	public RoleTournament() {
		super();
	}

	public RoleTournament(String roleId, Role role, int num, int hasSignup, Date startDate, int refreshCount,
			int fightCount, String myFormation, int maxRank, int lastChampionNum, Date lastResetDate, Date updateDate, String shopBuyCount) {
		super();
		this.roleId = roleId;
		this.role = role;
		this.num = num;
		this.hasSignup = hasSignup;
		this.startDate = startDate;
		this.refreshCount = refreshCount;
		this.fightCount = fightCount;
		this.myFormation = myFormation;
		this.maxHistoryRank = maxRank;
		this.lastChampionNum = lastChampionNum;
		this.lastResetDate = lastResetDate;
		this.updateDate = updateDate;
		this.shopBuyCount = shopBuyCount;
		this.coin = 0;
		this.ybcoin = 0;
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
	 * @return the hasSignup
	 */
	@Column(name = "has_signup", nullable = false)
	public int getHasSignup() {
		return hasSignup;
	}

	/**
	 * @param hasSignup
	 *            the hasSignup to set
	 */
	public void setHasSignup(int hasSignup) {
		this.hasSignup = hasSignup;
	}

	/**
	 * @return the startDate
	 */
	@Column(name = "start_date", nullable = true)
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the lastResetDate
	 */
	@Column(name = "last_reset_date", nullable = true)
	public Date getLastResetDate() {
		return lastResetDate;
	}

	/**
	 * @param lastResetDate
	 *            the lastResetDate to set
	 */
	public void setLastResetDate(Date lastResetDate) {
		this.lastResetDate = lastResetDate;
	}

	/**
	 * @return the updateDate
	 */
	@Column(name = "update_date", nullable = false)
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate
	 *            the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the refreshCount
	 */
	@Column(name = "refresh_count", nullable = false)
	public int getRefreshCount() {
		return refreshCount;
	}

	/**
	 * @param refreshCount
	 *            the refreshCount to set
	 */
	public void setRefreshCount(int refreshCount) {
		this.refreshCount = refreshCount;
	}

	/**
	 * @return the fightCount
	 */
	@Column(name = "fight_count", nullable = false)
	public int getFightCount() {
		return fightCount;
	}

	/**
	 * @param fightCount
	 *            the fightCount to set
	 */
	public void setFightCount(int fightCount) {
		this.fightCount = fightCount;
	}

	/**
	 * @return the myFormation
	 */
	@Column(name = "formation", nullable = false)
	public String getMyFormation() {
		return myFormation;
	}

	/**
	 * @param myFormation
	 *            the myFormation to set
	 */
	public void setMyFormation(String myFormation) {
		this.myFormation = myFormation;
	}

	/**
	 * @return the buyRefreshCount
	 */
	@Column(name = "buy_refresh_count", nullable = false)
	public int getBuyRefreshCount() {
		return buyRefreshCount;
	}

	/**
	 * @param buyRefreshCount
	 *            the buyRefreshCount to set
	 */
	public void setBuyRefreshCount(int buyRefreshCount) {
		this.buyRefreshCount = buyRefreshCount;
	}

	/**
	 * @return the buyFightCount
	 */
	@Column(name = "buy_fight_count", nullable = false)
	public int getBuyFightCount() {
		return buyFightCount;
	}

	/**
	 * @return the maxHistoryRank
	 */
	@Column(name = "max_rank", nullable = false)
	public int getMaxHistoryRank() {
		return maxHistoryRank;
	}

	/**
	 * @param maxHistoryRank
	 *            the maxHistoryRank to set
	 */
	public void setMaxHistoryRank(int maxHistoryRank) {
		this.maxHistoryRank = maxHistoryRank;
	}

	/**
	 * @param buyFightCount
	 *            the buyFightCount to set
	 */
	public void setBuyFightCount(int buyFightCount) {
		this.buyFightCount = buyFightCount;
	}

	/**
	 * @return the lastChampionNum
	 */
	@Column(name = "last_champion_num", nullable = false)
	public int getLastChampionNum() {
		return lastChampionNum;
	}

	/**
	 * @param lastChampionNum
	 *            the lastChampionNum to set
	 */
	public void setLastChampionNum(int lastChampionNum) {
		this.lastChampionNum = lastChampionNum;
	}

	@Column(name = "win_num", nullable = false)
	public int getWinNum() {
		return winNum;
	}

	public void setWinNum(int winNum) {
		this.winNum = winNum;
	}

	/**
	 * @return the shopBuyCount
	 */
	@Column(name = "shop_buy_count", nullable = true)
	public String getShopBuyCount() {
		return shopBuyCount;
	}

	/**
	 * @param shopBuyCount the shopBuyCount to set
	 */
	public void setShopBuyCount(String shopBuyCount) {
		this.shopBuyCount = shopBuyCount;
	}

	/**
	 * @return the coin
	 */
	@Column(name = "coin", nullable = false)
	public int getCoin() {
		return coin;
	}

	/**
	 * @param coin the coin to set
	 */
	public void setCoin(int coin) {
		this.coin = coin;
	}

	/**
	 * @return the ybcoin
	 */
	@Column(name = "ybcoin", nullable = false)
	public int getYbcoin() {
		return ybcoin;
	}

	/**
	 * @param ybcoin the ybcoin to set
	 */
	public void setYbcoin(int ybcoin) {
		this.ybcoin = ybcoin;
	}

}

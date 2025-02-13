package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "role_vip")
public class RoleVip implements Serializable {
	
	private static final long serialVersionUID = -1375017799727604914L;
	private String roleId;
	private Role role;
	/** vip等级 */
	private int vipLevel;
	/** 充值元宝，非绑定 */
	private int unbindYuanbao;
	/** 赠送元宝，绑定 */
	private int bindYuanbao;
	/** 当前vip经验 */
	private int vipExperience;

	/** 月卡到期时间 */
	private Date monthExpireTime;
	/** 金币 */
	private long jinbi;
	/** 累计获得金币 */
	private long jinbiHistory;
	/** 上次刷新vip商城时间 */
	private Date lastRefreshingVipTraderTime;
	/** 累计充值金额 */
	private long chargeHistory;

	private Set<RoleVipTraderItem> vipTraderItems = new HashSet<RoleVipTraderItem>(
			0);
	private Map<Integer, RoleVipGiftPacks> vipGiftPacks = new HashMap<Integer, RoleVipGiftPacks>(
			0);
	/** 充值记录 */
	private Set<RoleCharge> roleCharge = new HashSet<RoleCharge>();

	public RoleVip() {
	}

	public RoleVip(Role role) {
		this.role = role;
	}

	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "role"))
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "role_id", nullable = false)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "unbind_yuanbao", nullable = false)
	public int getUnbindYuanbao() {
		return unbindYuanbao;
	}

	public void setUnbindYuanbao(int unbindYuanbao) {
		this.unbindYuanbao = unbindYuanbao;
	}

	@Column(name = "bind_yuanbao", nullable = false)
	public int getBindYuanbao() {
		return bindYuanbao;
	}

	public void setBindYuanbao(int bindYuanbao) {
		this.bindYuanbao = bindYuanbao;
	}

	@Column(name = "jinbi", nullable = false)
	public long getJinbi() {
		return jinbi;
	}

	public void setJinbi(long jinbi) {
		this.jinbi = jinbi;
	}

	@Column(name = "jinbi_history", nullable = false, columnDefinition = "bigint default 0")
	public long getJinbiHistory() {
		return jinbiHistory;
	}

	public void setJinbiHistory(long jinbiHistory) {
		this.jinbiHistory = jinbiHistory;
	}
	
	@Column(name = "charge_history", nullable = false, columnDefinition = "bigint default 0")
	public long getChargeHistory () {
		return chargeHistory;
	}
	
	public void setChargeHistory (long chargeHistroy) {
		this.chargeHistory = chargeHistroy;
	}

	@Column(name = "last_refreshing_vip_trader_time")
	public Date getLastRefreshingVipTraderTime() {
		return lastRefreshingVipTraderTime;
	}

	public void setLastRefreshingVipTraderTime(Date lastRefreshingVipTraderTime) {
		this.lastRefreshingVipTraderTime = lastRefreshingVipTraderTime;
	}

	@Column(name = "vip_experience", nullable = false, columnDefinition = "int default 0")
	public int getVipExperience() {
		return vipExperience;
	}

	public void setVipExperience(int vipExperience) {
		this.vipExperience = vipExperience;
	}

	@Column(name = "vip_level", nullable = false, columnDefinition = "int default 0")
	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "vip")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public Set<RoleVipTraderItem> getVipTraderItems() {
		return vipTraderItems;
	}

	public void setVipTraderItems(Set<RoleVipTraderItem> vipTraderItems) {
		this.vipTraderItems = vipTraderItems;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "vip")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	@MapKey(name = "vipLevel")
	public Map<Integer, RoleVipGiftPacks> getVipGiftPacks() {
		return vipGiftPacks;
	}

	public void setVipGiftPacks(Map<Integer, RoleVipGiftPacks> vipGiftPacks) {
		this.vipGiftPacks = vipGiftPacks;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "vip")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(FetchMode.SELECT)
	public Set<RoleCharge> getRoleCharge() {
		return roleCharge;
	}

	public void setRoleCharge(Set<RoleCharge> roleCharge) {
		this.roleCharge = roleCharge;
	}

	@Column(name = "month_expire_time", nullable = true)
	public Date getMonthExpireTime() {
		return monthExpireTime;
	}

	public void setMonthExpireTime(Date monthExpireTime) {
		this.monthExpireTime = monthExpireTime;
	}
}

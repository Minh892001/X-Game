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
 * 铁匠铺角色相关 数据库结构
 * 暂时仅涉及蓝装商城兑换数据
 * 
 */
@Entity
@Table(name = "role_blue_smithy")
public class RoleBlueSmithy implements Serializable {

	private static final long serialVersionUID = -4989445413015431277L;

	private String roleId;;
	private Role role;
	private String exchangeItemStr; // 挑战兑换商城 道具
	private Date exchangeRefreshDate; // 商城 刷新时间
	private int exchangeRefreshNum; // 商城 刷新次数
	
	public RoleBlueSmithy() {}

	public RoleBlueSmithy(String roleId, Role role, String exchangeItemStr, Date exchangeRefreshDate,
			int exchangeRefreshNum) {
		this.roleId = roleId;
		this.role = role;
		this.exchangeItemStr = exchangeItemStr;
		this.exchangeRefreshDate = exchangeRefreshDate;
		this.exchangeRefreshNum = exchangeRefreshNum;
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

	@Column(name = "exchange_item_str", length = 5000)
	public String getExchangeItemStr() {
		return exchangeItemStr;
	}

	public void setExchangeItemStr(String exchangeItemStr) {
		this.exchangeItemStr = exchangeItemStr;
	}

	@Column(name = "exchange_refresh_date")
	public Date getExchangeRefreshDate() {
		return exchangeRefreshDate;
	}

	public void setExchangeRefreshDate(Date exchangeRefreshDate) {
		this.exchangeRefreshDate = exchangeRefreshDate;
	}

	@Column(name = "exchange_refresh_num", columnDefinition = "INT default 0")
	public int getExchangeRefreshNum() {
		return exchangeRefreshNum;
	}

	public void setExchangeRefreshNum(int exchangeRefreshNum) {
		this.exchangeRefreshNum = exchangeRefreshNum;
	}

	
	

}

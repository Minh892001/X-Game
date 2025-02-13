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
 * 兑换物品已换取次数
 */
@Entity
@Table(name = "role_exchange_item")
public class RoleExchangeItem  implements Serializable {

	private static final long serialVersionUID = -2009918631237757232L;

	private String id;
	private Role role;
	private String exchangNo;
	private int exchangeCounts;// 已兑换次数
	private boolean exchangeOut;// 兑换后是否消失
	private Date lastExchangeTime;//上次兑换时间
	
	
	public RoleExchangeItem() {}

	public RoleExchangeItem(String id, Role role, String exchangNo, int exchangeCounts, boolean exchangeOut,
			Date lastExchangeTime) {
		super();
		this.id = id;
		this.role = role;
		this.exchangNo = exchangNo;
		this.exchangeCounts = exchangeCounts;
		this.exchangeOut = exchangeOut;
		this.lastExchangeTime = lastExchangeTime;
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
	@Column(name = "exchange_no", nullable = false)
	public String getExchangNo() {
		return exchangNo;
	}

	public void setExchangNo(String exchangNo) {
		this.exchangNo = exchangNo;
	}
	@Column(name = "exchange_counts", nullable = false)
	public int getExchangeCounts() {
		return exchangeCounts;
	}

	public void setExchangeCounts(int exchangeCounts) {
		this.exchangeCounts = exchangeCounts;
	}
	
	@Column(name = "exchange_out", nullable = false, columnDefinition = "bit default 0")
	public boolean isExchangeOut() {
		return exchangeOut;
	}

	public void setExchangeOut(boolean useOut) {
		this.exchangeOut = useOut;
	}
	
	@Column(name = "last_exchange_item_date")
	public Date getLastExchangeTime() {
		return lastExchangeTime;
	}

	public void setLastExchangeTime(Date lastExchangeTime) {
		this.lastExchangeTime = lastExchangeTime;
	}
	
	
}

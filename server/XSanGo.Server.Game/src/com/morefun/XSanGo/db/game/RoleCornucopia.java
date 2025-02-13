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
 * 聚宝盆
 * 
 * @author lixiongming
 *
 */
@Entity
@Table(name = "role_cornucopia")
public class RoleCornucopia implements Serializable{
	private static final long serialVersionUID = -2723193984636027777L;
	private String id;
	private Role role;
	private int scriptId;
	private Date buyDate;// 购买日期
	private Date lastReceiveDate;// 最后领取时间

	public RoleCornucopia() {

	}

	public RoleCornucopia(String id, Role role, int scriptId, Date buyDate,
			Date lastReceiveDate) {
		super();
		this.id = id;
		this.role = role;
		this.scriptId = scriptId;
		this.buyDate = buyDate;
		this.lastReceiveDate = lastReceiveDate;
	}

	@Id
	@Column(name = "id", nullable = false, length = 64)
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

	@Column(name = "script_id", nullable = false)
	public int getScriptId() {
		return scriptId;
	}

	public void setScriptId(int scriptId) {
		this.scriptId = scriptId;
	}

	@Column(name = "buy_date")
	public Date getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(Date buyDate) {
		this.buyDate = buyDate;
	}

	@Column(name = "last_receive_date")
	public Date getLastReceiveDate() {
		return lastReceiveDate;
	}

	public void setLastReceiveDate(Date lastReceiveDate) {
		this.lastReceiveDate = lastReceiveDate;
	}

}

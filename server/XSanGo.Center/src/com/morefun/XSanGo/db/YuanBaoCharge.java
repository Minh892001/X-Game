/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.db;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 元宝流水记录
 * 
 * @author BruceSu
 * 
 */
@Entity
@Table(name = "yuan_bao_charge")
public class YuanBaoCharge implements Serializable {
	private int id;
	private Account account;
	private int yuanBao;
	private Date createTime;
	private String remark;

	// private String accountName;

	public YuanBaoCharge() {

	}

	public YuanBaoCharge(String account, int yuanBao, Date createTime,
			String remark) {
		this.account = new Account();
		this.account.setAccount(account);
		this.yuanBao = yuanBao;
		this.createTime = createTime;
		this.remark = remark;
	}

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account", nullable = false)
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Column(name = "yuan_bao", nullable = false)
	public int getYuanBao() {
		return yuanBao;
	}

	public void setYuanBao(int yuanBao) {
		this.yuanBao = yuanBao;
	}

	@Column(name = "create_time", nullable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "remark", nullable = false)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	// @Column(name = "account", nullable = false)
	// public String getAccountName() {
	// return accountName;
	// }
	//
	// public void setAccountName(String accountName) {
	// this.accountName = accountName;
	// }
}

/**
 * 
 */
package com.morefun.XSanGo.db.stat;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author sulingyun
 *
 */
@Entity
@Table(name = "account_operate_log")
public class AccountOperateLog implements Serializable {

	private long id;
	private String account;
	private Date createTime;
	private String remark;

	public AccountOperateLog(String account, Date createTime, String remark) {
		this.account = account;
		this.createTime = createTime;
		this.remark = remark;
	}

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "account", nullable = false)
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "create_time", nullable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}

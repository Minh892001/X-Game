/**
 * 
 */
package com.morefun.XSanGo.db;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 帐号激活码
 * 
 * @author sulingyun
 *
 */
@Entity
@Table(name = "active_code")
public class ActiveCode implements Serializable {
	private String code;
	private String account;
	private Date useTime;

	public ActiveCode() {

	}

	public ActiveCode(String code) {
		this.code = code;
	}

	@Id
	@Column(name = "code", nullable = false, length = 32)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "account", nullable = true, length = 32)
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "use_time", nullable = true)
	public Date getUseTime() {
		return useTime;
	}

	public void setUseTime(Date useTime) {
		this.useTime = useTime;
	}
}

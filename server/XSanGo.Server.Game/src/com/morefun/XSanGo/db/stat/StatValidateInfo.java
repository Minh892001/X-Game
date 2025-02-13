package com.morefun.XSanGo.db.stat;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 非法战报验证结果
 * 
 * @author qinguofeng
 */
@Entity
@Table(name = "role_validate_info")
public class StatValidateInfo implements Serializable {

	private static final long serialVersionUID = 3270599794201478953L;

	private int id;
	private String account;
	private String roleId;
	private int serverId;
	private int type;
	private String tag;
	private Date updateDate;

	public StatValidateInfo() {
	}

	public StatValidateInfo(String account, String roleId, int serverId, int type, String tag, Date updateDate) {
		this.account = account;
		this.roleId = roleId;
		this.serverId = serverId;
		this.type = type;
		this.tag = tag;
		this.updateDate = updateDate;
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the account
	 */
	@Column(name = "account", nullable = false)
	public String getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @return the roleId
	 */
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
	 * @return the serverId
	 */
	@Column(name = "server_id", nullable = false)
	public int getServerId() {
		return serverId;
	}

	/**
	 * @param serverId
	 *            the serverId to set
	 */
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	/**
	 * @return the type
	 */
	@Column(name = "type", nullable = false)
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the tag
	 */
	@Column(name = "tag", nullable = true)
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag
	 *            the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
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

}

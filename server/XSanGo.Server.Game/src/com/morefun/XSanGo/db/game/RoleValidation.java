package com.morefun.XSanGo.db.game;

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
 * 玩家非法状态表
 * 
 * @author qinguofeng
 */
@Entity
@Table(name = "role_validation")
public class RoleValidation implements Serializable {

	private static final long serialVersionUID = 3237361089650147011L;

	private int id;
	private String roleId;
	private Role role;
	private int type; // 类型
	private String reason; // 描述
	private String tag; // 附加标记
	private String referId; // 关联ID
	private Date updateDate;

	public RoleValidation() {

	}

	public RoleValidation(String roleId, int type, String reason, String tag,
			Date date) {
		this.roleId = roleId;
		this.type = type;
		this.reason = reason;
		this.tag = tag;
		this.updateDate = date;
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
	 * @return the role
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false, insertable = false, updatable = false)
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
	 * @return the type
	 */
	@Column(name = "type", nullable = false)
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the reason
	 */
	@Column(name = "reason", nullable = true)
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason
	 *            the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
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
	 * @return the referId
	 */
	@Column(name = "refer_id", nullable = true)
	public String getReferId() {
		return referId;
	}

	/**
	 * @param referId
	 *            the referId to set
	 */
	public void setReferId(String referId) {
		this.referId = referId;
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

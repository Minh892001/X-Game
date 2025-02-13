/**
 * 
 */
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
 * @author BruceSu
 * 
 */
@Entity
@Table(name = "role_cdk_record")
public class RoleCDKRecord implements Serializable {

	private static final long serialVersionUID = -202216375265374318L;
	private String id;
	private Role role;
	private String category;
	private String cdk;
	private Date createTime;

	public RoleCDKRecord() {

	}

	public RoleCDKRecord(String id, Role role, String category, String cdk, Date createTime) {
		this.id = id;
		this.category = category;
		this.role = role;
		this.cdk = cdk;
		this.createTime = createTime;
	}

	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id", nullable = false)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "cdk", nullable = false)
	public String getCdk() {
		return cdk;
	}

	public void setCdk(String cdk) {
		this.cdk = cdk;
	}

	@Column(name = "create_time", nullable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "category", nullable = false)
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
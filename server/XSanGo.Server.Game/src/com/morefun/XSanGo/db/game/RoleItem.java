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
 * 物品表
 * 
 * @author sulingyun
 * 
 */
@Entity
@Table(name = "role_item")
public class RoleItem implements Serializable {

	// /** 默认状态，在背包中 */
	// public static final byte State_Package = 0;
	// /** 物品被装备中 */
	// public static final byte State_Equip = 1;
	// /** 物品被删除 */
	// public static final byte State_Delete = 2;
	// Fields

	
	private static final long serialVersionUID = -2991395551601283054L;
	private String id;
	private Role role;
	private String templateCode;
	private int num;
	private String attachData;
	private Date deleteTime;
	
	// Constructors

	/** default constructor */
	public RoleItem() {
	}

	/** full constructor */
	public RoleItem(String id, Role role, String templateCode, int num) {
		this.id = id;
		this.role = role;
		this.templateCode = templateCode;
		this.num = num;
	}

	// Property accessors
	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "template_id", nullable = false)
	public String getTemplateCode() {
		return this.templateCode;
	}

	public void setTemplateCode(String templateId) {
		this.templateCode = templateId;
	}

	@Column(name = "num", nullable = false)
	public int getNum() {
		return this.num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	@Column(name = "attach_data", nullable = true)
	public String getAttachData() {
		return attachData;
	}

	public void setAttachData(String attachData) {
		this.attachData = attachData;
	}

	@Column(name = "delete_time", nullable = true)
	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

}

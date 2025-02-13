/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 玩家邮件，即收件箱和发件箱中的邮件
 * 
 * @author BruceSu
 * 
 */
@Entity
@Table(name = "role_mail_history")
public class RoleMailHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2976866195055854018L;

	private String id;
	private Role role;
	private String templeId; // 邮件模板ID

	public RoleMailHistory() {
	}

	public RoleMailHistory(String id, Role role, String templeId) {
		this.id = id;
		this.role = role;
		this.templeId = templeId;
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
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "templ_id", length = 5000)
	public String getTempleId() {
		return templeId;
	}

	public void setTempleId(String templeId) {
		this.templeId = templeId;
	}

}

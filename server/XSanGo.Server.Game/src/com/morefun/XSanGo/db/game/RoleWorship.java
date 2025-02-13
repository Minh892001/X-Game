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
 * 膜拜
 */
@Entity
@Table(name = "role_worship")
public class RoleWorship implements Serializable {
	private static final long serialVersionUID = -6859798253062194708L;
	private String id;
	private int copyId;// 关卡ID
	private Role role;
	private String byWorship;// 被膜拜者ID
	private Date worshipDate;

	public RoleWorship() {

	}

	public RoleWorship(String id, int copyId, Role role, String byWorship,
			Date worshipDate) {
		this.id = id;
		this.copyId = copyId;
		this.role = role;
		this.byWorship = byWorship;
		this.worshipDate = worshipDate;
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
	@JoinColumn(name = "role_id")
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "copy_id", nullable = false)
	public int getCopyId() {
		return copyId;
	}

	public void setCopyId(int copyId) {
		this.copyId = copyId;
	}

	@Column(name = "by_worship")
	public String getByWorship() {
		return byWorship;
	}

	public void setByWorship(String byWorship) {
		this.byWorship = byWorship;
	}

	@Column(name = "worship_date")
	public Date getWorshipDate() {
		return worshipDate;
	}

	public void setWorshipDate(Date worshipDate) {
		this.worshipDate = worshipDate;
	}

}

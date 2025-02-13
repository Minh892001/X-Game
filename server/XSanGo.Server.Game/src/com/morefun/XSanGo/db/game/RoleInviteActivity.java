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
 * 邀请好友奖励领取记录
 * 
 * @author lixiongming
 *
 */
@Entity
@Table(name = "role_invite_activity")
public class RoleInviteActivity implements Serializable {
	private static final long serialVersionUID = -4377977489892721947L;

	private String id;
	private Role role;
	/** 领取阈值 */
	private int threshold;

	public RoleInviteActivity() {
	}

	public RoleInviteActivity(String id, Role role, int threshold) {
		this.id = id;
		this.role = role;
		this.threshold = threshold;
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
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "threshold", nullable = false)
	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

}

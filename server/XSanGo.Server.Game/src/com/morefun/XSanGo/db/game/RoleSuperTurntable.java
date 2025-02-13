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
 * 超级转盘奖励领取记录
 * @author xiaojun.zhang
 *
 */
@Entity
@Table(name = "role_super_turntable")
public class RoleSuperTurntable implements Serializable {

	private static final long serialVersionUID = -8514202920691248329L;
	private String id;
	private String roleName;
	private Role role;
	private int scriptId;
	private int vipLevel;
	/**是否公告*/
	private int announceFlag;
	/** 领取时间 */
	private Date lastReceiveTime;

	public RoleSuperTurntable() {
	}

	public RoleSuperTurntable(String id, String roleName, Role role, int scriptId, int announceFlag,
			Date lastReceiveTime,int viplevel) {
		super();
		this.id = id;
		this.roleName = roleName;
		this.role = role;
		this.scriptId = scriptId;
		this.announceFlag = announceFlag;
		this.lastReceiveTime = lastReceiveTime;
		this.vipLevel = viplevel;
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

	@Column(name = "last_receive_time", nullable = false)
	public Date getLastReceiveTime() {
		return lastReceiveTime;
	}

	public void setLastReceiveTime(Date lastReceiveTime) {
		this.lastReceiveTime = lastReceiveTime;
	}

	@Column(name = "script_Id", nullable = false)
	public int getScriptId() {
		return scriptId;
	}

	public void setScriptId(int scriptId) {
		this.scriptId = scriptId;
	}
	@Column(name = "role_name", nullable = false)
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	@Column(name = "announce_flag", nullable = false)
	public int getAnnounceFlag() {
		return announceFlag;
	}

	public void setAnnounceFlag(int announceFlag) {
		this.announceFlag = announceFlag;
	}
	@Column(name = "vip_level", nullable = false)
	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

}

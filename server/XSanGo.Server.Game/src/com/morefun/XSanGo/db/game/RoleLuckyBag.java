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
 * 福袋领取记录
 * 
 * @author lixiongming
 */
@Entity
@Table(name = "role_lucky_bag")
public class RoleLuckyBag implements Serializable {
	private static final long serialVersionUID = -8511202220690248629L;
	private String id;
	private Role role;
	private int scriptId;
	/** 领取时间 */
	private Date lastReceiveTime;
	private int type;// 0-单日 1-当月

	public RoleLuckyBag() {
	}

	public RoleLuckyBag(String id, Role role, int scriptId,
			Date lastReceiveTime, int type) {
		this.id = id;
		this.role = role;
		this.scriptId = scriptId;
		this.lastReceiveTime = lastReceiveTime;
		this.type = type;
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

	@Column(name = "type", nullable = false)
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}

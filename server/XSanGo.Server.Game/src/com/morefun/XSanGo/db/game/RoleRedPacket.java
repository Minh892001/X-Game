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
 * 红包历史记录
 * 
 * @author lixiongming
 *
 */
@Entity
@Table(name = "role_red_packet")
public class RoleRedPacket implements Serializable {
	private static final long serialVersionUID = 8479861124485440890L;

	private String id;
	// 已发送的全局红包ID
	private String redPacketIds;
	// 是否通关了大神关卡
	private boolean isTongGuan;
	private Role role;

	public RoleRedPacket() {
	}

	public RoleRedPacket(String id, String redPacketIds, boolean isTongGuan,
			Role role) {
		super();
		this.id = id;
		this.redPacketIds = redPacketIds;
		this.isTongGuan = isTongGuan;
		this.role = role;
	}

	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "red_packet_ids")
	public String getRedPacketIds() {
		return redPacketIds;
	}

	public void setRedPacketIds(String redPacketIds) {
		this.redPacketIds = redPacketIds;
	}

	@Column(name = "is_tong_guan")
	public boolean isTongGuan() {
		return isTongGuan;
	}

	public void setTongGuan(boolean isTongGuan) {
		this.isTongGuan = isTongGuan;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}

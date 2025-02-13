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
 * 索要红包记录
 * 
 * @author guofeng.qin
 */
@Entity
@Table(name = "role_claim_redpacket")
public class RoleClaimRedPacket implements Serializable {

	private static final long serialVersionUID = -6123231956655850061L;

	private String id;
	private Role role;
	private String redPacketId;
	private Date updateDate;

	public RoleClaimRedPacket() {
		super();
	}

	public RoleClaimRedPacket(String id, Role role, String redPacketId, Date updateDate) {
		super();
		this.id = id;
		this.role = role;
		this.redPacketId = redPacketId;
		this.updateDate = updateDate;
	}

	/**
	 * @return the id
	 */
	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the role
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
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
	 * @return the redPacketId
	 */
	@Column(name = "redpacket_id", nullable = false)
	public String getRedPacketId() {
		return redPacketId;
	}

	/**
	 * @param redPacketId
	 *            the redPacketId to set
	 */
	public void setRedPacketId(String redPacketId) {
		this.redPacketId = redPacketId;
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

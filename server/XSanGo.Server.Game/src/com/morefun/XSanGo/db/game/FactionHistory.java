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
@SuppressWarnings("serial")
@Entity
@Table(name = "faction_history")
public class FactionHistory implements Serializable {
	private String id;
	private Faction faction;
	private String roleName;
	private int vipLevel;
	private String remark;
	private Date createTime;

	public FactionHistory() {

	}

	public FactionHistory(String id, Faction faction, String roleName,
			String remark, Date createTime, int vipLevel) {
		this.id = id;
		this.roleName = roleName;
		this.faction = faction;
		this.remark = remark;
		this.createTime = createTime;
		this.vipLevel = vipLevel;
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
	@JoinColumn(name = "faction_id", nullable = false)
	public Faction getFaction() {
		return faction;
	}

	public void setFaction(Faction faction) {
		this.faction = faction;
	}

	@Column(name = "remark", nullable = false)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "create_time", nullable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "role_name", nullable = false, columnDefinition = "varchar(30) default ''")
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = "vip_level", nullable = false)
	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

}

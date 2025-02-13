/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 煮酒论英雄 分享记录 数据库结构
 * 
 * @author zhuzhi.yang
 * 
 */
@Entity
@Table(name = "role_makewine_share_record")
public class RoleMakeWineShareRecord implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String roleId;
	private int configID; // 分享酒的xls配置编号
	private String roleName;
	private int lastCount;
	private int top;
	private Date topTime; // 置顶的时间
	private String receivedPlayers; // 领取过的玩家
	private Date shareTime; // 分享时间

	public RoleMakeWineShareRecord() {
	}

	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RoleMakeWineShareRecord(String id, String roleId, int configID, String roleName, int lastCount, Date shareTime, Date topTime) {
		this.id = id;
		this.roleId = roleId;
		this.configID = configID;
		this.roleName = roleName;
		this.lastCount = lastCount;
		this.shareTime = shareTime;
		this.topTime = topTime;
	}
	
	@Column(name = "role_id", nullable = false)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	@Column(name = "config_id", nullable = false)
	public int getConfigID() {
		return configID;
	}

	public void setConfigID(int configID) {
		this.configID = configID;
	}

	@Column(name = "role_name", nullable = false)
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = "last_count", nullable = false)
	public int getLastCount() {
		return lastCount;
	}

	public void setLastCount(int lastCount) {
		this.lastCount = lastCount;
	}

	@Column(name = "top", nullable = false)
	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}
	
	@Column(name = "top_time", nullable = true)
	public Date getTopTime() {
		return topTime;
	}

	public void setTopTime(Date topTime) {
		this.topTime = topTime;
	}
	
	@Column(name = "received_player", nullable = true)
	public String getReceivedPlayers() {
		return receivedPlayers;
	}

	public void setReceivedPlayers(String receivedPlayers) {
		this.receivedPlayers = receivedPlayers;
	}
	
	@Column(name = "share_time", nullable = false)
	public Date getShareTime() {
		return shareTime;
	}

	public void setShareTime(Date shareTime) {
		this.shareTime = shareTime;
	}

}

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
 * 聊天投票禁言
 * 
 * @author BruceSu
 * 
 */
@Entity
@Table(name = "role_chat_vote_forbidden")
public class ChatVoteForbidden implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8074968650182417999L;

	private String id;
	/**
	 * 发起投票的玩家
	 */
	private String roleId;
	
	/**
	 * 被投票的玩家
	 */
	private String targetId;
	
	/**
	 * 参与的玩家ID
	 */
	private String voteIds;

	/**
	 * 投票发起时间
	 */
	public Date addTime;


	public ChatVoteForbidden() {
	}

	public ChatVoteForbidden(String id, String roleId, String targetId, String voteIds, Date addTime) {
		this.id = id;
		this.roleId = roleId;
		this.targetId = targetId;
		this.voteIds = voteIds;
		this.addTime = addTime;
	}

	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "role_id", nullable = false)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	@Column(name = "target_id", nullable = false)
	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	@Column(name = "vote_ids", nullable = false)
	public String getVoteIds() {
		return voteIds;
	}
	public void setVoteIds(String voteIds) {
		this.voteIds = voteIds;
	}

	@Column(name = "add_time", nullable = false)
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

}

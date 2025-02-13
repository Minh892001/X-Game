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
 * 微信分享次数
 * 
 * @author sunjie
 */
@Entity
@Table(name = "role_share_weixin")
public class RoleWeixinShare implements Serializable {

	private static final long serialVersionUID = -5803455102855990585L;
	private String id;
	private Role role;

	/** 分享次数 */
	private int shareNums;
	/** 最后更新时间 */
	private Date lastUpdateTime;
	

	public RoleWeixinShare() {

	}

	public RoleWeixinShare(String id, Role role, int shareNums,Date updateDate) {
		super();
		this.id = id;
		this.role = role;
		this.shareNums = shareNums;
		this.lastUpdateTime = updateDate;
	}

	@Id
	@Column(name = "id", nullable = false, length = 64)
	public String getId() {
		return this.id;
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
	
	@Column(name = "share_times")
	public int getShareNums() {
		return shareNums;
	}

	public void setShareNums(int shareNums) {
		this.shareNums = shareNums;
	}
	
	@Column(name = "update_time")
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}

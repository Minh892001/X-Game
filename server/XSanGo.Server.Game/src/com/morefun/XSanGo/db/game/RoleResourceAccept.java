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
 * 用户奖励领取表
 * 
 * @author guofeng.qin
 */
 @Entity
 @Table(name = "role_resource_accept")
public class RoleResourceAccept implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private Role role;

	private String dateTag; // 日期
	private int type; // 类型
	private String tag; // 标记
	private String name; // 名称
	private int recvCount; // 领取次数
	private int totalCount; // 总的次数
	private int hasReceived; // 是否已领取
	private Date updateTime; // 更新时间

	public RoleResourceAccept() {
		super();
	}

	public RoleResourceAccept(String id, Role role, String dateTag, int type, String tag, String name, int recvCount,
			int totalCount, int hasReceived, Date updateTime) {
		super();
		this.id = id;
		this.role = role;
		this.dateTag = dateTag;
		this.type = type;
		this.tag = tag;
		this.name = name;
		this.recvCount = recvCount;
		this.totalCount = totalCount;
		this.hasReceived = hasReceived;
		this.updateTime = updateTime;
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
	 * @return the type
	 */
	@Column(name = "type", nullable = false)
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the tag
	 */
	@Column(name = "tag", nullable = false)
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag
	 *            the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * @return the name
	 */
	@Column(name = "name", nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the hasReceived
	 */
	@Column(name = "is_received", nullable = false)
	public int getHasReceived() {
		return hasReceived;
	}

	/**
	 * @param hasReceived
	 *            the hasReceived to set
	 */
	public void setHasReceived(int hasReceived) {
		this.hasReceived = hasReceived;
	}

	/**
	 * @return the updateTime
	 */
	@Column(name = "update_time", nullable = false)
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 *            the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the recvCount
	 */
	@Column(name = "recv_count", nullable = false)
	public int getRecvCount() {
		return recvCount;
	}

	/**
	 * @param recvCount
	 *            the recvCount to set
	 */
	public void setRecvCount(int recvCount) {
		this.recvCount = recvCount;
	}

	/**
	 * @return the totalCount
	 */
	@Column(name = "total_count", nullable = false)
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount
	 *            the totalCount to set
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @return the date
	 */
	@Column(name = "date_tag", nullable = false)
	public String getDateTag() {
		return dateTag;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDateTag(String date) {
		this.dateTag = date;
	}

}

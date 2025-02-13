package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 玩家彩蛋记录
 * 
 */
@Entity
@Table(name = "role_colorfullegg")
public class RoleColorfulEgg implements Serializable {

	private static final long serialVersionUID = -8794989939868855024L;

	private String roleId;
	private Role role;
	/** 砸蛋获取对应奖励配置 彩蛋标记-预获取物品Id-预获取物品数量 */
	private String record;
	/** 领奖获取对应奖励配置 */
	private String acceptScriptId;
	/** 砸蛋时间 */
	private Date brokenTime;
	/** 领奖时间 */
	private Date receiveTime;
	/** 领奖时间 */
	private Date startTime;
	/** 领奖次数 */
	private int acceptNum;

	public RoleColorfulEgg() {
	}

	public RoleColorfulEgg(Role role, String record, String acceptScriptId, Date brokenTime, Date receiveTime,
			int acceptNum, Date startTime) {
		this.role = role;
		this.record = record;
		this.acceptScriptId = acceptScriptId;
		this.brokenTime = brokenTime;
		this.receiveTime = receiveTime;
		this.startTime = startTime;
		this.acceptNum = acceptNum;
	}

	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "role") )
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "role_id", nullable = false)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "record")
	public String getRecord() {
		return record;
	}

	public void setRecord(String record) {
		this.record = record;
	}

	@Column(name = "accept_script_Id")
	public String getAcceptScriptId() {
		return acceptScriptId;
	}

	public void setAcceptScriptId(String acceptScriptId) {
		this.acceptScriptId = acceptScriptId;
	}

	/** 最后一次砸蛋时间 */
	@Column(name = "broken_time")
	public Date getBrokenTime() {
		return brokenTime;
	}

	public void setBrokenTime(Date brokenTime) {
		this.brokenTime = brokenTime;
	}

	@Column(name = "receive_time")
	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	@Column(name = "accept_number")
	public int getAcceptNum() {
		return acceptNum;
	}

	public void setAcceptNum(int acceptNum) {
		this.acceptNum = acceptNum;
	}

	/**活动开始时间*/
	@Column(name = "start_time")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

}

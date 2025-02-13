package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 全局红包
 * 
 * @author lixiongming
 *
 */
@Entity
@Table(name = "red_packet")
public class RedPacket implements Serializable {
	private static final long serialVersionUID = 8479861124485440890L;

	private String id;
	private int taskId;
	private String senderId;// 红包发送者，控制自己不能领取
	private Date createTime;

	public RedPacket() {
	}

	public RedPacket(String id, int taskId, String senderId,Date createTime) {
		super();
		this.id = id;
		this.taskId = taskId;
		this.createTime = createTime;
		this.senderId = senderId;
	}

	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "create_time", nullable = false)
	public Date getCreateTime() {
		return createTime;
	}

	@Column(name = "task_id", nullable = false)
	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	@Column(name = "sender_id", nullable = false)
	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

}

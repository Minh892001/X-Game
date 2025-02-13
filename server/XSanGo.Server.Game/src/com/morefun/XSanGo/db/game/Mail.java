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
 * 邮件中心中待处理的邮件，即玩家发送，但尚未被接收的邮件
 * 
 * @author BruceSu
 * 
 */
@Entity
@Table(name = "mail")
public class Mail implements Serializable {

	private static final long serialVersionUID = 8479861124485440890L;

	private String id;
	private int source;
	private String senderId;
	private String senderName;
	private String acceptorId;
	private String title;
	private String body;
	private String attachments;
	private Date createTime;
	private String params = "";

	public Mail() {
	}

	public Mail(String id, int source, String senderId, String senderName, String acceptorId, String title,
			String body, String attachments, Date createTime) {
		this.id = id;
		this.source = source;
		this.senderId = senderId;
		this.senderName = senderName;
		this.acceptorId = acceptorId;
		this.title = title;
		this.body = body;
		this.attachments = attachments;
		this.createTime = createTime;
	}

	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSource(int source) {
		this.source = source;
	}

	@Column(name = "source", nullable = false)
	public int getSource() {
		return source;
	}

	@Column(name = "sender_id", nullable = false)
	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	@Column(name = "sender_name", nullable = false)
	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	@Column(name = "acceptor_id", nullable = false)
	public String getAcceptorId() {
		return acceptorId;
	}

	public void setAcceptorId(String acceptorId) {
		this.acceptorId = acceptorId;
	}

	@Column(name = "title", nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "body", nullable = false)
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Column(name = "attachments", nullable = false, length = 1000)
	public String getAttachments() {
		return attachments;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "create_time", nullable = false)
	public Date getCreateTime() {
		return createTime;
	}

	@Column(name = "params")
	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
}

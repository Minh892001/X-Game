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
 * 玩家邮件，即收件箱和发件箱中的邮件
 * 
 * @author BruceSu
 * 
 */
@Entity
@Table(name = "role_mail")
public class RoleMail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3236037269430718656L;

	private String id;
	private Role role;
	private int type; // 0:公告邮件，1：系统邮件,阅后即焚，2：私人邮件
	private int state; // 1:未读，2：已读，9：已删除
	private String senderId;
	private String senderName;
	private String acceptorName;
	private String title;
	private String body;
	private String attachments;
	private Date createTime;
	private String templId; // 邮件模板ID

	public RoleMail() {

	}

	public RoleMail(String id, Role role, int type, int state, String senderId,
			String senderName, String acceptorName, String title, String body,
			String attachments, Date createTime, String templId) {
		this.id = id;
		this.role = role;
		this.type = type;
		this.state = state;
		this.senderId = senderId;
		this.senderName = senderName;
		this.acceptorName = acceptorName;
		this.title = title;
		this.body = body;
		this.attachments = attachments;
		this.createTime = createTime;
		this.templId = templId;
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
	@JoinColumn(name = "role_id", nullable = false)
	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "type", nullable = false)
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	@Column(name = "acceptor_name", nullable = false)
	public String getAcceptorName() {
		return acceptorName;
	}

	public void setAcceptorName(String acceptorName) {
		this.acceptorName = acceptorName;
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

	public void setState(int state) {
		this.state = state;
	}

	@Column(name = "state", nullable = false)
	public int getState() {
		return state;
	}

	@Column(name = "templ_id", nullable = false, columnDefinition = "")
	public String getTemplId() {
		return templId;
	}

	public void setTemplId(String templId) {
		this.templId = templId;
	}
}

/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 聊天消息
 * 
 * @author BruceSu
 * 
 */
@Entity
@Table(name = "chat_message")
public class ChatMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2812972728583755997L;

	private int id;
	private int channel;
	private String senderId;
	private String senderName;
	private String acceptorName;
	private String content;
	private Date createTime;

	public ChatMessage() {
	}

	public ChatMessage(int channel, String senderId, String senderName,
			String acceptorName, String content, Date createTime) {
		this.channel = channel;
		this.senderId = senderId;
		this.senderName = senderName;
		this.acceptorName = acceptorName;
		this.content = content;
		this.createTime = createTime;
	}

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "channel", nullable = false)
	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
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

	@Column(name = "acceptor_name")
	public String getAcceptorName() {
		return acceptorName;
	}

	public void setAcceptorName(String acceptorName) {
		this.acceptorName = acceptorName;
	}

	@Column(name = "content", nullable = false)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "create_time", nullable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}

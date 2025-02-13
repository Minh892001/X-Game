/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 聊天消息 离线消息
 * 
 * @author BruceSu
 * 
 */
@Entity
@Table(name = "chat_message_offline")
public class ChatMessageOffline implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5093355375376182702L;

	private int id;
	private int channel;
	private String senderId;
	private String senderName;
	private short senderLevel;
	private int senderVip;
	private String senderIcon;
	private String senderContent;
	private int senderOfficalRankId; // 官阶
	private Date createTime;

	private String acceptorName;
	private Role role;

	public ChatMessageOffline() {
	}

	public ChatMessageOffline(int channel, String senderId, String senderName,
			int senderLevel, int senderVip, String senderIcon,
			int senderOfficalRankId, String senderContent, Date createTime,
			String acceptorName, Role role) {

		this.channel = channel;
		this.senderId = senderId;
		this.senderName = senderName;
		this.senderLevel = (short) senderLevel;
		this.senderVip = senderVip;
		this.senderIcon = senderIcon;
		this.senderOfficalRankId = senderOfficalRankId;
		this.senderContent = senderContent;
		this.createTime = createTime;
		this.acceptorName = acceptorName;
		this.role = role;
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

	@Column(name = "sender_level", nullable = false)
	public short getSenderLevel() {
		return senderLevel;
	}

	public void setSenderLevel(short senderLevel) {
		this.senderLevel = senderLevel;
	}

	@Column(name = "sender_vip", nullable = false)
	public int getSenderVip() {
		return senderVip;
	}

	public void setSenderVip(int senderVip) {
		this.senderVip = senderVip;
	}

	@Column(name = "sender_icon", nullable = false)
	public String getSenderIcon() {
		return senderIcon;
	}

	public void setSenderIcon(String senderIcon) {
		this.senderIcon = senderIcon;
	}

	@Column(name = "sender_offical_rankId", nullable = false)
	public int getSenderOfficalRankId() {
		return senderOfficalRankId;
	}

	public void setSenderOfficalRankId(int senderOfficalRankId) {
		this.senderOfficalRankId = senderOfficalRankId;
	}

	@Column(name = "sender_content", nullable = false, length = 1000)
	public String getSenderContent() {
		return senderContent;
	}

	public void setSenderContent(String senderContent) {
		this.senderContent = senderContent;
	}

	@Column(name = "create_time", nullable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}

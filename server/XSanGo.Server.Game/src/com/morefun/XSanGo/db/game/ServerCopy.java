/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 副本占领
 * 
 * @author sulingyun
 * 
 */
@Entity
@Table(name = "server_copy")
public class ServerCopy implements Serializable {
	
	private static final long serialVersionUID = -1191646838287250999L;
	private int id;
	private ServerConfig server;
	private int templateId;
	private String championId;

	public ServerCopy() {
	}

	public ServerCopy(ServerConfig server, int templateId, String championId) {
		this.server = server;
		this.templateId = templateId;
		this.championId = championId;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "server_id", nullable = false)
	public ServerConfig getServer() {
		return server;
	}

	public void setServer(ServerConfig server) {
		this.server = server;
	}

	@Column(name = "template_id", nullable = false)
	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	@Column(name = "champion_id", nullable = false)
	public String getChampionId() {
		return championId;
	}

	public void setChampionId(String championId) {
		this.championId = championId;
	}
}

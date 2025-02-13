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

@Entity
@Table(name = "server_guide_counter")
public class ServerGuideCounter implements Serializable {

	private static final long serialVersionUID = 1235089784977664178L;
	private int id;
	private ServerConfig server;
	private int guideId;
	private int counter;

	public ServerGuideCounter() {

	}

	public ServerGuideCounter(ServerConfig server, int guideId, int counter) {
		this.server = server;
		this.guideId = guideId;
		this.counter = counter;
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

	@Column(name = "guide_id", nullable = false)
	public int getGuideId() {
		return guideId;
	}

	public void setGuideId(int guideId) {
		this.guideId = guideId;
	}

	@Column(name = "counter", nullable = false)
	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

}

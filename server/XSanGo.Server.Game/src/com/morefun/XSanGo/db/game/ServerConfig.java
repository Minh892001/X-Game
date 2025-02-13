/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * 服务器运行配置
 * 
 * @author sulingyun
 * 
 */
@Entity
@Table(name = "server_config")
public class ServerConfig implements Serializable {
	private static final long serialVersionUID = 554889267831359659L;
	private int id;
	/**
	 * 序列号，用于生成特定实例的唯一编号
	 */
	private long sequence;
	private Date openTime;
	private long factionSeq;// 公会ID

	private Map<Integer, ServerCopy> serverCopys = new HashMap<Integer, ServerCopy>(0);

	public ServerConfig() {
	}

	public ServerConfig(long sequence, Date open, long factionSeq) {
		this.sequence = sequence;
		this.openTime = (Date) open.clone();
		this.factionSeq = factionSeq;
	}

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "sequence", nullable = false)
	public long getSequence() {
		return sequence;
	}

	public void setSequence(long sequence) {
		this.sequence = sequence;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "server")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "templateId")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, ServerCopy> getServerCopys() {
		return serverCopys;
	}

	public void setServerCopys(Map<Integer, ServerCopy> serverCopys) {
		this.serverCopys = serverCopys;
	}

	@Column(name = "open_time", nullable = false)
	public Date getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}

	@Column(name = "faction_seq", nullable = false, columnDefinition = "bigint default 1000")
	public long getFactionSeq() {
		return factionSeq;
	}

	public void setFactionSeq(long factionSeq) {
		this.factionSeq = factionSeq;
	}
}

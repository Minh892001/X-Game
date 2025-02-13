package com.morefun.XSanGo.db.stat;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.morefun.XSanGo.util.TextUtil;

@Entity
@Table(name = "stat_ecnomy")
public class StatEcnomy implements Serializable {

	private long id;
	private Date statDate;
	private int serverId;
	private int type;
	private long produce;
	private long consume;
	private long total;

	public StatEcnomy(Date statDate, int serverId, int type, long produce,
			long consume, long total) {
		this.statDate = statDate;
		this.serverId = serverId;
		this.type = type;
		this.produce = produce;
		this.consume = consume;
		this.total = total;
	}

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "stat_date", nullable = false, columnDefinition = "date not null")
	public Date getStatDate() {
		return statDate;
	}

	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}

	@Column(name = "server_id", nullable = false)
	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	@Column(name = "produce", nullable = false)
	public long getProduce() {
		return produce;
	}

	public void setProduce(long produce) {
		this.produce = produce;
	}

	@Column(name = "consume", nullable = false)
	public long getConsume() {
		return consume;
	}

	public void setConsume(long consume) {
		this.consume = consume;
	}

	@Column(name = "type", nullable = false)
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Column(name = "total", nullable = false)
	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return TextUtil.format("type={0},produce={1},consume={2},total={3}.",
				this.getType(), this.getProduce(), this.getConsume(),
				this.getTotal());
	}

}

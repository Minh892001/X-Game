package com.morefun.XSanGo.db;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 跨服竞技场战报
 * 
 * @author xiongming.li
 *
 */
@Entity
public class CrossArenaMovie implements Serializable {

	private static final long serialVersionUID = 2552581505431909398L;
	
	private String id;
	private String fightMovie; // 压缩的战报数据 FightMovieView的json
	private Date datetime;

	public CrossArenaMovie() {

	}

	public CrossArenaMovie(String id, String fightMovie, Date datetime) {
		this.id = id;
		this.fightMovie = fightMovie;
		this.datetime = datetime;
	}

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(columnDefinition = "mediumtext")
	public String getFightMovie() {
		return fightMovie;
	}

	public void setFightMovie(String fightMovie) {
		this.fightMovie = fightMovie;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

}

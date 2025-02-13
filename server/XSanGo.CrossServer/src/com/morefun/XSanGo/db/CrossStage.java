package com.morefun.XSanGo.db;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 每个跨服区间的淘汰赛阶段
 * 
 * @author xiongming.li
 *
 */
@Entity
public class CrossStage implements Serializable {

	private static final long serialVersionUID = -2809304228835695192L;
	private int id;
	private int crossId;
	private int stage;

	public CrossStage() {

	}

	public CrossStage(int crossId, int stage) {
		this.crossId = crossId;
		this.stage = stage;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCrossId() {
		return crossId;
	}

	public void setCrossId(int crossId) {
		this.crossId = crossId;
	}

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

}

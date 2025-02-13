package com.morefun.XSanGo.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.XSanGo.Protocol.CrossMovieView;

/**
 * 淘汰赛对阵表
 * 
 * @author xiongming.li
 *
 */
@Entity
public class Schedule implements Serializable {
	private static final long serialVersionUID = -343314751571371037L;

	private int id;
	private int crossId;
	private int groupNum;// 小组编号
	private int stage;// 阶段 32强 16强等
	private int orderNum;// 对阵编号
	private String roleView1;
	private String roleView2;
	private String winRoleView;
	private String movieView = "[]";// 战报 压缩CrossMovieView的json的String[]
	private int battlePower1;
	private int battlePower2;
	// 临时字段 不存入数据库
	private List<CrossMovieView> movieList = new ArrayList<CrossMovieView>();

	public Schedule() {

	}

	public Schedule(int crossId, int stage, int orderNum, String roleView1, String roleView2, int battlePower1,
			int battlePower2) {
		this.crossId = crossId;
		this.stage = stage;
		this.orderNum = orderNum;
		this.roleView1 = roleView1;
		this.roleView2 = roleView2;
		this.battlePower1 = battlePower1;
		this.battlePower2 = battlePower2;
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

	public int getGroupNum() {
		return groupNum;
	}

	public void setGroupNum(int groupNum) {
		this.groupNum = groupNum;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	@Column(length = 1000)
	public String getRoleView1() {
		return roleView1;
	}

	public void setRoleView1(String roleView1) {
		this.roleView1 = roleView1;
	}

	@Column(length = 1000)
	public String getRoleView2() {
		return roleView2;
	}

	public void setRoleView2(String roleView2) {
		this.roleView2 = roleView2;
	}

	@Column(length = 1000)
	public String getWinRoleView() {
		return winRoleView;
	}

	public void setWinRoleView(String winRoleView) {
		this.winRoleView = winRoleView;
	}

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	@Column(columnDefinition = "mediumtext")
	public String getMovieView() {
		return movieView;
	}

	public void setMovieView(String movieView) {
		this.movieView = movieView;
	}

	@Transient
	public List<CrossMovieView> getMovieList() {
		return movieList;
	}

	public void setMovieList(List<CrossMovieView> movieList) {
		this.movieList = movieList;
	}

	public int getBattlePower1() {
		return battlePower1;
	}

	public void setBattlePower1(int battlePower1) {
		this.battlePower1 = battlePower1;
	}

	public int getBattlePower2() {
		return battlePower2;
	}

	public void setBattlePower2(int battlePower2) {
		this.battlePower2 = battlePower2;
	}

}

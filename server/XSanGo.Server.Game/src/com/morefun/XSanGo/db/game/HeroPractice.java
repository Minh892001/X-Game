package com.morefun.XSanGo.db.game;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 武将修炼
 */
@Entity
@Table(name = "hero_practice")
public class HeroPractice implements Serializable {
	private static final long serialVersionUID = -3050633495390055384L;
	private String id;
	private RoleHero hero;
	private int scriptId; // 脚本ID
	private String propName;// 属性名
	private int color;// 颜色 1 2 3
	private int level;// 修炼等级
	private int exp;
	private int addValue;// 修炼增加的基础值
	private int nextUpExp;// 下次升级需要的经验
	private int indexof;// 位置

	public HeroPractice() {

	}

	public HeroPractice(String id, RoleHero hero, int scriptId, String propName, int level, int exp, int addValue,
			int nextUpExp, int indexof, int color) {
		this.id = id;
		this.hero = hero;
		this.scriptId = scriptId;
		this.propName = propName;
		this.level = level;
		this.exp = exp;
		this.addValue = addValue;
		this.nextUpExp = nextUpExp;
		this.indexof = indexof;
		this.color = color;
	}

	@Id
	@Column(name = "id", nullable = false, length = 64)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hero_id", nullable = false)
	public RoleHero getHero() {
		return hero;
	}

	public void setHero(RoleHero hero) {
		this.hero = hero;
	}

	@Column(name = "level", nullable = false)
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Column(name = "script_id", nullable = false)
	public int getScriptId() {
		return scriptId;
	}

	public void setScriptId(int scriptId) {
		this.scriptId = scriptId;
	}

	@Column(name = "prop_name")
	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	@Column(name = "exp")
	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	@Column(name = "add_value")
	public int getAddValue() {
		return addValue;
	}

	public void setAddValue(int addValue) {
		this.addValue = addValue;
	}

	@Column(name = "next_up_exp")
	public int getNextUpExp() {
		return nextUpExp;
	}

	public void setNextUpExp(int nextUpExp) {
		this.nextUpExp = nextUpExp;
	}

	@Column(name = "indexof")
	public int getIndexof() {
		return indexof;
	}

	public void setIndexof(int indexof) {
		this.indexof = indexof;
	}

	@Column(name = "color")
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "HeroPractice [id=" + id + ", scriptId=" + scriptId + ", propName=" + propName + ", color=" + color
				+ ", level=" + level + ", exp=" + exp + ", addValue=" + addValue + ", nextUpExp=" + nextUpExp
				+ ", indexof=" + indexof + "]";
	}

}

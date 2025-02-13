/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: RoleHeroAwaken
 * 功能描述：
 * 文件名：RoleHeroAwaken.java
 **************************************************
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 角色武将觉醒数据
 * 
 * @author zwy
 * @since 2015-11-14
 * @version 1.0
 */
@Entity
@Table(name = "role_hero_awaken")
public class RoleHeroAwaken implements Serializable {

	private static final long serialVersionUID = 1135748362170724233L;

	/** 角色武将数据对象 */
	private RoleHero hero;

	/** 武将编号 */
	private String hero_id;

	/** 觉醒星级 */
	private int star;

	/** 是否觉醒 */
	private byte is_awaken;

	/** 洗炼等级 */
	private int lvl;

	/** 洗炼属性 */
	private String baptizeProps;

	/** 洗炼消耗 */
	private String baptizeConsume;

	public RoleHeroAwaken() {
	}

	/**
	 * @return Returns the hero.
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public RoleHero getHero() {
		return hero;
	}

	/**
	 * @param hero The hero to set.
	 */
	public void setHero(RoleHero hero) {
		this.hero = hero;
	}

	/**
	 * @return Returns the hero_id.
	 */
	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "role_hero"))
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "hero_id", nullable = false)
	public String getHero_id() {
		return hero_id;
	}

	/**
	 * @param hero_id The hero_id to set.
	 */
	public void setHero_id(String hero_id) {
		this.hero_id = hero_id;
	}

	/**
	 * @return Returns the star.
	 */
	@Column(name = "awaken_star")
	public int getStar() {
		return star;
	}

	/**
	 * @param star The star to set.
	 */
	public void setStar(int star) {
		this.star = star;
	}

	/**
	 * @return Returns the is_awaken.
	 */
	@Column(name = "is_awaken")
	public byte getIs_awaken() {
		return is_awaken;
	}

	/**
	 * @param is_awaken The is_awaken to set.
	 */
	public void setIs_awaken(byte is_awaken) {
		this.is_awaken = is_awaken;
	}

	/**
	 * @return Returns the lvl.
	 */
	@Column(name = "baptize_lvl")
	public int getLvl() {
		return lvl;
	}

	/**
	 * @param lvl The lvl to set.
	 */
	public void setLvl(int lvl) {
		this.lvl = lvl;
	}

	/**
	 * @return Returns the baptizeProps.
	 */
	@Column(name = "baptize_prop", nullable = true)
	public String getBaptizeProps() {
		return baptizeProps;
	}

	/**
	 * @param baptizeProps The baptizeProps to set.
	 */
	public void setBaptizeProps(String baptizeProps) {
		this.baptizeProps = baptizeProps;
	}

	/**
	 * @return Returns the baptizeConsume.
	 */
	@Column(name = "baptize_consume")
	public String getBaptizeConsume() {
		return baptizeConsume;
	}

	/**
	 * @param baptizeConsume The baptizeConsume to set.
	 */
	public void setBaptizeConsume(String baptizeConsume) {
		this.baptizeConsume = baptizeConsume;
	}
}

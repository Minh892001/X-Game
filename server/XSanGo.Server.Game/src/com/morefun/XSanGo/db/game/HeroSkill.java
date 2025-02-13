/**
 * 
 */
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
 * 武将技能数据
 * 
 * @author sulingyun
 * 
 */
@Entity
@Table(name = "role_hero_skill")
public class HeroSkill implements Serializable {

	private static final long serialVersionUID = -6216669647566038200L;
	private String id;
	private RoleHero hero;
	private int templateId;
	private int level;

	public HeroSkill() {
	}

	public HeroSkill(String id, RoleHero hero, int templateId, int level) {
		this.id = id;
		this.hero = hero;
		this.templateId = templateId;
		this.level = level;
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

	@Column(name = "template_id", nullable = false)
	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	@Column(name = "level", nullable = false)
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}

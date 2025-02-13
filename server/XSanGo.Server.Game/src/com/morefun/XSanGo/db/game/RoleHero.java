/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * 武将数据
 * 
 * @author Su LingYun
 * 
 */
@Entity
@Table(name = "role_hero")
public class RoleHero implements Serializable {
	private static final long serialVersionUID = 1533538272977579032L;
	private String id;
	private Role role;
	private int templateId;
	private int level;
	private int exp;
	/** 品质颜色 */
	private byte color;
	/** 星级 */
	private byte star;
	/** 突破等级 */
	private int breakLevel;

	/** 武将装备信息 */
	private String equipConfig;
	/** 附加数据 */
	private String attachData = "{}";
	/**专属随从配置*/
	private String specialAttendant;
	
	/** 觉醒数据 */
	private RoleHeroAwaken roleHeroAwaken;
	
	private Map<Integer, HeroSkill> heroSkills = new HashMap<Integer, HeroSkill>();
	private Map<Integer, RoleHeroRelation> roleHeroRelations = new HashMap<Integer, RoleHeroRelation>();
	private Map<Integer, HeroPractice> heroPractice = new HashMap<Integer, HeroPractice>();

	public RoleHero() {
	}

	public RoleHero(String id, Role role, int templateId, byte color, byte star) {
		this.id = id;
		this.role = role;
		this.templateId = templateId;
		this.level = 1;
		this.exp = 0;
		this.color = color;
		this.star = star;
	}

	// Property accessors
	@Id
	@Column(name = "id", nullable = false, length = 64)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
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

	@Column(name = "exp", nullable = false)
	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	@Column(name = "color", nullable = false)
	public byte getColor() {
		return color;
	}

	public void setColor(byte color) {
		this.color = color;
	}

	@Column(name = "star", nullable = false)
	public byte getStar() {
		return star;
	}

	public void setStar(byte star) {
		this.star = star;
	}

	@Column(name = "equip_config", length = 512)
	public String getEquipConfig() {
		return equipConfig;
	}

	public void setEquipConfig(String equipConfig) {
		this.equipConfig = equipConfig;
	}

	@Column(name = "attach_data", nullable = true)
	public String getAttachData() {
		return attachData;
	}

	public void setAttachData(String attachData) {
		this.attachData = attachData;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "hero")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "templateId")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, HeroSkill> getHeroSkills() {
		return heroSkills;
	}

	public void setHeroSkills(Map<Integer, HeroSkill> heroSkills) {
		this.heroSkills = heroSkills;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "hero")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "templateId")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, RoleHeroRelation> getRoleHeroRelations() {
		return roleHeroRelations;
	}

	public void setRoleHeroRelations(
			Map<Integer, RoleHeroRelation> roleHeroRelations) {
		this.roleHeroRelations = roleHeroRelations;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "hero")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@MapKey(name = "scriptId")
	@Fetch(FetchMode.SELECT)
	public Map<Integer, HeroPractice> getHeroPractice() {
		return heroPractice;
	}

	public void setHeroPractice(Map<Integer, HeroPractice> heroPractice) {
		this.heroPractice = heroPractice;
	}

	@Column(name = "break_level")
	public int getBreakLevel() {
		return breakLevel;
	}

	public void setBreakLevel(int breakLevel) {
		this.breakLevel = breakLevel;
	}
	@Column(name = "special_attendant")
	public String getSpecialAttendant() {
		return specialAttendant;
	}

	public void setSpecialAttendant(String specialAttendant) {
		this.specialAttendant = specialAttendant;
	}

	/**
	 * @return Returns the roleHeroAwaken.
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "hero")
	public RoleHeroAwaken getRoleHeroAwaken() {
		return roleHeroAwaken;
	}

	/**
	 * @param roleHeroAwaken The roleHeroAwaken to set.
	 */
	public void setRoleHeroAwaken(RoleHeroAwaken roleHeroAwaken) {
		this.roleHeroAwaken = roleHeroAwaken;
	}
}

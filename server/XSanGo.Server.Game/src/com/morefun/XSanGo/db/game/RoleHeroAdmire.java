/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

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
 * 名将仰慕 角色相关 数据库结构
 * 
 * @author 吕明涛
 */
@Entity
@Table(name = "role_hero_admire")
public class RoleHeroAdmire implements Serializable {
	/** */
	private static final long serialVersionUID = 580942758997659276L;

	private String roleId;;
	private Role role;
	private int heroId; // 武将ID
	private int value; // 武将仰慕值
	private String heroList; // 武将ID列表。JSON格式，格式：武将ID，武将ID
	private Date heroRefreshDate; // 武将刷新时间
	private String itemList; // 仰慕的道具列表，JSON格式
	private Date itemRefreshDate; // 道具刷新时间

	public RoleHeroAdmire() {
	}

	public RoleHeroAdmire(String roleId, Role role, int heroId, int value,
			Date heroRefreshDate, Date itemRefreshDate) {
		this.roleId = roleId;
		this.role = role;
		this.heroId = heroId;
		this.value = value;
		this.heroRefreshDate = heroRefreshDate;
		this.itemRefreshDate = itemRefreshDate;
	}

	public RoleHeroAdmire(String roleId, Role role, int heroId, int value,
			String heroList, Date heroRefreshDate, String itemList,
			Date itemRefreshDate) {
		this.roleId = roleId;
		this.role = role;
		this.heroId = heroId;
		this.value = value;
		this.heroList = heroList;
		this.heroRefreshDate = heroRefreshDate;
		this.itemList = itemList;
		this.itemRefreshDate = itemRefreshDate;
	}

	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "role"))
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "role_id", nullable = false)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "hero_id", columnDefinition = "INT default 0")
	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	@Column(name = "value", columnDefinition = "INT default 0")
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Column(name = "hero_list")
	public String getHeroList() {
		return heroList;
	}

	public void setHeroList(String heroList) {
		this.heroList = heroList;
	}

	@Column(name = "hero_refresh_date")
	public Date getHeroRefreshDate() {
		return heroRefreshDate;
	}

	public void setHeroRefreshDate(Date heroRefreshDate) {
		this.heroRefreshDate = heroRefreshDate;
	}

	@Column(name = "item_list")
	public String getItemList() {
		return itemList;
	}

	public void setItemList(String itemList) {
		this.itemList = itemList;
	}

	@Column(name = "item_refresh_date")
	public Date getItemRefreshDate() {
		return itemRefreshDate;
	}

	public void setItemRefreshDate(Date itemRefreshDate) {
		this.itemRefreshDate = itemRefreshDate;
	}
}

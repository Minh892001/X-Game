/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * 玩家打开菜单时间，控制一天只亮一次红点
 * 
 * @author xiongming.li
 *
 */
@Entity
@Table(name = "role_opened_menu")
public class RoleOpenedMenu implements Serializable {

	private static final long serialVersionUID = -4151865346257096692L;
	private String id;
	private Role role;
	private Date openAnnounceDate;// 公告
	private Date openMakeVipDate;// 我要做VIP
	private Date openHeroAdmireDate;// 名将仰慕
	private Date openHeroCallDate;// 名将招唤
	private Date openVipGiftDate;// vip礼包

	public RoleOpenedMenu() {
	
	}

	public RoleOpenedMenu(String id, Role role) {
		this.id = id;
		this.role = role;
	}
	
	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "open_announce_date")
	public Date getOpenAnnounceDate() {
		return openAnnounceDate;
	}

	public void setOpenAnnounceDate(Date openAnnounceDate) {
		this.openAnnounceDate = openAnnounceDate;
	}

	@Column(name = "open_make_vip_date")
	public Date getOpenMakeVipDate() {
		return openMakeVipDate;
	}

	public void setOpenMakeVipDate(Date openMakeVipDate) {
		this.openMakeVipDate = openMakeVipDate;
	}

	@Column(name = "open_hero_admire_date")
	public Date getOpenHeroAdmireDate() {
		return openHeroAdmireDate;
	}

	public void setOpenHeroAdmireDate(Date openHeroAdmireDate) {
		this.openHeroAdmireDate = openHeroAdmireDate;
	}

	@Column(name = "open_hero_call_date")
	public Date getOpenHeroCallDate() {
		return openHeroCallDate;
	}

	public void setOpenHeroCallDate(Date openHeroCallDate) {
		this.openHeroCallDate = openHeroCallDate;
	}

	@Column(name = "open_vip_gift_date")
	public Date getOpenVipGiftDate() {
		return openVipGiftDate;
	}

	public void setOpenVipGiftDate(Date openVipGiftDate) {
		this.openVipGiftDate = openVipGiftDate;
	}

}

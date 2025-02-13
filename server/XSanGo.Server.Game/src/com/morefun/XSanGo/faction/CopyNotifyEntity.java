package com.morefun.XSanGo.faction;

/**
 * 公会副本通知类
 * @author lixiongming
 *
 */
public class CopyNotifyEntity {
	public String roleName;
	public String icon;
	public int vipLevel;

	public CopyNotifyEntity() {

	}

	public CopyNotifyEntity(String roleName, String icon, int vipLevel) {
		this.roleName = roleName;
		this.icon = icon;
		this.vipLevel = vipLevel;
	}

}

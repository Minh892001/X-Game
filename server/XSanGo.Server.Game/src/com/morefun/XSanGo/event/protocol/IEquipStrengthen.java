package com.morefun.XSanGo.event.protocol;

import com.morefun.XSanGo.equip.EquipItem;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 
 * 强化装备
 * 
 * @author lvmingtao
 */

@signalslot
public interface IEquipStrengthen {
	/**
	 * 强化装备
	 * 
	 * @param auto
	 *            是否是一键强化;0,普通强化;1,一键强化
	 * @param equip
	 *            强化的装备
	 * @param beforeLevel
	 *            强化前等级
	 * @param afterLevel
	 *            强化后等级
	 */
	void onEquipStrengthen(int auto, EquipItem equip, int beforeLevel, int afterLevel);
}

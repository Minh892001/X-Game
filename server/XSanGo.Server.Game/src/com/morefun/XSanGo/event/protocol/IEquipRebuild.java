/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.morefun.XSanGo.equip.EquipItem;

/**
 * 装备重铸事件
 * 
 * @author sulingyun
 * 
 */
@signalslot
public interface IEquipRebuild {
	void onEquipRebuild(EquipItem equip);
}

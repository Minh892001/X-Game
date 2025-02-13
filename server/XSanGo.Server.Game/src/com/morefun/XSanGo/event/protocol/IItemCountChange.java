/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.morefun.XSanGo.item.IItem;

/**
 * 物品数量变更事件
 * 
 * @author sulingyun
 * 
 */
@signalslot
public interface IItemCountChange {
	void onItemCountChange(IItem item, int change);
}

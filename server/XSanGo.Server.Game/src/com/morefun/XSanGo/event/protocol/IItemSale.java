/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import com.morefun.XSanGo.item.IItem;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 出售物品事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IItemSale {
	void onItemSaled(IItem item, int count, int money);
}

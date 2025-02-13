/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.morefun.XSanGo.item.IItem;

/**
 * 宝石合成数量变更事件
 * 
 * @author sunjie
 * 
 */
@signalslot
public interface IItemStoneMix {
	void onStoneMixAddChange(int lvl, int num);
}

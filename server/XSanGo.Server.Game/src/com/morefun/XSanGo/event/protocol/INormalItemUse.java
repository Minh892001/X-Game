/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import com.morefun.XSanGo.item.NormalItem;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 非宝箱类物品使用事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface INormalItemUse {

	/**
	 * 非宝箱类物品被使用
	 * 
	 * @param item
	 *            物品对象
	 * @param count
	 *            请求使用数量
	 * @param realCount
	 *            实际使用数量
	 */
	void onItemUse(NormalItem item, int count, int realCount);

}

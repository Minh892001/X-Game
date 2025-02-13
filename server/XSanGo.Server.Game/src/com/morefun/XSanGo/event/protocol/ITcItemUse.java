/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import com.XSanGo.Protocol.ItemView;
import com.morefun.XSanGo.item.NormalItem;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 宝箱类物品使用事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface ITcItemUse {

	void onItemUse(NormalItem item, int count, ItemView[] items);

}

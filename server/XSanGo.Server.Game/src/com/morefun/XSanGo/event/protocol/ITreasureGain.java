package com.morefun.XSanGo.event.protocol;

import com.XSanGo.Protocol.ItemView;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 寻宝收获
 * 
 * @author lixiongming
 *
 */
@signalslot
public interface ITreasureGain {
	/**
	 * 寻宝收获
	 * 
	 * @param items
	 *            得到物品
	 *  @param addArray 各种加成参数
	 */
	public void onGain(ItemView[] items, String addArray);
}

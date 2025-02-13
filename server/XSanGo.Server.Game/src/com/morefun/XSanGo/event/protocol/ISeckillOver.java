package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 物品秒杀完事件
 * 
 * @author lixiongming
 *
 */
@signalslot
public interface ISeckillOver {
	/**
	 * 物品秒杀完事件
	 * 
	 * @param id
	 *            秒杀ID
	 * @param itemId 物品编号
	 */
	public void onSeckillOver(int id, String itemId);
}

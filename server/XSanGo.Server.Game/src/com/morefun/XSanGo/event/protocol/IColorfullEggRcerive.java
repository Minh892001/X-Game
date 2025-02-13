package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 彩蛋砸蛋领奖
 * 
 */
@signalslot
public interface IColorfullEggRcerive {
	/**
	 * 
	 * @param item 领取的物品
	 * @param num 领取的物品数量
	 */
	public void onReceive(String item, int num);
}

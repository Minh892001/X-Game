package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 彩蛋砸蛋
 * 
 */
@signalslot
public interface IColorfullEggBroken {

	/**
	 * 
	 * @param eggFlag 第几个彩蛋
	 * @param count 第几次砸彩蛋
	 * @param itemId 中奖物品配置id
	 */
	public void onBroken(int eggFlag, int count, String itemId);
}

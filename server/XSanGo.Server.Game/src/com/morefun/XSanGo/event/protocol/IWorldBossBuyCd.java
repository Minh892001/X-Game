package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 世界BOSS购买CD
 * 
 * @author xiongming.li
 */
@signalslot
public interface IWorldBossBuyCd {

	/**
	 * 世界BOSS购买CD
	 * @param yuanbao 花费元宝
	 */
	void onBuyCd(int yuanbao);
}

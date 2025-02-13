package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 世界BOSS购买鼓舞
 * 
 * @author xiongming.li
 */
@signalslot
public interface IWorldBossBuyInspire {

	/**
	 * 世界BOSS购买鼓舞
	 * @param yuanbao 花费元宝
	 */
	void onBuyInspire(int yuanbao);
}

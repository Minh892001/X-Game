package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;
/**
 * 购买副本互动次数
 * @author lixiongming
 *
 */
@signalslot
public interface IBuyHudong {
	/**
	 * @param buyCount   第几次购买
	 * @param yuanbao    花费元宝
	 */
	public void onBuyHudong(int buyCount,int yuanbao);
}

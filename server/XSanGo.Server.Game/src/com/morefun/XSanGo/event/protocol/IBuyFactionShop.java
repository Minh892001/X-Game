package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 购买公会商品
 * 
 * @author lixiongming
 *
 */
@signalslot
public interface IBuyFactionShop {
	/**
	 * 购买公会商品
	 * @param id 脚本ID
	 */
	public void onBuyFactionShop(int id);
}

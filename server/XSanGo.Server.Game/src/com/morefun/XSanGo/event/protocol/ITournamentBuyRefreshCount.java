package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 竞技场购买刷新次数
 * 
 * @author guofeng.qin
 */
@signalslot
public interface ITournamentBuyRefreshCount {
	/**
	 * 购买刷新次数
	 * 
	 * @param price  
	 *            消耗元宝数量
	 */
	void onBuyRefreshCount(int price);
}

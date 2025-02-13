package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 北伐, 商城刷新
 * @author qinguofeng
 */
@signalslot
public interface IAttackCastleRefreshStore {

	/**
	 * 北伐, 商城刷新
	 * 
	 * @param count 刷新次数
	 * @param price 花费的竞技币
	 * */
	void onRefreshStore(int count, int price);
}

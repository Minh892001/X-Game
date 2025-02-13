package com.morefun.XSanGo.event.protocol;

import com.XSanGo.Protocol.AttackCastleShopItemView;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 北伐, 商城兑换
 * @author qinguofeng
 */
@signalslot
public interface IAttackCastleExchange {

	/**
	 * 北伐, 商城兑换
	 * 
	 * @param item 兑换的物品
	 * */
	void onExchange(AttackCastleShopItemView item);
}

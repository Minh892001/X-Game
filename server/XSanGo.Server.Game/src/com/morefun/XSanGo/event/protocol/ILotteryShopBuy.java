package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 大富翁神秘商店购买事件
 * @author sunjie
 *
 */
@signalslot
public interface ILotteryShopBuy {
	
	/**
	 * 
	 * @param costType 货币类型
	 * @param cost 数量
	 * @param item 道具
	 * @param num 道具数量
	 */
	public void onBuy(String costType,int cost,String item,int num);
}
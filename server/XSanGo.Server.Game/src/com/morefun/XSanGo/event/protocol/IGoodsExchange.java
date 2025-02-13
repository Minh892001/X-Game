package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 物物对换
 */
@signalslot
public interface IGoodsExchange {
	/**
	 * 物品兑换
	 * @param templateId  兑换所得物品模版ID
	 * @param exchangeNo  兑换编号
	 * @param type		      兑换类型
	 * @param num		      兑换所得物品数量
	 */
	void onExchangeItem(String templateId, String exchangeNo, int type, int num);
}

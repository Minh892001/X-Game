package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 商城购物事件
 * 
 * @author lixiongming
 */
@signalslot
public interface IShopping {
	/**
	 * 购买商品事件方法
	 * @param shopId
	 * @param num    购买数量
	 * @param price  单价
	 * @param type    类型0-商品 1-礼包
	 * @throws Exception
	 */
	public void onShopping(String shopId, String templateId, int num, int price, int type)
			throws Exception;
}

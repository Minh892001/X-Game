/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.XSanGo.Protocol.CustomChargeParams;

/**
 * 充值成功事件
 * 
 * @author sulingyun
 * 
 */
@signalslot
public interface ICharge {
	/**
	 * @param params
	 *            充值参数
	 * @param returnYuanbao
	 *            返利赠送元宝
	 * @param orderId
	 *            订单id
	 * @param currency
	 *            货币类型
	 */
	void onCharge(CustomChargeParams params, int returnYuanbao, String orderId, String currency);
}

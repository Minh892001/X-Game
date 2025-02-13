/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import com.XSanGo.Protocol.CurrencyType;
import com.morefun.XSanGo.trader.TraderType;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 名将/商人召唤事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface ITraderCall {
	/**
	 * 商人/名将召唤
	 * 
	 * @param traderType
	 *            商人/名将
	 * @param currencyType
	 *            使用货币类型
	 */
	void onTraderCalled(TraderType traderType, CurrencyType currencyType);
}

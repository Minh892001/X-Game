/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.XSanGo.Protocol.BuyHeroResult;

/**
 * 元宝单次抽卡事件
 * 
 * @author sulingyun
 * 
 */
@signalslot
public interface IBuySingleWineByYuanbao {
	/**
	 * 元宝单次抽卡事件
	 * 
	 * @param result
	 * @param isFree
	 *            是否免费
	 */
	void onBuyWineByYuanbao(BuyHeroResult result, boolean isFree);
}

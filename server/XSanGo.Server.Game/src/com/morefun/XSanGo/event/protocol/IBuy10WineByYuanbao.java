/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import java.util.List;

import net.sf.signalslot_apt.annotations.signalslot;

import com.XSanGo.Protocol.BuyHeroResult;

/**
 * 元宝十连抽事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IBuy10WineByYuanbao {
	/**
	 * 元宝十连抽事件
	 * 
	 * @param list
	 *            抽卡结果
	 */
	void onBuy10WineByYuanbao(List<BuyHeroResult> list);
}

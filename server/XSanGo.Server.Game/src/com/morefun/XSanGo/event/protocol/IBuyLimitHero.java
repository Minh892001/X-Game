/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import java.util.List;

import com.XSanGo.Protocol.BuyHeroResult;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 购买限时武将，魂匣
 * 
 * @author lxm
 */
@signalslot
public interface IBuyLimitHero {
	/**
	 * 购买限时武将，魂匣
	 * @param results 购买结果
	 */
	void onBuyLimitHero(List<BuyHeroResult> results);
}

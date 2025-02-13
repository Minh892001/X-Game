/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import com.XSanGo.Protocol.HeroCallResult;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 挑战召唤来的名将事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IChallengeTraderHero {

	/**
	 * 挑战名将
	 * 
	 * @param callResult
	 */
	void onChallengeHeroTrader(HeroCallResult callResult);

}

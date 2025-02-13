/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 购买副本挑战次数事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IBuyChallengeChance {
	void onBuyChallengeChance(int copyId, int price);
}

package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 世界BOSS结束挑战
 * 
 * @author xiongming.li
 */
@signalslot
public interface IWorldBossEndChallenge {

	/**
	 * 世界BOSS结束挑战
	 * @param harm 打出的伤害
	 */
	void onEndChallenge(int harm);
}

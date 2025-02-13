package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 世界BOSS尾刀奖励
 * 
 * @author xiongming.li
 */
@signalslot
public interface IWorldBossTailAward {

	/**
	 * 世界BOSS尾刀奖励
	 * @param hp 百分比尾刀
	 */
	void onWorldBossTailAward(int hp);
}

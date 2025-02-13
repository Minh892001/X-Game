package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.morefun.XSanGo.sign.RandomRoulette;

/**
 * 签到抽奖事件
 *
 */
@signalslot
public interface IRoulette {
	/**
	 * @param times 已经抽奖的次数
	 * @param award 得到的奖励
	 */
	void onRoulette(int times, RandomRoulette award);
}

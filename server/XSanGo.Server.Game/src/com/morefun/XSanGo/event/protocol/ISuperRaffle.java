package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 超级转盘抽奖
 * @author xiaojun.zhang
 *
 */
@signalslot
public interface ISuperRaffle {
	/**
	 * 超级转盘中奖领取
	 * @param oldVal 抽奖前次数
	 * @param itemCode 物品模版id
	 * @param num 数量
	 * @param newVal 抽奖后剩余次数
	 */
	public void onAcceptRaffleReward(int oldVal, String itemCode,int num,int newVal);
}

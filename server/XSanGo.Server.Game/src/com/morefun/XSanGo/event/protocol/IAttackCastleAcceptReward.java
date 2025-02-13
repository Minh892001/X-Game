package com.morefun.XSanGo.event.protocol;

import com.XSanGo.Protocol.ItemView;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 北伐, 开启宝箱
 * @author qinguofeng
 */
@signalslot
public interface IAttackCastleAcceptReward {

	/**
	 * 北伐,开启宝箱
	 * 
	 * @param nodeId 关卡索引
	 * @param reputation 奖励声望
	 * @param rewards 奖励物品
	 */
	void onAcceptReward(int nodeId, int reputation, ItemView[] rewards);
}

package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 北伐, 结束关卡
 * @author qinguofeng
 */
@signalslot
public interface IAttackCastleEnd {

	/**
	 * 结束关卡
	 * @param nodeIndex 关卡索引
	 * @param heroCount 初始武将数
	 * @param heroRemain 剩余武将数
	 * @param star 星级
	 * */
	void onAttackCastleEnd(int nodeIndex, byte heroCount, byte heroRemain, int star);
}

package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 北伐, 开始关卡
 * @author qinguofeng
 */
@signalslot
public interface IAttackCastleBegin {
	/**
	 * 开始一个北伐关卡
	 * 
	 * @param nodeIndex 关卡索引
	 * @param roleId 对手ID
	 * */
	void onAttackCastleBegin(int nodeIndex, String roleId);
}

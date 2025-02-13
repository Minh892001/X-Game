package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 北伐, 重置北伐
 * @author qinguofeng
 */
@signalslot
public interface IAttackCastleReset {
	/**
	 * 重置北伐
	 * @param resetCount 重置次数
	 * */
	void onAttackCastleReset(int resetCount);
}

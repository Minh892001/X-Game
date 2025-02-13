package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 好友模块一键补签
 *
 */
@signalslot
public interface IAutoResign {
	/**
	 * @param resignedDay
	 *            补签了哪几天
	 * @param totalCost
	 *            一键补钱的总花费
	 */
	void resign(Integer[] resignedDay, int totalCost);
}

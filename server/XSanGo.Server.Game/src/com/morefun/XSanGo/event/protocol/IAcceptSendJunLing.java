package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 领取军令
 * 
 * @author qinguofeng
 */
@signalslot
public interface IAcceptSendJunLing {

	/**
	 * 领取军令事件
	 * @param junlingCount 军令
	 * @param rmbyCount    元宝
	 */
	void onAcceptSendJunLing(int junlingCount, int rmbyCount);
}

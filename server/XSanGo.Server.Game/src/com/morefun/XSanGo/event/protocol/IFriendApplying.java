package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 好友模块申请好友的操作
 *
 */
@signalslot
public interface IFriendApplying {
	/**
	 * @param target
	 *            申请的对象
	 */
	void onApplyingHappend(String target);
}

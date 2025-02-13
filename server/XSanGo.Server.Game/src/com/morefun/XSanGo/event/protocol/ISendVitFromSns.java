package com.morefun.XSanGo.event.protocol;

import java.util.Collection;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 好友模块一键赠送行动力
 *
 */
@signalslot
public interface ISendVitFromSns {
	/**
	 * @param targetRoleIdCollection
	 *            目标玩家集合
	 */
	void onSend(Collection<String> targetRoleIdCollection);
}

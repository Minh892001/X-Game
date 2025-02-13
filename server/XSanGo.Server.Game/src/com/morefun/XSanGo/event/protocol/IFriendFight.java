package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 好友切磋
 */
@signalslot
public interface IFriendFight {
	/**
	 *  好友切磋
	 * @param targetId  目标ID
	 * @param resFlag   结果 0-失败 1-胜利
	 */
	void onFriendFight(String targetId, int resFlag);
}

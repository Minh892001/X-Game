package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 友情点增加
 * 
 * @author guofeng.qin
 */
@signalslot
public interface ISnsFriendPoint {
	/**
	 * 友情点增加
	 * 
	 * @param targetId
	 *            对方ID
	 * @param addNum
	 *            增加的数量
	 * @param currentNum
	 *            当前友情点数量
	 * */
	void onAddFriendPoint(String targetId, int addNum, int currentNum);
}

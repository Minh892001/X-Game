package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;
/**
 * 寻宝增加队伍
 * 
 * @author lixiongming
 *
 */
@signalslot
public interface ITreasureAddGroup {
	/**
	 * 寻宝增加队伍
	 * 
	 * @param currentGroupNum
	 *            当前队伍数量
	 */
	public void onAddGroup(int currentGroupNum);
}

package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;
/**
 * 时空战役开始事件
 * 
 * @author lixiongming
 *
 */
@signalslot
public interface ITimeBattleBegin {
	/**
	 * 时空战役开始
	 * @param id
	 * @param isClear 是否扫荡
	 * @param junling 消耗军令
	 */
	public void onBattleBegin(int id, boolean isClear, int junling);
}

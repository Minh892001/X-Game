package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;
/**
 * 时空战役事件
 * 
 * @author lixiongming
 *
 */
@signalslot
public interface ITimeBattle {
	/**
	 * 
	 * @param id
	 * @param isClear 是否扫荡
	 * @param junling 消耗军令
	 */
	public void onPassBattle(int id, boolean isClear, int junling);
}

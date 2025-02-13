package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 竞技场每日首胜
 * 
 * @author guofeng.qin
 */
@signalslot
public interface IArenaFirstWin {
	/**
	 * 竞技场每日首胜
	 * */
	void onFirstWin();
}

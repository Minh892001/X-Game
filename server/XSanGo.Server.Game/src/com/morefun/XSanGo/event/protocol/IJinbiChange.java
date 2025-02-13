package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 游戏币数量变化
 * 
 * @author lvmingtao
 */

@signalslot
public interface IJinbiChange {
	void onJinbiChange(long change) throws Exception;
}

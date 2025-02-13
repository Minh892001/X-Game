package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 随机PK认怂事件
 * 
 * @author zhangwei02.zhang
 * @since 2015年11月10日
 * @version 1.0
 */
@signalslot
public interface IWarmupEscape {

	/**
	 * 认怂
	 * 
	 */
	public void onEscape();
}

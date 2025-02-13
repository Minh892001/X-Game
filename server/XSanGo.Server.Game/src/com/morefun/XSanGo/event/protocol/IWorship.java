package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 膜拜事件
 * 
 * @author lixiongming
 *
 */
@signalslot
public interface IWorship {
	/**
	 * @param roleId 被膜拜者ID
	 * @param count  被膜拜总次数
	 */
	public void onWorship(String roleId, int count);
}

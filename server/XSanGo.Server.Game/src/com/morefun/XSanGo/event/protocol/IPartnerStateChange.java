package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 伙伴阵位重置战力变更事件
 * @author xiaojun.zhang
 *
 */
@signalslot
public interface IPartnerStateChange {

	/**
	 * 伙伴位置重置
	 */
	void onPartnerStateChange();
}

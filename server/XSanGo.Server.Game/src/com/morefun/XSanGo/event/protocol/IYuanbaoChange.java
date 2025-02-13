/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 元宝变更事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IYuanbaoChange {

	/**
	 * 元宝变更通知
	 * 
	 * @param oldBind
	 *            变更前的绑定元宝数量
	 * @param oldUnbind
	 *            变更前的非绑定元宝数量
	 * @param newBind
	 *            变更后的绑定元宝数量
	 * @param newUnbind
	 *            变更后的非绑定元宝数量
	 * @param change
	 *            变更值
	 */
	void onYuanbaoChange(int oldBind, int oldUnbind, int newBind, int newUnbind, int change);

}

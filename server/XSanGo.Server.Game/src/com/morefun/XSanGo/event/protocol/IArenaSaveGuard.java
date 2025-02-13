/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 竞技场 保存防守部队
 * 
 * @author lvmingtao
 */

@signalslot
public interface IArenaSaveGuard {
	/**
	 * 
	 * @param guardId
	 *            保存防守部队ID
	 */
	void onSave(String guardId);
}

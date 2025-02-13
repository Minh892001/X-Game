/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 公会战报名
 * 
 * @author lxm
 */
@signalslot
public interface IFactionApply {
	/**
	 * 公会战报名
	 */
	void onApply();
}

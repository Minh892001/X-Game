/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 公会战复活
 * 
 * @author lxm
 */
@signalslot
public interface IFactionGvgRevive {
	/**
	 * 公会战复活
	 * @param money 费用
	 */
	void onFactionGvgRevive(int money);
}

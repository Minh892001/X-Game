/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 公会战战斗结束
 * 
 * @author lxm
 */
@signalslot
public interface IFactionGvgEnd {
	/**
	 * 公会战战斗结束
	 * @param isWin    是否胜利
	 * @param addHonor 增加荣誉
	 */
	void onFactionGvgEnd(boolean isWin,int addHonor);
}

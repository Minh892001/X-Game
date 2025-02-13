/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.morefun.XSanGo.copy.SmallCopyT;

/**
 * 副本通关事件定义
 * 
 * @author BruceSu
 * 
 */
@signalslot
public interface ICopyCompleted {
	
	/**
	 * 副本通关
	 * @param templete
	 * @param star
	 * @param firstPass
	 * @param fightPower
	 * @param junling 
	 */
	void onCopyCompleted(SmallCopyT templete, int star, boolean firstPass, int fightPower, int junling);
}

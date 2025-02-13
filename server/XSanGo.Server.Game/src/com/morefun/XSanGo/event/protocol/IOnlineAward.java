/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.XSanGo.Protocol.ItemView;

/**
 * 在线礼包事件
 * 
 * @author lvmingtao
 */

@signalslot
public interface IOnlineAward {
	/**
	 * 
	 * @param itemView 在线的礼包的物品
	 * @throws Exception
	 */
	void onOnlineAward(int onlineGiftId, ItemView[] itemView) throws Exception;
}

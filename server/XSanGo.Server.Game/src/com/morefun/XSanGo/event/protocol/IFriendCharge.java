/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import com.morefun.XSanGo.vip.ChargeItemT;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 朋友充值成功事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IFriendCharge {

	/**
	 * @param template
	 *            充值选项
	 * @param friendAccount
	 */
	void onChargeFromFriend(ChargeItemT template, String friendAccount);

}

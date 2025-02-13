/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * VIP礼包购买事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IVipGiftBuy {

	void onVipGiftBuy(int vipLevel, String reward, int num);

}

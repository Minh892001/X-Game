/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 拍卖币变更事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IAuctionMoneyChange {
	void onAuctionMoneyChange(long change);
}

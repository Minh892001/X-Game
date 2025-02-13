/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import com.XSanGo.Protocol.ItemView;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 领取名将奖励事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IAcceptHeroGift {

	void onReceiveHeroGift(ItemView[] items);

}

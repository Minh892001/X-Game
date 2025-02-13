package com.morefun.XSanGo.event.protocol;

import com.morefun.XSanGo.activity.UpGiftT;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 领取升级礼包
 */
@signalslot
public interface ILevelGift {
	void onGetGift(UpGiftT upGiftT) throws Exception;
}

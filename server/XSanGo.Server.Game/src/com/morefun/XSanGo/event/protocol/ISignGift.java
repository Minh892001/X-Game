package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.XSanGo.Protocol.ItemView;

/**
 * 签到模块累计签到
 *
 */
@signalslot
public interface ISignGift {
	/**
	 * @param id
	 *            奖励配置对应的id
	 * @param count
	 *            奖励对应的次数
	 * @param items
	 *            奖励道具
	 */
	void onGift(String id, int count, ItemView[] items);
}

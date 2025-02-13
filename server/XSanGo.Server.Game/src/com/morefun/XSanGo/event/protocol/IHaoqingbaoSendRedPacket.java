package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 豪情宝发红包
 * 
 * @author guofeng.qin
 */
@signalslot
public interface IHaoqingbaoSendRedPacket {
	/**
	 * 发红包
	 * 
	 * @param type
	 *            类型，1，工会；2，好友；3，全服,
	 * @param total
	 *            总金额
	 * @param num
	 *            个数
	 * @param id
	 *            红包id
	 * @param before
	 *            发红包之前的金额
	 * @param after
	 *            发送后的红包金额
	 */
	void onSendRedPacket(int type, int total, int num, String id, int before, int after);
}

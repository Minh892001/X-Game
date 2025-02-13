package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 豪情宝收红包
 * 
 * @author guofeng.qin
 */
@signalslot
public interface IHaoqingbaoRecvRedPacket {
	/**
	 * 抢红包
	 * 
	 * @param type
	 *            类型，1，工会；2，好友；3，全服,
	 * @param senderId
	 *            发送者
	 * @param num
	 *            抢到的金额
	 * @param redpacketID
	 *            红包ID
	 * @param recvId
	 *            接收者id
	 * @param money
	 *            抢完玩家余额
	 * @param lastNum
	 *            红包剩余数量
	 */
	void onRecvRedPacket(int type, String senderId, int num, String redpacketID, String recvId, int money, int lastNum);
}

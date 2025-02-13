package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 豪情宝充值
 * 
 * @author guofeng.qin
 */
@signalslot
public interface IHaoqingbaoCharge {
	/**
	 * 豪情宝充值
	 * 
	 * @param num 充值元宝数量
	 * */
	void onCharge(int num);
}

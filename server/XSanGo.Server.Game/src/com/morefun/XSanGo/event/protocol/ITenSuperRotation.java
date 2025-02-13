package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;
/**
 * 每抽奖十次获得奖励
 * @author xiaojun.zhang
 *
 */
@signalslot
public interface ITenSuperRotation {
	
	/**
	 * 
	 * @param num 刷新次数
	 * @param itemStr 刷新得到的物品
	 */
	void onRefresh(int num, String itemStr);
}

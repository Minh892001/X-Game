package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 秒杀活动
 * 
 * @author lixiongming
 *
 */
@signalslot
public interface ISeckill {
	/**
	 * 秒杀活动
	 * @param id   秒杀ID
	 * @param yuanbao 花费元宝
	 */
	public void onSeckill(int id, int yuanbao);
}

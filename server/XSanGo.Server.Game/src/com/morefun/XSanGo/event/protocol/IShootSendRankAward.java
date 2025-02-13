package com.morefun.XSanGo.event.protocol;

import java.util.Map;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 百步穿杨积分排名发奖的名单
 * @author lixiongming
 *
 */
@signalslot
public interface IShootSendRankAward {
	/**
	 * 
	 * @param serverId 服务器id
	 * @param score 最终积分
	 * @param rank 排名
	 * @param itemsMap 道具id,数量
	 */
	public void onSendRankAward(int serverId, int score, int rank, Map<String, Integer> itemsMap);
}

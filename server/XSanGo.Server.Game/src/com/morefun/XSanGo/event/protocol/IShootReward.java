/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import java.util.Map;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 百步穿杨射击事件
 * 
 * @author zhouming
 * 
 */
@signalslot
public interface IShootReward {

	/**
	 * 射击事件
	 * @param systemType
	 * @param shootType 射击类型(1单射，2十连)
	 * @param isFree 是否免费
	 * @param dayCnt 当日次数(十连当日次数为0)
	 * @param totalCnt 总次数
	 * @param itemsMap 获取的物品列表
	 * @param isHide 是否隐藏
	 */
	void onShoot(int systemType, int shootType, boolean isFree, int dayCnt, int totalCnt,
			Map<String, Integer> itemsMap, boolean isHide);
}

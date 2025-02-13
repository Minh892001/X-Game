/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IDreamlandSweep
 * 功能描述：
 * 文件名：IDreamlandSweep.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 南华幻境扫荡事件
 * 
 * @author weiyi.zhao
 * @since 2016-4-25
 * @version 1.0
 */
@signalslot
public interface IDreamlandSweep {

	/**
	 * 扫荡事件
	 * 
	 * @param sceneId 关卡编号
	 * @param challengeNum 已挑战次数
	 * @param items 奖励
	 */
	void onSweepDreamland(int sceneId, int challengeNum, String items);
}

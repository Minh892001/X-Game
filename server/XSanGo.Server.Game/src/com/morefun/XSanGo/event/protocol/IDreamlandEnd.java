/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IDreamlandEnd
 * 功能描述：
 * 文件名：IDreamlandEnd.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 南华幻境战斗结束
 * 
 * @author weiyi.zhao
 * @since 2016-4-22
 * @version 1.0
 */
@signalslot
public interface IDreamlandEnd {

	/**
	 * 战斗结束事件
	 * 
	 * @param sceneId 关卡编号
	 * @param heroCount 初始参与武将数
	 * @param remainCount 剩余武将数
	 * @param star 星级
	 * @param challengeNum 已挑战次数
	 * @param items 奖励
	 */
	void onEndDreamland(int sceneId, int heroCount, int remainCount, int star, int challengeNum, String items);
}

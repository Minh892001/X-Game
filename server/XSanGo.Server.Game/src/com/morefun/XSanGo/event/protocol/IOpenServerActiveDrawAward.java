/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IOpenServerActiveDrawAward
 * 功能描述：
 * 文件名：IOpenServerActiveDrawAward.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 开服活动领奖事件
 * 
 * @author weiyi.zhao
 * @since 2016-3-8
 * @version 1.0
 */
@signalslot
public interface IOpenServerActiveDrawAward {

	/**
	 * 领奖事件
	 * 
	 * @param activeId 活动ID
	 * @param nodeId 节点ID
	 * @param items 奖励内容
	 */
	void onDraw(int activeId, int nodeId, String items);
}

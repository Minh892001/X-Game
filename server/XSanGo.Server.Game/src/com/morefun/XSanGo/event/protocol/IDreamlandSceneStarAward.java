/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IDreamlandSceneStarAward
 * 功能描述：
 * 文件名：IDreamlandSceneStarAward.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 南华幻境通关星级奖励领取
 * 
 * @author weiyi.zhao
 * @since 2016-4-23
 * @version 1.0
 */
@signalslot
public interface IDreamlandSceneStarAward {

	/**
	 * 领取通关星级奖励事件
	 * 
	 * @param sceneId 关卡编号
	 * @param star 星级
	 * @param items 奖励
	 */
	void onDrawSceneStarAward(int sceneId, int star, String items);
}

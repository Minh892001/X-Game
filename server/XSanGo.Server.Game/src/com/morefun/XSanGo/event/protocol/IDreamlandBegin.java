/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IDreamlandBegin
 * 功能描述：
 * 文件名：IDreamlandBegin.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 南华幻境战斗开始
 * 
 * @author weiyi.zhao
 * @since 2016-4-22
 * @version 1.0
 */
@signalslot
public interface IDreamlandBegin {

	/**
	 * 战斗开始事件
	 * 
	 * @param sceneId 关卡编号
	 * @param fightMovieIdContext 战报编号
	 */
	void onBeginDreamland(int sceneId, String fightMovieIdContext);
}

/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IDreamlandRefreshScene
 * 功能描述：
 * 文件名：IDreamlandRefreshScene.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 南华幻境关卡重置
 * 
 * @author weiyi.zhao
 * @since 2016-4-27
 * @version 1.0
 */
@signalslot
public interface IDreamlandRefreshScene {

	/**
	 * 定点重置关卡事件
	 * 
	 * @param oldSceneId 重置之前关卡
	 * @param lastRefreshTime 上次关卡刷新时间
	 */
	void onRefreshScene(int oldSceneId, String lastRefreshTime);
}

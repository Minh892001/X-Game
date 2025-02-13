/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IHeroAwaken
 * 功能描述：
 * 文件名：IHeroAwaken.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 武将觉醒事件
 * 
 * @author zwy
 * @since 2015-11-17
 * @version 1.0
 */
@signalslot
public interface IHeroAwaken {

	/**
	 * 武将觉醒事件
	 * 
	 * @param templeId 武将模板ID
	 * @param star 觉醒星级
	 * @param isAwaken 是否觉醒
	 */
	void onHeroAwaken(int templeId, int star, boolean isAwaken);
}

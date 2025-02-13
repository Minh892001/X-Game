/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IDreamlandStarAward
 * 功能描述：
 * 文件名：IDreamlandStarAward.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 星数奖励领取
 * 
 * @author weiyi.zhao
 * @since 2016-4-23
 * @version 1.0
 */
@signalslot
public interface IDreamlandStarAward {

	/**
	 * 领取星数奖励事件
	 * 
	 * @param star 星级
	 * @param items 奖励
	 */
	void onDrawStarAward(int star, String items);
}

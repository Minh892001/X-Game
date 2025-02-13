/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IFactionBattleBuyMarchCool
 * 功能描述：
 * 文件名：IFactionBattleBuyMarchCool.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 购买行军冷却时间事件
 * 
 * @author zwy
 * @since 2016-1-13
 * @version 1.0
 */
@signalslot
public interface IFactionBattleBuyMarchCool {

	/**
	 * 购买行军冷却
	 * 
	 * @param cdNum 次数
	 * @param coolingEndTime 原到期时间
	 */
	void onBuyMarchCooling(int cdNum, String coolingEndTime);
}

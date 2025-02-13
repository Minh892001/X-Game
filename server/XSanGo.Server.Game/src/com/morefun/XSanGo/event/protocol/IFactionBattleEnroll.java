/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IFactionBattleEnroll
 * 功能描述：
 * 文件名：IFactionBattleEnroll.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 公会战报名事件
 * 
 * @author zwy
 * @since 2016-1-7
 * @version 1.0
 */
@signalslot
public interface IFactionBattleEnroll {

	/**
	 * 报名事件
	 * 
	 * @param campId 阵营编号（据点）
	 * @param dutyId 职位
	 * @param factionId 报名人所在公会编号
	 */
	void onEnroll(int campId, int dutyId, String factionId);
}

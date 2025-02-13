/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IFactionBattleChangeCamp
 * 功能描述：
 * 文件名：IFactionBattleChangeCamp.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 公会战阵营变更事件
 * 
 * @author zwy
 * @since 2016-1-7
 * @version 1.0
 */
@signalslot
public interface IFactionBattleChangeCamp {

	/**
	 * 改变阵营事件
	 * 
	 * @param campId 阵营编号（据点）
	 * @param dutyId 职位
	 */
	void onChangeCamp(int campId, int dutyId);
}

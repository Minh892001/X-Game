/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IFactionBattleMarch
 * 功能描述：
 * 文件名：IFactionBattleMarch.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 行军事件
 * 
 * @author zwy
 * @since 2016-1-13
 * @version 1.0
 */
@signalslot
public interface IFactionBattleMarch {

	/**
	 * 据点行军
	 * 
	 * @param oldStrongHoldId 行军之前据点编号
	 * @param strongholdId 目的地据点编号
	 * @param isUseKit 是否使用锦囊：日行千里
	 */
	void onMarch(int oldStrongHoldId, int strongholdId, boolean isUseKit);
}

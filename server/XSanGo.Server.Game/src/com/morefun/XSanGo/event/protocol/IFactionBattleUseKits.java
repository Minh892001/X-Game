/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IFactionBattleUseKits
 * 功能描述：
 * 文件名：IFactionBattleUseKits.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 锦囊使用事件
 * 
 * @author zwy
 * @since 2016-1-25
 * @version 1.0
 */
@signalslot
public interface IFactionBattleUseKits {

	/**
	 * 使用锦囊
	 * 
	 * @param kitsId 锦囊编号
	 */
	void onUseKits(int kitsId);
}

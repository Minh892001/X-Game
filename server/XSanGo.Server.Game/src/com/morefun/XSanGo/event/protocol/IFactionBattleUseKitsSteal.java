/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IFactionBattleUseKitsSteal
 * 功能描述：
 * 文件名：IFactionBattleUseKitsSteal.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 使用锦囊：顺手牵羊
 * 
 * @author zwy
 * @since 2016-1-25
 * @version 1.0
 */
@signalslot
public interface IFactionBattleUseKitsSteal {

	/**
	 * 顺手牵羊
	 * 
	 * @param isFull 是否锦囊袋已满
	 * @param targetRoleId 目标玩家
	 * @param targetKitsId 锦囊
	 */
	void onSteal(boolean isFull, String targetRoleId, int targetKitsId);
}

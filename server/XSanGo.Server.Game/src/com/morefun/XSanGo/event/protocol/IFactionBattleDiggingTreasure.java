/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IFactionBattleDiggingTreasure
 * 功能描述：
 * 文件名：IFactionBattleDiggingTreasure.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.XSanGo.Protocol.IntString;

/**
 * 挖宝事件
 * 
 * @author zwy
 * @since 2016-1-13
 * @version 1.0
 */
@signalslot
public interface IFactionBattleDiggingTreasure {

	/**
	 * 挖宝
	 * 
	 * @param strongholdId 据点
	 * @param forage 粮草
	 * @param views 物品（包含特殊事件产出的物品）
	 */
	void onDiggingTreasure(int strongholdId, int forage, IntString[] views);
}

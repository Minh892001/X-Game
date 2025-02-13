/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IFactionBattleEnter
 * 功能描述：
 * 文件名：IFactionBattleEnter.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 公会战参战事件
 * 
 * @author zwy
 * @since 2016-1-13
 * @version 1.0
 */
@signalslot
public interface IFactionBattleEnter {

	/**
	 * 进入事件
	 * 
	 * @param strongholdId 据点编号
	 * @param gainKitsId 获取的锦囊编号
	 */
	void onEnter(int strongholdId, int gainKitsId);
}

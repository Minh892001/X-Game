/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IFactionBattleLeave
 * 功能描述：
 * 文件名：IFactionBattleLeave.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 公会战离开战场事件
 * 
 * @author zwy
 * @since 2016-1-13
 * @version 1.0
 */
@signalslot
public interface IFactionBattleLeave {

	/**
	 * 离开事件
	 * 
	 * @param strongholdId 据点
	 */
	void onLeave(int strongholdId);
}

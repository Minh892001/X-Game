/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 竞技场 复仇 事件
 * 
 * @author lvmingtao
 */

@signalslot
public interface IArenaRevenge {
	/**
	 * @param resFlag
	 *            战斗结果
	 * @param roleRank
	 *            角色等级
	 * @param rivalRank
	 *            对手等级
	 */
	void onFight(int resFlag, int roleRank, int rivalRank);
}

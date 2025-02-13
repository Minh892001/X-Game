/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 竞技场战斗
 * 
 * @author lvmingtao
 */

@signalslot
public interface IArenaFight {
	/**
	 * @param resFlag
	 *            战斗结果
	 * @param fromRank
	 *            之前排名
	 * @param toRank
	 *            现在排名
	 * @param sneerId
	 *            嘲讽ID
	 * @param reward
	 *            战斗的奖励 JSON格式，参考邮件格式
	 */
	void onArenaFight(int resFlag, int fromRank, int toRank, int sneerId, String reward);
}

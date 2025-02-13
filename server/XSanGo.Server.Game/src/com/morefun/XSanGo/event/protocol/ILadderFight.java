/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 群雄争霸 战斗
 * 
 * @author lvmingtao
 */

@signalslot
public interface ILadderFight {
	/**
	 * @param fightStar 对手ID
	 * @param resFlag 战斗结果
	 * @param fightStar 战斗结果星级
	 */
	void onFight(String rivalId, int resFlag, int fightStar);
}

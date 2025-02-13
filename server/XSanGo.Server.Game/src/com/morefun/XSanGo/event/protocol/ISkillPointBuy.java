/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import com.morefun.XSanGo.hero.BuyHeroSkillT;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 购买武将技能点事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface ISkillPointBuy {

	/**
	 * 购买武将技能点
	 * 
	 * @param bhst
	 *            购买次数配置
	 * @param newPoint
	 *            最新点数
	 */
	void onSkillPointBuy(BuyHeroSkillT bhst, int newPoint);

}

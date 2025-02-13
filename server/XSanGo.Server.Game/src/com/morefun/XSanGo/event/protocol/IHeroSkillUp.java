/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import com.morefun.XSanGo.hero.IHero;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 武将技能升级事件定义
 * 
 * @author lvmingtao
 */

@signalslot
public interface IHeroSkillUp {
	/**
	 * 
	 * @param hero
	 * @param name 技能名字
	 * @param oldLevel 升级前等级
	 * @param newLevel 升级后等级
	 */
	void onHeroSkillUp(IHero hero, String name, int oldLevel, int newLevel);
}

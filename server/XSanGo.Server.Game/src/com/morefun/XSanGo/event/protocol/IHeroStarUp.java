/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.morefun.XSanGo.hero.IHero;

/**
 * 武将的星级
 * 
 * @author lvmingtao
 */

@signalslot
public interface IHeroStarUp {

	/**
	 * 武将升星事件
	 * 
	 * @param hero
	 * @param beforeStar
	 */
	void onHeroStarUp(IHero hero, int beforeStar);
}

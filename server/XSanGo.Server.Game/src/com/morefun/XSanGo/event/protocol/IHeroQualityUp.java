/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import com.morefun.XSanGo.hero.IHero;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 武将进阶事件
 * 
 * @author sulingyun
 * 
 */
@signalslot
public interface IHeroQualityUp {

	/**
	 * 武将进阶事件
	 * 
	 * @param hero
	 * @param beforeQualityLevel 进阶之前等级
	 */
	void onHeroQualityUp(IHero hero, int beforeQualityLevel) ;
}

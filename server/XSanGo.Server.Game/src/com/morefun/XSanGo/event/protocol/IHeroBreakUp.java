/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import com.morefun.XSanGo.hero.IHero;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 武将突破事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IHeroBreakUp {

	/**
	 * 武将突破
	 * 
	 * @param hero
	 * @param beforeBreakLevel 突破之前突破等级
	 */
	void onHeroBreakUp(IHero hero, int beforeBreakLevel);

}

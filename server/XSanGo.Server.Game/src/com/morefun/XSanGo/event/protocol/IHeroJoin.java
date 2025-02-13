/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.morefun.XSanGo.common.HeroSource;
import com.morefun.XSanGo.hero.IHero;

/**
 * 武将增加事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IHeroJoin {
	void onHeroJoin(IHero hero, HeroSource source);
}

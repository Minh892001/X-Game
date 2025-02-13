/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.morefun.XSanGo.hero.IHero;

/**
 * 使用武将道具事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IHeroUseItem {
	void onItemUseForHero(String itemTemplate, int count, IHero hero);
}

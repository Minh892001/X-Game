/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.morefun.XSanGo.equip.EquipItem;
import com.morefun.XSanGo.hero.IHero;

/**
 * 武将装备变更事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IHeroEquipChange {

	void onHeroEquipChange(IHero hero, EquipItem equip);

}

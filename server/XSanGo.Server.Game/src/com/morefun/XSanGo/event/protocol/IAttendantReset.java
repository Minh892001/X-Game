package com.morefun.XSanGo.event.protocol;

import com.morefun.XSanGo.hero.IHero;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 重置武将随从
 * 
 */
@signalslot
public interface IAttendantReset {
	/**
	 * 武将随从重置
	 * @param hero 当前武将
	 * @param pos 指定随从位置
	 * @param attendantItemid 特殊随从模版id
	 */
	void onAttendantReset(IHero hero, byte pos, int attendantItemid);
}

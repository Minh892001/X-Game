/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import com.morefun.XSanGo.hero.IHero;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 随从变更事件
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IAttendantChange {

	/**
	 * 随从变更时触发
	 * 
	 * @param hero
	 *            携带随从的武将
	 * @param pos
	 *            随从位置
	 * @param attendant
	 *            随从武将，可为NULL
	 */
	void onAttendantChange(IHero hero, byte pos, IHero attendant);

}

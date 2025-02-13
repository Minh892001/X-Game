/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.morefun.XSanGo.hero.IHero;

/**
 * 武将缘分变更
 * 
 * @author sulingyun
 *
 */
@signalslot
public interface IHeroRelationChange {
	/**
	 * @param hero
	 * @param orignalRelationId
	 *            缘分ID
	 * @param oldLevel
	 *            原来等级
	 * @param level
	 *            新等级
	 */
	void onRelationChange(IHero hero, int orignalRelationId, int oldLevel,
			int level);
}

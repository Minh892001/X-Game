package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 武将传承
 * 
 * @author guofeng.qin
 */
@signalslot
public interface IHeroInherit {
	/**
	 * 武将传承
	 * 
	 * @param baseHero
	 *            基础武将
	 * @param inheritHero
	 *            传承武将
	 * */
	void onHeroInherit(int baseHero, int inheritHero);
}

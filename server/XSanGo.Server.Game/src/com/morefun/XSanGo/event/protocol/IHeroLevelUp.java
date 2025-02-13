package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 武将升级
 * 
 * @author guofeng.qin
 */
@signalslot
public interface IHeroLevelUp {
	/**
	 * 武将升级
	 * 
	 * @param tempId
	 *            武将模版ID
	 * @param lvl
	 *            升到的等级
	 * */
	void onHeroLevelUp(int tempId, int lvl);
}

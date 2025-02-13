package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 竞技币变更
 * 
 * @author guofeng.qin
 */
@signalslot
public interface IChangeChallengeMoney {

	/**
	 * 竞技币变更
	 * 
	 * @param before
	 *            变更前
	 * @param after
	 *            变更后
	 * @param change
	 *            变更值
	 */
	void onChange(int before, int after, int change);
}

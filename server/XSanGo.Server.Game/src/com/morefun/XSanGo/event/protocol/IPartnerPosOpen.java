package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 开启伙伴位置
 * @author xiaojun.zhang
 *
 */
@signalslot
public interface IPartnerPosOpen {
	/**
	 * 伙伴位置开启
	 * @param pos 位置
	 * @param id 对应属性加成模版ID
	 * @param specialHeroCode 专属武将模版id
	 */
	void onPartnerPositionChange(int pos, int id, String specialHeroCode);
}

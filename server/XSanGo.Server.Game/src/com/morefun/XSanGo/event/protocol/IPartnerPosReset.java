package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;
/**
 * 伙伴阵位重置
 * @author xiaojun.zhang
 *
 */
@signalslot
public interface IPartnerPosReset {

	/**
	 * 伙伴位置重置
	 * @param pos 位置
	 * @param id 对应属性加成模版ID
	 * @param specialHeroCode 专属武将模版id
	 * @param costNum 消耗缘份丹数量
	 */
	void onPartnerPositionReset(int pos, int id, String specialHeroCode, int costNum);
}

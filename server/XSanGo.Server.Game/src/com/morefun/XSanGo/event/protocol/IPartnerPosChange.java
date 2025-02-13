package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.partner.IPartner;
/**
 * 伙伴阵位武将变更事件
 * @author xiaojun.zhang
 *
 */
@signalslot
public interface IPartnerPosChange {

	void onHeroPositionChange(IPartner partner, int pos, IHero hero);
	
}

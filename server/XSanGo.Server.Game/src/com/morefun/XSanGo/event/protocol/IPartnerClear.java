package com.morefun.XSanGo.event.protocol;

import com.morefun.XSanGo.partner.IPartner;

import net.sf.signalslot_apt.annotations.signalslot;
/**
 * 清空伙伴武将位置
 * @author xiaojun.zhang
 *
 */
@signalslot
public interface IPartnerClear {
	
	void onPartnerClear(IPartner partner);
	
}

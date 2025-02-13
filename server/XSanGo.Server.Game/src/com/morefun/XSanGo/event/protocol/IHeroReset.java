package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.XSanGo.Protocol.HeroConsumeView;
import com.XSanGo.Protocol.HeroView;

/**
 * 武将下野事件
 * @author qinguofeng
 */
@signalslot
public interface IHeroReset {
	void onHeroReset(String heroId, int release, HeroView originView, HeroConsumeView consumeBack);
}

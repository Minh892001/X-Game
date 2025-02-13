package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 名将召唤，召唤
 * @author qinguofeng
 */
@signalslot
public interface ICollectHeroSoulDoCollect {
    void onDoCollectHeroSoul(int type, int costType, int costNum, String soul);
}

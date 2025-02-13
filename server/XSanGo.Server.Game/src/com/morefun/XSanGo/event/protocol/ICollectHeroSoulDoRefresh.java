package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 名将召唤，刷新
 * @author qinguofeng
 */
@signalslot
public interface ICollectHeroSoulDoRefresh {
    void onDoRefresh(int type, int cost);
}

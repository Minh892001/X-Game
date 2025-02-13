package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 一键购买副本挑战次数
 * 
 * @author qinguofeng
 */
@signalslot
public interface IBuyChapterChallengeChance {
	void onBuyChapterChallengeChance(int chapterId, int price);
}

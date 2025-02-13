package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 寻宝 救援好友
 * 
 * @author zhuzhi.yang
 */
@signalslot
public interface ITreasureHelpFriend {
	
	/**
	 * 救援好友
	 * */
	void onTreasureHelpFriend();
}

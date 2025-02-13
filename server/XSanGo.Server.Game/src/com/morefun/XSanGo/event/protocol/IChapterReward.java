/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 领取副本章节通关奖励
 * @author sulingyun
 *
 */
@signalslot
public interface IChapterReward {
	/**
	 * 领取副本章节通关奖励
	 * @param chapterId 章节ID
	 * @param level     奖励等级1(十星),2(20星),3(满星)
	 * @param tcCode    奖励TC  
	 */
	void onGetChapterReward(int chapterId, int level, String tcCode);

}

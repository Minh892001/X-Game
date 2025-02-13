/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 群雄争霸等级变更
 * 
 * @author lxm
 */

@signalslot
public interface ILadderLevelChange {
	/**
	 * @param level 当前等级
	 */
	void onLevelChange(int level);
}

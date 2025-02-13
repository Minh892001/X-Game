/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 名将仰慕 招募武将
 * 
 * @author lvmingtao
 */

@signalslot
public interface IHeroAdmireSummon {
	/**
	 * @param heroId 武将ID
	 */
	void onSummon(int heroId);
}

/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 名将仰慕 选择仰慕的武将
 * 
 * @author lvmingtao
 */

@signalslot
public interface IHeroAdmireChoose {
	/**
	 * @param heroId 选择武将的模板ID
	 */
	void onChoose(int heroId);
}

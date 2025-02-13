/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IHeroBaptizeUpgrade
 * 功能描述：
 * 文件名：IHeroBaptizeUpgrade.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 武将洗练等级升级事件接口
 * 
 * @author zwy
 * @since 2015-11-18
 * @version 1.0
 */
@signalslot
public interface IHeroBaptizeUpgrade {

	/**
	 * 武将洗练等级升级事件
	 * 
	 * @param heroId
	 * @param lvl
	 */
	void onBaptizeUpgrade(String heroId, int lvl);
}

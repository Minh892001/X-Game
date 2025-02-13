/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IHeroPolishDivisionUpgrade
 * 功能描述：
 * 文件名：IHeroPolishDivisionUpgrade.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 洗练师升级事件
 * 
 * @author zwy
 * @since 2015-11-18
 * @version 1.0
 */
@signalslot
public interface IHeroPolishDivisionUpgrade {

	/**
	 * 洗练升级事件
	 * 
	 * @param beforeLvl
	 * @param afterLvl
	 */
	void onHeroPolishDivisionUpgrade(int beforeLvl, int afterLvl);
}

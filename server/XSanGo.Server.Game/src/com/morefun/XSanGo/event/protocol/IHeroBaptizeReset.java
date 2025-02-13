/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IHeroBaptizeReset
 * 功能描述：
 * 文件名：IHeroBaptizeReset.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 武将洗炼重置
 * 
 * @author weiyi.zhao
 * @since 2016-5-13
 * @version 1.0
 */
@signalslot
public interface IHeroBaptizeReset {

	/**
	 * 洗炼重置事件
	 * 
	 * @param heroId 武将编号
	 * @param baptizeLvl 洗炼等级
	 * @param baptizeProps 洗炼属性
	 * @param items 返还的道具
	 */
	void onReset(String heroId, int baptizeLvl, String baptizeProps, String items);
}

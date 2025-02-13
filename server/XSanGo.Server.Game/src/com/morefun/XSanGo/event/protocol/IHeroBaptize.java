/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IHeroBaptize
 * 功能描述：
 * 文件名：IHeroBaptize.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 武将洗练事件接口
 * 
 * @author zwy
 * @since 2015-11-18
 * @version 1.0
 */
@signalslot
public interface IHeroBaptize {

	/**
	 * 武将洗练事件
	 * 
	 * @param heroId 武将编号
	 * @param props 本次获得的属性值
	 * @param times 实际洗炼次数
	 */
	void onHeroBaptize(String heroId, String props, int times);
}

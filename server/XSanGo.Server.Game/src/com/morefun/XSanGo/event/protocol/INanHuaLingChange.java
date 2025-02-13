/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: INanHuaLingChange
 * 功能描述：
 * 文件名：INanHuaLingChange.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 南华幻境 南华令变化
 * @author weiyi.zhao
 * @since 2016-4-25 
 * @version 1.0
 */
@signalslot
public interface INanHuaLingChange {

	/**
	 * 变更事件
	 * @param value 变更值
	 * @param before 变前
	 * @param after 变后
	 */
	void onNanHuaLingChange(int value, int before, int after);
}

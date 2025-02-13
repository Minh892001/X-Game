/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: ICopyBegin
 * 功能描述：
 * 文件名：ICopyBegin.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.morefun.XSanGo.copy.SmallCopyT;

/**
 * 副本开始事件
 * 
 * @author zwy
 * @since 2015-11-27
 * @version 1.0
 */
@signalslot
public interface ICopyBegin {

	/**
	 * 副本开始
	 * 
	 * @param templete
	 * @param junling
	 */
	void onCopyBegin(SmallCopyT templete, int junling);
}

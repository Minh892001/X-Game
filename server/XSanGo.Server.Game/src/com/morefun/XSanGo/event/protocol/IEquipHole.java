/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: IEquipHole
 * 功能描述：
 * 文件名：IEquipHole.java
 **************************************************
 */
package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.morefun.XSanGo.equip.EquipItem;

/**
 * 装备打孔事件
 * 
 * @author zwy
 * @since 2015-11-26
 * @version 1.0
 */
@signalslot
public interface IEquipHole {

	/**
	 * 装备打孔
	 * 
	 * @param equip
	 */
	void onEquipHole(EquipItem equip);
}

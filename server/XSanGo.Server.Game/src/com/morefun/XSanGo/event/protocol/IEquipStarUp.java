/**
 * 
 */
package com.morefun.XSanGo.event.protocol;

import java.util.List;
import java.util.Map;

import net.sf.signalslot_apt.annotations.signalslot;

import com.morefun.XSanGo.equip.EquipItem;

/**
 * 装备升星事件
 * 
 * @author sulingyun
 * 
 */
@signalslot
public interface IEquipStarUp {
	/**
	 * 装备升星事件
	 * 
	 * @param equip
	 *            升星的装备
	 * @param uplevel
	 *            提升星级
	 * @param deleteList
	 *            消耗装备
	 * @param money
	 *            消耗金币
	 * @param addExp
	 *            增加经验
	 * @param consumeStars
	 *            消耗升星石 key-templateid，value-num
	 */
	void onEquipStarUp(EquipItem equip, int uplevel,
			List<EquipItem> deleteList, int money, int addExp, Map<String, Integer> consumeStars);
}

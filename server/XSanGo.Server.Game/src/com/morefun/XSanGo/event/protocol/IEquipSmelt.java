package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

import com.XSanGo.Protocol.ItemView;
import com.morefun.XSanGo.equip.EquipItem;

/**
 * 装备熔炼事件
 * @author qinguofeng
 */
@signalslot
public interface IEquipSmelt {
    /**
     * @param smeltEquips 熔炼掉的装备
     * @param rewardItem 奖励的道具
     * */
    void onEquipSmelt(EquipItem[] smeltEquips, ItemView[] rewardItem, int money);
}

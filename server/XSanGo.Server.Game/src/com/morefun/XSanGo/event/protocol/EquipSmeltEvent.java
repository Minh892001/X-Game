// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class EquipSmeltEvent implements IEquipSmelt{
   private IEventDispatcher dispatcher;
   public EquipSmeltEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onEquipSmelt(com.morefun.XSanGo.equip.EquipItem[] smeltEquips,com.XSanGo.Protocol.ItemView[] rewardItem,int money) {
      this.dispatcher.emit(IEquipSmelt.class,new Object[]{smeltEquips,rewardItem,money});
   }

}

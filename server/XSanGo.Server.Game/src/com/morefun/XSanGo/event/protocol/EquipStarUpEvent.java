// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class EquipStarUpEvent implements IEquipStarUp{
   private IEventDispatcher dispatcher;
   public EquipStarUpEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onEquipStarUp(com.morefun.XSanGo.equip.EquipItem equip,int uplevel,java.util.List<com.morefun.XSanGo.equip.EquipItem> deleteList,int money,int addExp,java.util.Map<java.lang.String,java.lang.Integer> consumeStars) {
      this.dispatcher.emit(IEquipStarUp.class,new Object[]{equip,uplevel,deleteList,money,addExp,consumeStars});
   }

}

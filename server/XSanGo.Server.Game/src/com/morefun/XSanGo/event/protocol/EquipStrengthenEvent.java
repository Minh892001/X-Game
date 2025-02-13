// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class EquipStrengthenEvent implements IEquipStrengthen{
   private IEventDispatcher dispatcher;
   public EquipStrengthenEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onEquipStrengthen(int auto,com.morefun.XSanGo.equip.EquipItem equip,int beforeLevel,int afterLevel) {
      this.dispatcher.emit(IEquipStrengthen.class,new Object[]{auto,equip,beforeLevel,afterLevel});
   }

}

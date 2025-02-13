// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class EquipRebuildEvent implements IEquipRebuild{
   private IEventDispatcher dispatcher;
   public EquipRebuildEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onEquipRebuild(com.morefun.XSanGo.equip.EquipItem equip) {
      this.dispatcher.emit(IEquipRebuild.class,new Object[]{equip});
   }

}

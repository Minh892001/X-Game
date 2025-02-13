// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class CombatPowerChangeEvent implements ICombatPowerChange{
   private IEventDispatcher dispatcher;
   public CombatPowerChangeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onCombatPowerChange(int old,int newValue) {
      this.dispatcher.emit(ICombatPowerChange.class,new Object[]{old,newValue});
   }

}

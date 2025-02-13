// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class SuperChargeRaffleNumEvent implements ISuperChargeRaffleNum{
   private IEventDispatcher dispatcher;
   public SuperChargeRaffleNumEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onRaffleNumUpdate(int oldValue,int newValue) {
      this.dispatcher.emit(ISuperChargeRaffleNum.class,new Object[]{oldValue,newValue});
   }

}

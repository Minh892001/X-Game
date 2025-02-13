// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class SuperuChargeEvent implements ISuperuCharge{
   private IEventDispatcher dispatcher;
   public SuperuChargeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onReceiveChargeReward(int scriptId) {
      this.dispatcher.emit(ISuperuCharge.class,new Object[]{scriptId});
   }

}

// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class GainDayChargeEvent implements IGainDayCharge{
   private IEventDispatcher dispatcher;
   public GainDayChargeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onGainDayCharge(int charge,java.lang.String itemId) {
      this.dispatcher.emit(IGainDayCharge.class,new Object[]{charge,itemId});
   }

}

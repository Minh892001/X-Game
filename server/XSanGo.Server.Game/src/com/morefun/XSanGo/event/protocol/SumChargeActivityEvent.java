// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class SumChargeActivityEvent implements ISumChargeActivity{
   private IEventDispatcher dispatcher;
   public SumChargeActivityEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onReceiveSumReward(int threshold) {
      this.dispatcher.emit(ISumChargeActivity.class,new Object[]{threshold});
   }

}

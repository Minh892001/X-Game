// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ArenaClearCDEvent implements IArenaClearCD{
   private IEventDispatcher dispatcher;
   public ArenaClearCDEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onClear(int num,int cost) {
      this.dispatcher.emit(IArenaClearCD.class,new Object[]{num,cost});
   }

}

// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class GainDayConsumeEvent implements IGainDayConsume{
   private IEventDispatcher dispatcher;
   public GainDayConsumeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onGainDayConsume(int consume,java.lang.String itemId) {
      this.dispatcher.emit(IGainDayConsume.class,new Object[]{consume,itemId});
   }

}

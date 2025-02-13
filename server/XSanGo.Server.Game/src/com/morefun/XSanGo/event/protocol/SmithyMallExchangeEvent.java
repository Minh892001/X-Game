// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class SmithyMallExchangeEvent implements ISmithyMallExchange{
   private IEventDispatcher dispatcher;
   public SmithyMallExchangeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onExchange(java.lang.String itemId,int cost) {
      this.dispatcher.emit(ISmithyMallExchange.class,new Object[]{itemId,cost});
   }

}

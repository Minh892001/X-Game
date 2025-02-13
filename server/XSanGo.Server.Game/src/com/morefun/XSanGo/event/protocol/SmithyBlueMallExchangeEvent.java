// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class SmithyBlueMallExchangeEvent implements ISmithyBlueMallExchange{
   private IEventDispatcher dispatcher;
   public SmithyBlueMallExchangeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onExchange(java.lang.String itemId,int cost) {
      this.dispatcher.emit(ISmithyBlueMallExchange.class,new Object[]{itemId,cost});
   }

}

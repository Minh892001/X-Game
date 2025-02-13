// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class TraderCallEvent implements ITraderCall{
   private IEventDispatcher dispatcher;
   public TraderCallEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onTraderCalled(com.morefun.XSanGo.trader.TraderType traderType,com.XSanGo.Protocol.CurrencyType currencyType) {
      this.dispatcher.emit(ITraderCall.class,new Object[]{traderType,currencyType});
   }

}

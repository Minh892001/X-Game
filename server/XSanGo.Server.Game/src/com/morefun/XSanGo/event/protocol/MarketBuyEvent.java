// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class MarketBuyEvent implements IMarketBuy{
   private IEventDispatcher dispatcher;
   public MarketBuyEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onSnsSendJunLing(java.lang.String sender,java.lang.String accepter,int num) {
      this.dispatcher.emit(IMarketBuy.class,new Object[]{sender,accepter,num});
   }

}

// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class GoodsExchangeEvent implements IGoodsExchange{
   private IEventDispatcher dispatcher;
   public GoodsExchangeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onExchangeItem(java.lang.String templateId,java.lang.String exchangeNo,int type,int num) {
      this.dispatcher.emit(IGoodsExchange.class,new Object[]{templateId,exchangeNo,type,num});
   }

}

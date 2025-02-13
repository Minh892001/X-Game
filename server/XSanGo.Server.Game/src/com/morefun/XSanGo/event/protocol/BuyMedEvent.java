// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class BuyMedEvent implements IBuyMed{
   private IEventDispatcher dispatcher;
   public BuyMedEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onSnsSendJunLing(java.lang.String sender,java.lang.String accepter,int num) {
      this.dispatcher.emit(IBuyMed.class,new Object[]{sender,accepter,num});
   }

}

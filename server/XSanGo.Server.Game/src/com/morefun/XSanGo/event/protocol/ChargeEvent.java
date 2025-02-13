// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ChargeEvent implements ICharge{
   private IEventDispatcher dispatcher;
   public ChargeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onCharge(com.XSanGo.Protocol.CustomChargeParams params,int returnYuanbao,java.lang.String orderId,java.lang.String currency) {
      this.dispatcher.emit(ICharge.class,new Object[]{params,returnYuanbao,orderId,currency});
   }

}

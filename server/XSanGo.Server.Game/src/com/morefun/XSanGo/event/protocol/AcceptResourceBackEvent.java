// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AcceptResourceBackEvent implements IAcceptResourceBack{
   private IEventDispatcher dispatcher;
   public AcceptResourceBackEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAcceptResourceBack(java.lang.String date,java.lang.String items,com.XSanGo.Protocol.Money money) {
      this.dispatcher.emit(IAcceptResourceBack.class,new Object[]{date,items,money});
   }

}

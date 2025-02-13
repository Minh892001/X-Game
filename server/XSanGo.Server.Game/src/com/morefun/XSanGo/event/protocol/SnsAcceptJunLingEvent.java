// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class SnsAcceptJunLingEvent implements ISnsAcceptJunLing{
   private IEventDispatcher dispatcher;
   public SnsAcceptJunLingEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onSnsAcceptJunLing(java.lang.String accepter,java.lang.String sender) {
      this.dispatcher.emit(ISnsAcceptJunLing.class,new Object[]{accepter,sender});
   }

}

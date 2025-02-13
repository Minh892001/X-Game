// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class SnsSendJunLingEvent implements ISnsSendJunLing{
   private IEventDispatcher dispatcher;
   public SnsSendJunLingEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onSnsSendJunLing(java.lang.String sender,java.lang.String accepter,int num) {
      this.dispatcher.emit(ISnsSendJunLing.class,new Object[]{sender,accepter,num});
   }

}

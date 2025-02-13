// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class SendVitFromSnsEvent implements ISendVitFromSns{
   private IEventDispatcher dispatcher;
   public SendVitFromSnsEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onSend(java.util.Collection<java.lang.String> targetRoleIdCollection) {
      this.dispatcher.emit(ISendVitFromSns.class,new Object[]{targetRoleIdCollection});
   }

}

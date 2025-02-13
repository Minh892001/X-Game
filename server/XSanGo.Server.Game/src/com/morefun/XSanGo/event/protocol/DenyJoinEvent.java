// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class DenyJoinEvent implements IDenyJoin{
   private IEventDispatcher dispatcher;
   public DenyJoinEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onDenyJoin(java.lang.String factionId,java.lang.String roleId) {
      this.dispatcher.emit(IDenyJoin.class,new Object[]{factionId,roleId});
   }

}

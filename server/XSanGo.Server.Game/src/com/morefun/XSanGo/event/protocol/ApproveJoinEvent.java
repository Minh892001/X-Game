// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ApproveJoinEvent implements IApproveJoin{
   private IEventDispatcher dispatcher;
   public ApproveJoinEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onApproveJoin(java.lang.String factionId,java.lang.String roleId) {
      this.dispatcher.emit(IApproveJoin.class,new Object[]{factionId,roleId});
   }

}

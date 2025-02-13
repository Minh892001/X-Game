// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class DeleteFactionMenberEvent implements IDeleteFactionMenber{
   private IEventDispatcher dispatcher;
   public DeleteFactionMenberEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onDeleteFactionMenber(java.lang.String factionId,java.lang.String roleId) {
      this.dispatcher.emit(IDeleteFactionMenber.class,new Object[]{factionId,roleId});
   }

}

// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class CreateFactionEvent implements ICreateFaction{
   private IEventDispatcher dispatcher;
   public CreateFactionEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onCreateFaction(java.lang.String factionId) {
      this.dispatcher.emit(ICreateFaction.class,new Object[]{factionId});
   }

}

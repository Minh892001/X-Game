// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class QuitFactionEvent implements IQuitFaction{
   private IEventDispatcher dispatcher;
   public QuitFactionEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onQuitFaction(java.lang.String factionId,java.lang.String roleId) {
      this.dispatcher.emit(IQuitFaction.class,new Object[]{factionId,roleId});
   }

}

// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FactionLevelUpEvent implements IFactionLevelUp{
   private IEventDispatcher dispatcher;
   public FactionLevelUpEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onFactionLevelUp(java.lang.String factionId,int level) {
      this.dispatcher.emit(IFactionLevelUp.class,new Object[]{factionId,level});
   }

}

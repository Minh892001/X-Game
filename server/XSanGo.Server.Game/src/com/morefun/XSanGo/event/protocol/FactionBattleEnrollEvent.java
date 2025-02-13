// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FactionBattleEnrollEvent implements IFactionBattleEnroll{
   private IEventDispatcher dispatcher;
   public FactionBattleEnrollEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onEnroll(int campId,int dutyId,java.lang.String factionId) {
      this.dispatcher.emit(IFactionBattleEnroll.class,new Object[]{campId,dutyId,factionId});
   }

}

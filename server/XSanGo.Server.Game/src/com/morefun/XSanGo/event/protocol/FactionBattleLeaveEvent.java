// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FactionBattleLeaveEvent implements IFactionBattleLeave{
   private IEventDispatcher dispatcher;
   public FactionBattleLeaveEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onLeave(int strongholdId) {
      this.dispatcher.emit(IFactionBattleLeave.class,new Object[]{strongholdId});
   }

}

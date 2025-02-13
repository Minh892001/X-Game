// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FactionBattleUseKitsEvent implements IFactionBattleUseKits{
   private IEventDispatcher dispatcher;
   public FactionBattleUseKitsEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onUseKits(int kitsId) {
      this.dispatcher.emit(IFactionBattleUseKits.class,new Object[]{kitsId});
   }

}

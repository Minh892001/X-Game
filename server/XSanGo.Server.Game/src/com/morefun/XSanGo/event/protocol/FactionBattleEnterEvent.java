// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FactionBattleEnterEvent implements IFactionBattleEnter{
   private IEventDispatcher dispatcher;
   public FactionBattleEnterEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onEnter(int strongholdId,int gainKitsId) {
      this.dispatcher.emit(IFactionBattleEnter.class,new Object[]{strongholdId,gainKitsId});
   }

}

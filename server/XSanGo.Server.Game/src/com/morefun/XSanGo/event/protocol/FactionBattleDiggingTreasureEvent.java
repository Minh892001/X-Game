// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FactionBattleDiggingTreasureEvent implements IFactionBattleDiggingTreasure{
   private IEventDispatcher dispatcher;
   public FactionBattleDiggingTreasureEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onDiggingTreasure(int strongholdId,int forage,com.XSanGo.Protocol.IntString[] views) {
      this.dispatcher.emit(IFactionBattleDiggingTreasure.class,new Object[]{strongholdId,forage,views});
   }

}

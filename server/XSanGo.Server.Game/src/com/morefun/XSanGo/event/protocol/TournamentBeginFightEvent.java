// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class TournamentBeginFightEvent implements ITournamentBeginFight{
   private IEventDispatcher dispatcher;
   public TournamentBeginFightEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBeginFight(java.lang.String opponentId,int reduceCount,int count) {
      this.dispatcher.emit(ITournamentBeginFight.class,new Object[]{opponentId,reduceCount,count});
   }

}

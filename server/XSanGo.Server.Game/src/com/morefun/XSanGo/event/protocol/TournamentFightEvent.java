// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class TournamentFightEvent implements ITournamentFight{
   private IEventDispatcher dispatcher;
   public TournamentFightEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onFight(java.lang.String opponentId,int winornot,java.lang.String extra) {
      this.dispatcher.emit(ITournamentFight.class,new Object[]{opponentId,winornot,extra});
   }

}

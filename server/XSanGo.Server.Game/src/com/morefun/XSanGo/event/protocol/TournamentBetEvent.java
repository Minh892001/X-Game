// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class TournamentBetEvent implements ITournamentBet{
   private IEventDispatcher dispatcher;
   public TournamentBetEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBet(com.morefun.XSanGo.db.game.RoleTournamentBet bet) {
      this.dispatcher.emit(ITournamentBet.class,new Object[]{bet});
   }

}

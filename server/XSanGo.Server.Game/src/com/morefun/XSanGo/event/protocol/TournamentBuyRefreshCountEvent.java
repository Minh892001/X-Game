// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class TournamentBuyRefreshCountEvent implements ITournamentBuyRefreshCount{
   private IEventDispatcher dispatcher;
   public TournamentBuyRefreshCountEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBuyRefreshCount(int price) {
      this.dispatcher.emit(ITournamentBuyRefreshCount.class,new Object[]{price});
   }

}

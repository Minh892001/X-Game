// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FootballBetEvent implements IFootballBet{
   private IEventDispatcher dispatcher;
   public FootballBetEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onFootballBet(int id,int num,int countryId) {
      this.dispatcher.emit(IFootballBet.class,new Object[]{id,num,countryId});
   }

}

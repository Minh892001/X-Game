// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ChallengeTraderHeroEvent implements IChallengeTraderHero{
   private IEventDispatcher dispatcher;
   public ChallengeTraderHeroEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onChallengeHeroTrader(com.XSanGo.Protocol.HeroCallResult callResult) {
      this.dispatcher.emit(IChallengeTraderHero.class,new Object[]{callResult});
   }

}

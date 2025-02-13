// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class BuyLimitHeroEvent implements IBuyLimitHero{
   private IEventDispatcher dispatcher;
   public BuyLimitHeroEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBuyLimitHero(java.util.List<com.XSanGo.Protocol.BuyHeroResult> results) {
      this.dispatcher.emit(IBuyLimitHero.class,new Object[]{results});
   }

}

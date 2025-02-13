// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class BuyChallengeChanceEvent implements IBuyChallengeChance{
   private IEventDispatcher dispatcher;
   public BuyChallengeChanceEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBuyChallengeChance(int copyId,int price) {
      this.dispatcher.emit(IBuyChallengeChance.class,new Object[]{copyId,price});
   }

}

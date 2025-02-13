// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ArenaBuyChallengeEvent implements IArenaBuyChallenge{
   private IEventDispatcher dispatcher;
   public ArenaBuyChallengeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onbuy(int num,int count,int cost) {
      this.dispatcher.emit(IArenaBuyChallenge.class,new Object[]{num,count,cost});
   }

}

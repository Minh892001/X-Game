// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AcceptFundRewardEvent implements IAcceptFundReward{
   private IEventDispatcher dispatcher;
   public AcceptFundRewardEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAcceptFundReward(int level,int money) {
      this.dispatcher.emit(IAcceptFundReward.class,new Object[]{level,money});
   }

}

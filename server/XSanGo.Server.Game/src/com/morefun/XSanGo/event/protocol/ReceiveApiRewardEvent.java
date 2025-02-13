// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ReceiveApiRewardEvent implements IReceiveApiReward{
   private IEventDispatcher dispatcher;
   public ReceiveApiRewardEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onReceiveApiReward(int actId,int rewardId) {
      this.dispatcher.emit(IReceiveApiReward.class,new Object[]{actId,rewardId});
   }

}

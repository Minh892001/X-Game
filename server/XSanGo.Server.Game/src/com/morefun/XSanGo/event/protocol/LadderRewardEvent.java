// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class LadderRewardEvent implements ILadderReward{
   private IEventDispatcher dispatcher;
   public LadderRewardEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onReward(int rewardId) {
      this.dispatcher.emit(ILadderReward.class,new Object[]{rewardId});
   }

}

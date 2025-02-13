// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ShootScoreRecvRewardEvent implements IShootScoreRecvReward{
   private IEventDispatcher dispatcher;
   public ShootScoreRecvRewardEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onShootScoreRecvReward(java.lang.String score,java.util.Map<java.lang.String,java.lang.Integer> itemsMap) {
      this.dispatcher.emit(IShootScoreRecvReward.class,new Object[]{score,itemsMap});
   }

}

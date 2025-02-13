// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AttackCastleAcceptRewardEvent implements IAttackCastleAcceptReward{
   private IEventDispatcher dispatcher;
   public AttackCastleAcceptRewardEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAcceptReward(int nodeId,int reputation,com.XSanGo.Protocol.ItemView[] rewards) {
      this.dispatcher.emit(IAttackCastleAcceptReward.class,new Object[]{nodeId,reputation,rewards});
   }

}

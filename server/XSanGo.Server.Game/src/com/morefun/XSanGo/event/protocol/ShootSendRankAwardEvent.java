// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ShootSendRankAwardEvent implements IShootSendRankAward{
   private IEventDispatcher dispatcher;
   public ShootSendRankAwardEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onSendRankAward(int serverId,int score,int rank,java.util.Map<java.lang.String,java.lang.Integer> itemsMap) {
      this.dispatcher.emit(IShootSendRankAward.class,new Object[]{serverId,score,rank,itemsMap});
   }

}

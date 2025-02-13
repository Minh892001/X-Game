// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ActPointRewardEvent implements IActPointReward{
   private IEventDispatcher dispatcher;
   public ActPointRewardEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAccept(int point) {
      this.dispatcher.emit(IActPointReward.class,new Object[]{point});
   }

}

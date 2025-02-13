// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AcceptDayforverLoginRewardEvent implements IAcceptDayforverLoginReward{
   private IEventDispatcher dispatcher;
   public AcceptDayforverLoginRewardEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAcceptDayforverLoginReward(int level,java.lang.String templateId) {
      this.dispatcher.emit(IAcceptDayforverLoginReward.class,new Object[]{level,templateId});
   }

}

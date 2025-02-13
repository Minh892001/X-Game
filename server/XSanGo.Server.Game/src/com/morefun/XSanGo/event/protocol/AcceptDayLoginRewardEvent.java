// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AcceptDayLoginRewardEvent implements IAcceptDayLoginReward{
   private IEventDispatcher dispatcher;
   public AcceptDayLoginRewardEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAcceptDayLoginReward(int level,java.lang.String templateId) {
      this.dispatcher.emit(IAcceptDayLoginReward.class,new Object[]{level,templateId});
   }

}

// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AcceptFirstJiaRewardEvent implements IAcceptFirstJiaReward{
   private IEventDispatcher dispatcher;
   public AcceptFirstJiaRewardEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAcceptLevelReward(int level,java.lang.String templateId) {
      this.dispatcher.emit(IAcceptFirstJiaReward.class,new Object[]{level,templateId});
   }

}

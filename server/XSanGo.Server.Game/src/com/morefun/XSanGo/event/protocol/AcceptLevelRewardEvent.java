// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AcceptLevelRewardEvent implements IAcceptLevelReward{
   private IEventDispatcher dispatcher;
   public AcceptLevelRewardEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAcceptLevelReward(int level,java.lang.String templateId) {
      this.dispatcher.emit(IAcceptLevelReward.class,new Object[]{level,templateId});
   }

}

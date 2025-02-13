// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AcceptPowerRewardEvent implements IAcceptPowerReward{
   private IEventDispatcher dispatcher;
   public AcceptPowerRewardEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAcceptPowerReward(int power,java.lang.String templateId) {
      this.dispatcher.emit(IAcceptPowerReward.class,new Object[]{power,templateId});
   }

}

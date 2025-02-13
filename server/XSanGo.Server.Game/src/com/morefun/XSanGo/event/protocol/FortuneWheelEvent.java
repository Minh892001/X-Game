// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FortuneWheelEvent implements IFortuneWheel{
   private IEventDispatcher dispatcher;
   public FortuneWheelEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onFortuneWheel(int count,com.XSanGo.Protocol.ItemView[] rewards,int lastCount) {
      this.dispatcher.emit(IFortuneWheel.class,new Object[]{count,rewards,lastCount});
   }

}

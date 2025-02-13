// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class VipEndAnswerEvent implements IVipEndAnswer{
   private IEventDispatcher dispatcher;
   public VipEndAnswerEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onVipEndAnswer(int addVipExp) {
      this.dispatcher.emit(IVipEndAnswer.class,new Object[]{addVipExp});
   }

}

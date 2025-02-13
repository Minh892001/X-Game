// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AcceptSendJunLingEvent implements IAcceptSendJunLing{
   private IEventDispatcher dispatcher;
   public AcceptSendJunLingEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAcceptSendJunLing(int junlingCount,int rmbyCount) {
      this.dispatcher.emit(IAcceptSendJunLing.class,new Object[]{junlingCount,rmbyCount});
   }

}

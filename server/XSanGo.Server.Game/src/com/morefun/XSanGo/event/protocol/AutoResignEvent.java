// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AutoResignEvent implements IAutoResign{
   private IEventDispatcher dispatcher;
   public AutoResignEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void resign(java.lang.Integer[] resignedDay,int totalCost) {
      this.dispatcher.emit(IAutoResign.class,new Object[]{resignedDay,totalCost});
   }

}

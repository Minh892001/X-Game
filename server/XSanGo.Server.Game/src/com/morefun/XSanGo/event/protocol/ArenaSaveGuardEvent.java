// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ArenaSaveGuardEvent implements IArenaSaveGuard{
   private IEventDispatcher dispatcher;
   public ArenaSaveGuardEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onSave(java.lang.String guardId) {
      this.dispatcher.emit(IArenaSaveGuard.class,new Object[]{guardId});
   }

}

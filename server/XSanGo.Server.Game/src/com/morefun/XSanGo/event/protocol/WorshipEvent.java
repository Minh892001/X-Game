// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class WorshipEvent implements IWorship{
   private IEventDispatcher dispatcher;
   public WorshipEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onWorship(java.lang.String roleId,int count) {
      this.dispatcher.emit(IWorship.class,new Object[]{roleId,count});
   }

}

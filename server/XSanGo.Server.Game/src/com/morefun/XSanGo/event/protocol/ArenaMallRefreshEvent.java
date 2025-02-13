// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ArenaMallRefreshEvent implements IArenaMallRefresh{
   private IEventDispatcher dispatcher;
   public ArenaMallRefreshEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onRefresh(int num,int cost,java.lang.String itemStr) {
      this.dispatcher.emit(IArenaMallRefresh.class,new Object[]{num,cost,itemStr});
   }

}

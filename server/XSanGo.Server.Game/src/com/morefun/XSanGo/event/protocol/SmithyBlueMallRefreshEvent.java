// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class SmithyBlueMallRefreshEvent implements ISmithyBlueMallRefresh{
   private IEventDispatcher dispatcher;
   public SmithyBlueMallRefreshEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onRefresh(int num,int cost,java.lang.String itemStr,java.util.Date lastRefTime) {
      this.dispatcher.emit(ISmithyBlueMallRefresh.class,new Object[]{num,cost,itemStr,lastRefTime});
   }

}

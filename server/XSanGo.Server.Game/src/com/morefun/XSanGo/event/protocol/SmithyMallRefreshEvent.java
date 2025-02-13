// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class SmithyMallRefreshEvent implements ISmithyMallRefresh{
   private IEventDispatcher dispatcher;
   public SmithyMallRefreshEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onRefresh(int num,int cost,java.lang.String itemStr,java.util.Date lastRefTime) {
      this.dispatcher.emit(ISmithyMallRefresh.class,new Object[]{num,cost,itemStr,lastRefTime});
   }

}

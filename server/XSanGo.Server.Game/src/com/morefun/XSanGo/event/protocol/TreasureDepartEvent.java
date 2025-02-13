// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class TreasureDepartEvent implements ITreasureDepart{
   private IEventDispatcher dispatcher;
   public TreasureDepartEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onDepart(java.lang.String heroIds,int recommendNum) {
      this.dispatcher.emit(ITreasureDepart.class,new Object[]{heroIds,recommendNum});
   }

}

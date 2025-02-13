// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class TreasureAddGroupEvent implements ITreasureAddGroup{
   private IEventDispatcher dispatcher;
   public TreasureAddGroupEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAddGroup(int currentGroupNum) {
      this.dispatcher.emit(ITreasureAddGroup.class,new Object[]{currentGroupNum});
   }

}

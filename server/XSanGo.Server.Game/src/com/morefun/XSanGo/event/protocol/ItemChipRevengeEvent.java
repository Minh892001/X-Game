// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ItemChipRevengeEvent implements IItemChipRevenge{
   private IEventDispatcher dispatcher;
   public ItemChipRevengeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onRevenge(int flag,java.lang.String itemChipId) {
      this.dispatcher.emit(IItemChipRevenge.class,new Object[]{flag,itemChipId});
   }

}

// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ItemCountChangeEvent implements IItemCountChange{
   private IEventDispatcher dispatcher;
   public ItemCountChangeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onItemCountChange(com.morefun.XSanGo.item.IItem item,int change) {
      this.dispatcher.emit(IItemCountChange.class,new Object[]{item,change});
   }

}

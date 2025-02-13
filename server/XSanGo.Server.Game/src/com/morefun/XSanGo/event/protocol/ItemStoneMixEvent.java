// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ItemStoneMixEvent implements IItemStoneMix{
   private IEventDispatcher dispatcher;
   public ItemStoneMixEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onStoneMixAddChange(int lvl,int num) {
      this.dispatcher.emit(IItemStoneMix.class,new Object[]{lvl,num});
   }

}
